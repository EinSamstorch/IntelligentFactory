package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.hal.MiddleHal;

/**
 * AgvInstance监听动作请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ActionListener extends CyclicBehaviour {

  private MiddleHal hal;
  private MessageTemplate mt = MessageTemplate.and(
      MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
      MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_REQUEST)
  );

  public void setHal(MiddleHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      block();
      return;
    }
    MachineAction action;
    try {
      action = (MachineAction) receive.getContentObject();
    } catch (UnreadableException e) {
      LoggerUtil.agent.error("Deserialization failed! From: "
          + receive.getSender().getLocalName()
          + e.getLocalizedMessage());
      return;
    }
    LoggerUtil.agent.debug("Action: " + action.getCmd() + ", extra: " + action.getExtra());
    while (!hal.executeAction(action)) {
      block(3000);
    }
    LoggerUtil.agent.debug("Action done!");
    ACLMessage reply = receive.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    reply.setLanguage("done");
    try {
      reply.setContentObject(hal.getExtra());
    } catch (IOException e) {
      LoggerUtil.agent.error("Serialization failed!" + e.getLocalizedMessage());
      reply.setContent("");
    }
    myAgent.send(reply);
  }

}
