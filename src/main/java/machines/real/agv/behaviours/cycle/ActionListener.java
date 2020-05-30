package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.behaviours.simple.ActionExecutor;
import machines.real.commons.hal.MiddleHal;

/**
 * AgvInstance监听动作请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ActionListener extends CyclicBehaviour {

  private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
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

    Behaviour b = tbf.wrap(new ActionExecutor(action, hal, receive));
    myAgent.addBehaviour(b);
    while (!b.done()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
