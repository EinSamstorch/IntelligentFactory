package machines.real.buffer.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.buffer.behaviours.simple.ImExportItemBehaviour;
import machines.real.commons.hal.MiddleHal;

public class ItemManageBehaviour extends CyclicBehaviour {

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
    if (msg != null) {
      myAgent.addBehaviour(new ImExportItemBehaviour(msg, hal));
    } else {
      block();
    }
  }
}
