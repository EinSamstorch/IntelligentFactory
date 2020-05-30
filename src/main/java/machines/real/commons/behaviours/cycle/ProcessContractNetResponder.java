package machines.real.commons.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import java.io.IOException;
import machines.agent.RealMachineAgent;
import machines.real.commons.ContractNetContent;
import machines.real.commons.actions.EvaluateAction;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.behaviours.sequential.CallForAgv;
import machines.real.commons.behaviours.simple.ActionExecutor;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.AgvRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessContractNetResponder extends ContractNetResponder {

  private static MessageTemplate mt = MessageTemplate.and(
      MessageTemplate.MatchPerformative(ACLMessage.CFP),
      MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_CONTRACT_NET)
  );
  private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
  private RealMachineAgent machineAgent;
  private MiddleHal hal;

  public ProcessContractNetResponder(RealMachineAgent machineAgent) {
    super(machineAgent, mt);
    this.machineAgent = machineAgent;
  }

  public void setHal(MiddleHal hal) {
    this.hal = hal;
  }

  @Override
  protected ACLMessage handleCfp(ACLMessage cfp)
      throws RefuseException, FailureException, NotUnderstoodException {
    LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
    if (!machineAgent.isAgentOnline()) {
      throw new RefuseException("Refuse CFP, due to agent is offline.");
    }
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
    int evaluateTime = evaluate(wpInfo);
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
  protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
      throws FailureException {
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
    buffer.getBufferState().setState(BufferState.STATE_ARRIVING);
    int evaluateTime = evaluate(wpInfo);
    buffer.setEvaluateTime(evaluateTime);
    // call for agv
    AgvRequest request = new AgvRequest(from, to, wpInfo);
    machineAgent.addBehaviour(tbf.wrap(new CallForAgv(request, buffer)));
    // 完成本次招标动作
    ACLMessage inform = accept.createReply();
    inform.setPerformative(ACLMessage.INFORM);
    return inform;
  }

  private int evaluate(WorkpieceStatus wpInfo) {
    MachineAction action = new EvaluateAction(wpInfo);
    while (!hal.executeAction(action)) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return (int) Float.parseFloat((String) hal.getExtra());
  }
}
