package warehouse.behaviours.cycle;

import commons.exceptions.MsgCreateFailedException;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import commons.WorkpieceInfo;
import jade.core.behaviours.Behaviour;
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
    private WarehouseSqlite sqlite;

    public RawContractNetResponder(WarehouseAgent whagent, MessageTemplate mt) {
        super(whagent, mt);
        this.whagent = whagent;
        sqlite = new WarehouseSqlite(whagent.getSqlitePath());
        sqlite.initTable();
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
        // cfp 内容: WorkpieceInfo
        String goodsid = null;
        try {
            WorkpieceInfo wpInfo = (WorkpieceInfo) cfp.getContentObject();
            goodsid = wpInfo.getGoodsId();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
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
            // 对 workpieceInfo 添加 warehousePosition
            WorkpieceInfo wpInfo = ((WorkpieceInfo)cfp.getContentObject());
            String goodsid = wpInfo.getGoodsId();
            Integer position = sqlite.getRaw(goodsid);
            wpInfo.setWarehousePosition(position);
            // 更新 wpInfo
            wpInfo.setProviderId(whagent.getLocalName());
            wpInfo.setCurOwnerId(whagent.getLocalName());
            // 设置bufferPos,  agv取货用
            wpInfo.setBufferPos(whagent.getPosIn());

            // 发送至Worker 进行下一轮招标
            ACLMessage msg = DFUtils.createRequestMsg(wpInfo);
            msg = DFUtils.searchDF(whagent, msg, DFServiceType.WORKER);
            whagent.send(msg);

            // 完成本次招标动作
            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            return inform;
//            String goodsid = ((WorkpieceInfo)cfp.getContentObject()).getGoodsId();
//            if (quantity == sqlite.getRawQuantityByGoodsId(goodsid)) {
//                ACLMessage inform = accept.createReply();
//                inform.setPerformative(ACLMessage.INFORM);
//
//                String position = sqlite.getRaw(goodsid);
//                // inform.setContent(position);
//
//                return inform;
//            } else {
//                throw new FailureException("Quantity changed.");
//            }
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("Get goodsid failed");
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailureException(e.getMessage());
        }
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }
}