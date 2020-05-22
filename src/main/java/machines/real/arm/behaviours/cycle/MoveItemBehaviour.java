package machines.real.arm.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.arm.actions.MoveItemAction;
import machines.real.commons.ActionExecutor;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.ArmRequest;

/**
 * executor move item request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public final class MoveItemBehaviour extends CyclicBehaviour {

  private static final int STATE_FREE = 0;
  private static final int STATE_BUSY = 1;
  /**
   * 匹配 FIPA_REQUEST 和 ACLMessage.REQUEST
   */
  private final MessageTemplate mt = MessageTemplate.and(
      MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
      MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
  private MiddleHal hal;
  private int state = STATE_FREE;
  private String language = "";
  private int oldStep = 0;
  private final ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();


  public MoveItemBehaviour() {
    super();
  }

  public void setHal(MiddleHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    // 根据当前状态 获取消息匹配模板
    final MessageTemplate mt = state == STATE_FREE ? this.mt :
        MessageTemplate.and(this.mt, MessageTemplate.MatchLanguage(language));
    ACLMessage msg = myAgent.receive(mt);
    if (msg == null) {
      block();
      return;
    }
    // 获取搬运任务
    ArmRequest request;
    try {
      request = (ArmRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      LoggerUtil.agent.warn("Deserialization Error in ArmRequest.");
      block();
      return;
    }
    // 检查任务类型
    final int step = request.getStep();
    if (state == STATE_FREE) {
      if (step > 1) {
        LoggerUtil.agent.warn("Illegal step: " + step + " in state free.");
        return;
      }
      if (step == 1) {
        // 进入busy状态，直至获取到step = -1 的任务
        state = STATE_BUSY;
        // 用于busy状态匹配后续任务
        language = request.toString();
        oldStep = step;
      }
    } else {
      if (step == -1) {
        // 结束态
        state = STATE_FREE;
        language = "";
        // 回复请求任务完成
        replySuccess(msg);
        return;
      }

      if (step != oldStep + 1) {
        LoggerUtil.agent.warn("Illegal step: " + step + ", old step: " + oldStep);
      }
      oldStep = step;
    }
    executeTask(request);
    replySuccess(msg);
  }

  private void executeTask(ArmRequest request) {
    Behaviour b = tbf.wrap(new ActionExecutor(new MoveItemAction(request), hal));
    myAgent.addBehaviour(b);
    while (!b.done()) {
      block(3000);
    }
  }

  private void replySuccess(ACLMessage msg) {
    ACLMessage reply = msg.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    myAgent.send(reply);
  }
}
