package machines.real.warehouse.behaviours.cycle;

import commons.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import machines.real.commons.TransportRequest;
import machines.real.warehouse.WarehouseAgent;
import machines.real.warehouse.WarehouseSqlite;
import machines.real.warehouse.behaviours.simple.CallForAgv;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProductContractNetResponder extends ContractNetResponder {
    private WarehouseAgent whagent;
    private WarehouseSqlite sqlite;

    public ProductContractNetResponder(WarehouseAgent whagent, MessageTemplate mt){
        super(whagent, mt);
        this.whagent = whagent;
        sqlite = whagent.getSqlite();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
        int quantity = sqlite.getProductQuantity();
        if(quantity > 0) {
            ACLMessage propose = cfp.createReply();
            propose.setPerformative(ACLMessage.PROPOSE);
            propose.setContent(String.valueOf(quantity));
            return propose;
        } else {
            LoggerUtil.agent.info("Product Full!");
            throw new RefuseException("Product Full!");
        }
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
        int quantity = Integer.parseInt(accept.getContent());
        WorkpieceInfo wpInfo = null;
        try {
            wpInfo = (WorkpieceInfo)cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        if(wpInfo != null) {
            int position = sqlite.getProduct(wpInfo);
            wpInfo.setWarehousePosition(position);
            int currentLocation = wpInfo.getBufferPos();
            // 更新 wpInfo
            wpInfo.setCurOwnerId(whagent.getLocalName());
            // 入库口
            wpInfo.setBufferPos(25);

            // call for agv
            TransportRequest request = new TransportRequest(currentLocation, 25, wpInfo);
            whagent.addBehaviour(new CallForAgv(whagent, request));

            ACLMessage inform = accept.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            return inform;
        } else {
            throw new FailureException("WorkpieceInfo NPE Error.");
        }
    }
}