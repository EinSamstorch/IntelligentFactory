package warehouse.behaviours;

import commons.tools.LoggerUtil;
import commons.WorkpieceInfo;
import warehouse.WarehouseAgent;
import warehouse.WarehouseSqlite;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;

/**
 * 原材料 合同网 招标回应方.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RawContractNetResponder extends ContractNetResponder {
    private WarehouseAgent whagent;

    public RawContractNetResponder(WarehouseAgent whagent, MessageTemplate mt) {
        super(whagent, mt);
        this.whagent = whagent;
    }

    /**
     * 处理原料招标请求
     *
     * @param cfp 招标信息， content: String goodsid
     * @return ACLMessage content: String quantity
     * @throws RefuseException
     * @throws FailureException
     * @throws NotUnderstoodException
     */
    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
        // cfp 内容: String goodsid
        String goodsid = null;
        try {
            WorkpieceInfo wpInfo = (WorkpieceInfo) cfp.getContentObject();
            goodsid = wpInfo.getGoodsId();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        WarehouseSqlite sqlite = new WarehouseSqlite(whagent.getSqlitePath());
        // 查询仓库余量
        int quantity = sqlite.getRawQuantityByGoodsId(goodsid);
        if (quantity > 0) {
            // propose 内容: String quantity
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(quantity));
            return propose;
        } else {
            // 仓库余量不足
            LoggerUtil.agent.info(String.format("Goodsid: %s is out of stock.", goodsid));
            throw new RefuseException(String.format("Goodsid: %s is out of stock.", goodsid));
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
        int quantity = Integer.parseInt(accept.getContent());

        try {
            String goodsid = ((WorkpieceInfo)cfp.getContentObject()).getGoodsId();
            WarehouseSqlite sqlite = new WarehouseSqlite(whagent.getSqlitePath());
            if (quantity == sqlite.getRawQuantityByGoodsId(goodsid)) {
                ACLMessage inform = accept.createReply();
                inform.setPerformative(ACLMessage.INFORM);

                String position = sqlite.getRaw(goodsid);
                inform.setContent(position);
                return inform;
            } else {
                throw new FailureException("Quantity changed.");
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("Get goodsid failed");
        }

    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }
}
