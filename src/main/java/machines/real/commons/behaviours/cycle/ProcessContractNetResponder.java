package machines.real.commons.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import machines.real.commons.ContractNetContent;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.simple.CallForAgv;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MachineHal;
import machines.real.commons.request.AgvRequest;

import java.io.IOException;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessContractNetResponder extends ContractNetResponder {
    private RealMachineAgent machineAgent;
    private MachineHal hal;

    public ProcessContractNetResponder(RealMachineAgent machineAgent, MessageTemplate mt) {
        super(machineAgent, mt);
        this.machineAgent = machineAgent;
        hal = machineAgent.getHal();
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException {
        LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
        if (machineAgent.getBufferManger().isBufferFull()) {
            throw new RefuseException("Buffer Full!");
        }
        WorkpieceStatus wpInfo;
        try {
            wpInfo = (WorkpieceStatus) cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("WorkpieceInfo read error.");
        }

        // 预估时间作为标书内容
        int evaluateTime = hal.evaluate(wpInfo);
        evaluateTime += machineAgent.getBufferManger().getAllWaitingTime();
        ContractNetContent content = new ContractNetContent(evaluateTime);

        ACLMessage propose = cfp.createReply();
        propose.setPerformative(ACLMessage.PROPOSE);
        try {
            propose.setContentObject(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propose;
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) throws FailureException {
        LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
        WorkpieceStatus wpInfo;
        try {
            wpInfo = (WorkpieceStatus) cfp.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
            throw new FailureException("WorkpieceInfo read error.");
        }
        // gather old info
        int from;
        if (wpInfo.getBufferPos() != 0) {
            from = wpInfo.getBufferPos();
        } else {
            throw new FailureException("Unknown workpiece location.");
        }
        // 更新wpInfo
        wpInfo.setCurOwnerId(machineAgent.getLocalName());
        Buffer buffer = machineAgent.getBufferManger().getEmptyBuffer();
        if (buffer == null) {
            throw new FailureException("Buffer Full!");
        }
        int to = buffer.getIndex();
        wpInfo.setBufferPos(to);
        // 放入机床buffer中
        buffer.setWpInfo(wpInfo);
        buffer.setEvaluateTime(hal.evaluate(wpInfo));
        // call for agv
        AgvRequest request = new AgvRequest(from, to, wpInfo);
        machineAgent.addBehaviour(new CallForAgv(machineAgent, request, buffer));
        // 完成本次招标动作
        ACLMessage inform = accept.createReply();
        inform.setPerformative(ACLMessage.INFORM);
        return inform;
    }
}
