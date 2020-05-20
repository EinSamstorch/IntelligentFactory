package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
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
  private int posOut;
  private int posIn;

  public ItemMoveBehaviour() {
    super();
  }

  public void setHal(WarehouseHal hal) {
    this.hal = hal;
  }

  public void setPosOut(int posOut) {
    this.posOut = posOut;
  }

  public void setPosIn(int posIn) {
    this.posIn = posIn;
  }

  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchLanguage(WarehouseItemMoveRequest.LANGUAGE),
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
      int itemPosition = request.getItemPosition();
      if (request.isIn()) {
        hal.moveItem(posIn, itemPosition);
        LoggerUtil.agent.info("Import item to " + itemPosition);
      } else {
        hal.moveItem(itemPosition, posOut);
        LoggerUtil.agent.info("Export item to " + itemPosition);
      }
      // 通知AGV取货
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
    } else {
      LoggerUtil.hal.error("Request NPE Error.");
    }
  }
}
