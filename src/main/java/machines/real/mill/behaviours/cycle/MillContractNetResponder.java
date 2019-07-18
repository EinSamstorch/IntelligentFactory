package machines.real.mill.behaviours.cycle;

import commons.Buffer;
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
import machines.real.mill.MillAgent;
import machines.real.mill.MillHal;
import machines.real.mill.behaviours.simple.CallForAgv;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillContractNetResponder extends ContractNetResponder {
    private MillAgent magent;
    private MillHal hal;
    public MillContractNetResponder(MillAgent a, MessageTemplate mt) {
        super(a, mt);
        magent = a;
        hal = a.getHal();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
        if(magent.isBufferFull()) {
            throw new RefuseException("Buffer Full!");
        }
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
        // gather old info
        int from;
        if(wpInfo.getBufferPos() != 0) {
            from = wpInfo.getBufferPos();
        } else {
            throw new FailureException("Unknown workpiece location.");
        }
        // 更新wpInfo
        wpInfo.setCurOwnerId(magent.getLocalName());
        Buffer buffer = magent.getBufferByIndex(magent.getEmptyBuffer());
        int to = buffer.getIndex();
        wpInfo.setBufferPos(to);
        // 放入机床buffer中
        buffer.setWpInfo(wpInfo);
        // call for agv
        TransportRequest request = new TransportRequest(from, to, wpInfo);
        magent.addBehaviour(new CallForAgv(magent, request, buffer));
        // 完成本次招标动作
        ACLMessage inform = accept.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        return inform;
    }
}
