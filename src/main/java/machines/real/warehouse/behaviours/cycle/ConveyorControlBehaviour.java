package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.commons.request.WarehouseConveyorRequest;
import machines.real.warehouse.WarehouseHal;
import machines.real.warehouse.behaviours.simple.ConveyorActionBehaviour;

/**
 * 传送带控制.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ConveyorControlBehaviour extends CyclicBehaviour {

  private WarehouseHal hal;
  private MessageTemplate mt = MessageTemplate
      .and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
          MessageTemplate.MatchLanguage(WarehouseConveyorRequest.LANGUAGE));
  private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

  public void setHal(WarehouseHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      block();
      return;
    }
    WarehouseConveyorRequest request;
    try {
      request = (WarehouseConveyorRequest) receive.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      LoggerUtil.agent.warn("Deserialization Error. " + e.getLocalizedMessage());
      return;
    }
    Behaviour b = tbf.wrap(new ConveyorActionBehaviour(request.isImportMode(), hal));
    myAgent.addBehaviour(b);
    while (!b.done()) {
      block(1000);
    }
    ACLMessage reply = receive.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    myAgent.send(reply);
  }
}
