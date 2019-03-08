package Warehouse.Behaviours;

import CommonTools.LoggerUtil;
import Warehouse.WarehouseAgent;
import Warehouse.WarehouseSqlite;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

/**
 * 原材料 合同网 招标回应方.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RawCNResponder extends ContractNetResponder {
    private WarehouseAgent whagent;

    public RawCNResponder(WarehouseAgent whagent, MessageTemplate mt) {
        super(whagent, mt);
        this.whagent = whagent;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.info(String.format("CFP received from: %s.", cfp.getSender().getName()));
        String goodsid = cfp.getContent();
        WarehouseSqlite sqlite = new WarehouseSqlite(whagent.getSqlitePath());
        int quantity = sqlite.getRawQuantityByGoodsId(goodsid);
        if (quantity > 0) {
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(quantity));
            return propose;
        } else {
            LoggerUtil.agent.info(String.format("Goodsid: %s is out of stock.", goodsid));
            throw new RefuseException(String.format("Goodsid: %s is out of stock.", goodsid));
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        return super.handleAcceptProposal(cfp, propose, accept);
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }
}
