package machines.virtual.recorder.behaviours;

import commons.order.OrderTrace;
import commons.order.WorkpieceStatus;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.virtual.recorder.OrderTraceMysql;

/**
 * @author <a href="mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @date: 2020/10/19 16:55
 */
public class MessageListener extends CyclicBehaviour {

  private MessageTemplate mt =
      MessageTemplate.and(
          MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
          MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_REQUEST));

  private OrderTraceMysql db;

  public void setDb(OrderTraceMysql db) {
    this.db = db;
  }

  @Override
  public void action() {
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      block();
      return;
    }

    WorkpieceStatus wpInfo = null;
    try {
      wpInfo = (WorkpieceStatus) receive.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
    }

    String orderId = wpInfo.getOrderId();
    String workpieceId = wpInfo.getWorkpieceId();
    int bufferId = wpInfo.getBufferPos();
    String owner = wpInfo.getPreOwnerId();
    String extra = "";

    OrderTrace orderTrace = new OrderTrace(orderId, workpieceId, bufferId, owner, extra);
    // 执行成功，回复inform确认
    if (db.updateInfo(orderTrace)) {
      ACLMessage reply = receive.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
    }
  }
}
