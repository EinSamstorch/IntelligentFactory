package machines.real.agv.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.Random;
import machines.real.commons.actions.MachineAction;

/**
 * AgvManager向Instance发送命令请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ActionCaller extends SimpleBehaviour {

  private boolean done = false;
  private AID receiver;

  private MachineAction action;
  private int step = 0;
  private String conversationId = "ActionCaller" + new Random().nextInt();

  public ActionCaller(AID receiver, MachineAction action) {
    this.receiver = receiver;
    this.action = action;
  }

  @Override
  public void action() {
    switch (step) {
      case 0:
        sendCmd();
        break;
      case 1:
        waitResponse();
        break;
      default:
        LoggerUtil.agent.error("Wrong step: " + step);
        break;
    }
    block(1000);
  }

  private void sendCmd() {
    ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
    msg.setProtocol(InteractionProtocol.FIPA_REQUEST);
    msg.addReceiver(receiver);
    msg.setConversationId(conversationId);
    try {
      msg.setContentObject(action);
    } catch (IOException e) {
      LoggerUtil.agent
          .error("Serialization failed! Cmd: " + action.getCmd() + e.getLocalizedMessage());
      return;
    }
    myAgent.send(msg);
    step++;
  }

  private void waitResponse() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchPerformative(ACLMessage.INFORM),
        MessageTemplate.MatchConversationId(conversationId)
    );
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      return;
    }
    done = "done".equalsIgnoreCase(receive.getLanguage());
    try {
      action.setResultExtra(receive.getContentObject());
    } catch (UnreadableException e) {
      LoggerUtil.agent.error("Deserialization result extra failed! From: "
          + receive.getSender().getLocalName()
          + e.getLocalizedMessage());
      action.setResultExtra("");
    }
  }

  @Override
  public boolean done() {
    return done;
  }
}
