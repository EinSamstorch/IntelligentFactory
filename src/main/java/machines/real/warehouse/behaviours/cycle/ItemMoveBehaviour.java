package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.commons.request.WarehouseItemMoveRequest;
import machines.real.warehouse.WarehouseHal;

/**
 * 工件移动管理.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemMoveBehaviour extends CyclicBehaviour {

  private WarehouseHal hal;
  private Integer posOut;

  public ItemMoveBehaviour() {
    super();
  }

  public void setHal(WarehouseHal hal) {
    this.hal = hal;
  }

  public void setPosOut(Integer posOut) {
    this.posOut = posOut;
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
    WarehouseItemMoveRequest request = null;
    try {
      request = (WarehouseItemMoveRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
    }
    if (request != null) {
      Integer itemPosition = request.getItemPosition();
      // 通知AGV取货
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
      if (hal.moveItem(itemPosition, posOut)) {
        LoggerUtil.hal.info(String.format("Succeed! Export item from %d", itemPosition));
      } else {
        LoggerUtil.hal.error(String.format("Failed! Export item from %d", itemPosition));
        myAgent.clean(false);
      }
    } else {
      LoggerUtil.hal.error("Request NPE Error.");
    }
  }
}
