package machines.real.agv.behaviours.simple;

import commons.NotifyFinish;
import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import machines.real.commons.request.WarehouseRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CallForWarehouse extends SimpleBehaviour {

  private boolean init = true;
  private boolean isDone = false;
  private String conversationId;
  private WarehouseRequest request;
  private volatile NotifyFinish notifyFinish;

  /**
   * 请求仓库.
   * @param a 请求方agent
   * @param request 仓库搬运请求
   * @param notifyFinish 信号量通知是否完成
   */
  public CallForWarehouse(Agent a, WarehouseRequest request, NotifyFinish notifyFinish) {
    super(a);
    this.request = request;
    this.notifyFinish = notifyFinish;
    conversationId = String.format("CALL_FOR_WH_%d", new Random().nextInt());

  }

  @Override
  public void action() {
    if (init) {
      // 发送出货请求
      ACLMessage msg = null;
      try {
        msg = DfUtils.createRequestMsg(request);
        DfUtils.searchDf(myAgent, msg, DfServiceType.WAREHOUSE);
        // 发送运输请求
        msg.setConversationId(conversationId);
        myAgent.send(msg);
      } catch (Exception e) {
        e.printStackTrace();
      }
      init = false;
      LoggerUtil.hal.debug("Call for warehouse.");
    } else {
      MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
      ACLMessage receive = myAgent.receive(mt);
      if (receive != null) {
        if (receive.getPerformative() == ACLMessage.INFORM) {
          // 已到达出货口
          isDone = true;
          notifyFinish.setDone(true);
          LoggerUtil.agent.info("Workpiece ready to export");
        } else {
          LoggerUtil.agent.error("Performative error.");
        }
      } else {
        block();
      }
    }
  }

  @Override
  public boolean done() {
    return isDone;
  }
}
