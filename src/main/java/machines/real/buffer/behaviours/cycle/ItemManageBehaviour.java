package machines.real.buffer.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.buffer.BufferHal;
import machines.real.buffer.behaviours.simple.InExportItemBehaviour;
import machines.real.commons.request.BufferRequest;

public class ItemManageBehaviour extends CyclicBehaviour {

  private BufferHal hal;

  public void setHal(BufferHal hal) {
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
      BufferRequest request = null;
      try {
        request = (BufferRequest) msg.getContentObject();
      } catch (UnreadableException e) {
        e.printStackTrace();
      }
      if (request == null) {
        LoggerUtil.hal.error("Buffer request NPE error.");
        return;
      }
      myAgent.addBehaviour(new InExportItemBehaviour(request, hal, msg));
    } else {
      block();
    }
  }
}
