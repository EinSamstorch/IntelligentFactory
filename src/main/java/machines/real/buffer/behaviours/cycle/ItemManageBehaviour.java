package machines.real.buffer.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.buffer.actions.ImExportAction;
import machines.real.commons.behaviours.simple.ActionExecutor;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.BufferRequest;

public class ItemManageBehaviour extends CyclicBehaviour {

  private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
  private MiddleHal hal;

  public void setHal(MiddleHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );
    ACLMessage msg = myAgent.receive(mt);
    if (msg == null) {
      block();
      return;
    }
    BufferRequest request;
    try {
      request = (BufferRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      LoggerUtil.agent.warn("Deserialization Error in BufferRequest.");
      return;
    }
    ImExportAction action = new ImExportAction(request.isImportMode(),
        request.getBufferNo());

    myAgent.addBehaviour(tbf.wrap(new ActionExecutor(action, hal, msg)));
  }
}
