package machines.real.arm.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.arm.ArmHal;
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
  private ArmHal hal;
  private int state = STATE_FREE;
  private String language = "";
  private int oldStep = 0;
  private ArmRequest retryRequest;
  private ACLMessage retryMessage;
  private boolean hasFailed = false;

  public MoveItemBehaviour() {
    super();
  }

  public void setHal(ArmHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    if (hasFailed) {
      // 重试任务
      doTask(retryMessage, retryRequest);
      return;
    }

    switch (state) {
      case STATE_FREE:
        inStateFree();
        break;
      case STATE_BUSY:
        inStateBusy();
        break;
      default:
        throw new IllegalArgumentException("Illegal state: " + state);
    }
  }

  /**
   * free状态 执行step=0的任务 或执行step=1的任务 并进入 busy状态.
   */
  private void inStateFree() {
    final ACLMessage msg = receiveMsg(mt);
    if (msg == null) {
      return;
    }
    final ArmRequest request = getRequest(msg);
    final int step = request.getStep();
    if (step > 1) {
      throw new IllegalArgumentException("Illegal step: " + step);
    }
    if (step == 1) {
      // 转换状态 进入busy态, 直到完成 step = -1的任务, 退出busy态
      state = STATE_BUSY;
      language = request.toString();
      oldStep = step;
    }
    doTask(msg, request);
  }

  /**
   * busy状态 执行该组搬运动作 直到该组搬运动作出现endStep，回归free状态.
   */
  private void inStateBusy() {
    final MessageTemplate mt1 = MessageTemplate.and(mt,
        MessageTemplate.MatchLanguage(language));
    final ACLMessage msg = receiveMsg(mt1);
    if (msg == null) {
      return;
    }
    final ArmRequest request = getRequest(msg);
    final int step = request.getStep();
    final int endStep = -1;
    if (step == endStep) {
      state = STATE_FREE;
      language = "";

      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);

      return;
    }
    if (step != oldStep + 1) {
      throw new IllegalArgumentException(String.format("Step: %d, oldStep: %d", step, oldStep));
    }
    oldStep = step;
    doTask(msg, request);
  }

  private void doTask(ACLMessage msg, ArmRequest request) {
    final String from = request.getFrom();
    final String to = request.getTo();
    final String goodsId = request.getGoodsId();
    final int step = request.getStep();
    boolean actionResult = hal.moveItem(from, to, goodsId, step);
    if (actionResult) {
      if (hasFailed) {
        // 执行动作成功,清除失败记录
        retryMessage = null;
        retryRequest = null;
        hasFailed = false;
      }
      LoggerUtil.hal.info(String.format("Move item from %s to %s, goodsid: %s, step: %d.",
          from, to, goodsId, step));
      // 搬运完成通知
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
    } else {
      // 记录失败信息等待重试
      retryRequest = request;
      retryMessage = msg;
      hasFailed = true;
      LoggerUtil.hal.error(
          String.format("Failed! Move item from %s to %s, goodsid: %s, step: %d, extra info:%s.",
              from, to, goodsId, step, hal.getExtraInfo()));
      // 同时休眠5000ms 等待重试
      block(5000);
    }
  }

  /**
   * 接收 搬运请求的 ACLMessage.
   *
   * @param mt 消息模板
   * @return 搬运请求消息, 无则null
   */
  private ACLMessage receiveMsg(MessageTemplate mt) {
    ACLMessage msg = myAgent.receive(mt);
    if (msg == null) {
      block();
      return null;
    }
    return msg;
  }

  /**
   * 获取搬运请求.
   *
   * @param msg 搬运请求消息
   * @return 搬运请求, 不为null
   */
  private ArmRequest getRequest(ACLMessage msg) {
    ArmRequest request = null;
    try {
      request = (ArmRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
    }
    if (request == null) {
      throw new IllegalArgumentException("Request NEP Error.");
    }
    return request;
  }
}
