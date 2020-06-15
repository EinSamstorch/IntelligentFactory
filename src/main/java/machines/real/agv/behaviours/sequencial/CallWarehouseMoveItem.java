package machines.real.agv.behaviours.sequencial;

import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import machines.real.commons.request.WarehouseItemMoveRequest;

/**
 * 请求仓库移动货物.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class CallWarehouseMoveItem extends SequentialBehaviour {

  /**
   * 请求仓库移动货物.
   *
   * @param request   移动货物请求
   * @param warehouse AID用于联系agent
   */
  public CallWarehouseMoveItem(WarehouseItemMoveRequest request, AID warehouse) {
    String conversationId = "CALL_FOR_WH_" + new Random().nextInt();
    addSubBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        ACLMessage msg;
        try {
          msg = DfUtils.createRequestMsg(request);
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        msg.addReceiver(warehouse);
        msg.setConversationId(conversationId);
        msg.setLanguage(WarehouseItemMoveRequest.LANGUAGE);
        myAgent.send(msg);
      }
    });
    addSubBehaviour(new SimpleBehaviour() {
      private boolean done = false;

      @Override
      public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
          if (receive.getPerformative() == ACLMessage.INFORM) {
            done = true;
            LoggerUtil.agent.info((request.isIn() ? "Import" : "Export")
                + ": warehouse move item from/to: "
                + request.getItemPosition());
          } else {
            LoggerUtil.agent.error("Response code error, code: " + receive.getPerformative());
          }
        } else {
          block();
        }
      }

      @Override
      public boolean done() {
        return done;
      }
    });
  }
}
