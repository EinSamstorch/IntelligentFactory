package lathe.behaviours.cycle;

import commons.Buffer;
import commons.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import lathe.LatheAgent;
import lathe.LatheHal;

/**
 * 车加工 招标应答者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class LatheContractNetResponder extends ContractNetResponder {
    private LatheAgent lagent;
    private LatheHal hal;
    public LatheContractNetResponder(LatheAgent a, MessageTemplate mt) {
        super(a, mt);
        lagent = a;
        hal = a.getHal();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
        WorkpieceInfo wpInfo;
        try {
            wpInfo = (WorkpieceInfo)cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("WorkpieceInfo read error.");
        }

        int evaluteTIme = hal.evaluate(wpInfo);

        ACLMessage propose = cfp.createReply();
        propose.setPerformative(ACLMessage.PROPOSE);
        propose.setContent(String.valueOf(evaluteTIme));
        return propose;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
        WorkpieceInfo wpInfo;
        try {
            wpInfo = (WorkpieceInfo)cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("WorkpieceInfo read error.");
        }

        // 更新wpInfo
        wpInfo.setCurOwnerId(lagent.getLocalName());
        Buffer buffer = lagent.getBufferByIndex(lagent.getEmptyBuffer());
        wpInfo.setBufferPos(buffer.getIndex());
        // 放入机床buffer中
        buffer.setWpInfo(wpInfo);
        // call for agv

        // 完成本次招标动作
        ACLMessage inform = accept.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        return inform;
    }

}
