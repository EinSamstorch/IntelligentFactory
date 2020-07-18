package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.WarehouseItemMoveRequest;
import machines.real.warehouse.actions.MoveItemAction;

/**
 * 工件移动管理.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemMoveBehaviour extends CyclicBehaviour {

  private MiddleHal hal;
  private int posOut;
  private int posIn;

  public ItemMoveBehaviour() {
    super();
  }

  public void setHal(MiddleHal hal) {
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
    WarehouseItemMoveRequest request;
    try {
      request = (WarehouseItemMoveRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      LoggerUtil.agent
          .warn("Deserialization Error in WarehouseItemMoveRequest. " + e.getLocalizedMessage());
      return;
    }
    int itemPosition = request.getItemPosition();
    int from = request.isIn() ? posIn : itemPosition;
    int to = request.isIn() ? itemPosition : posOut;
    MoveItemAction action = new MoveItemAction(from, to, request);
    while (!hal.executeAction(action)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    ACLMessage reply = msg.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    myAgent.send(reply);
  }
}
