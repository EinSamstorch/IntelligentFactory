package machines.real.commons.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.hal.MiddleHal;

/**
 * 动作执行者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ActionExecutor extends OneShotBehaviour {

  private ACLMessage requestMsg;
  private MachineAction action;
  private MiddleHal hal;

  /**
   * 执行动作行为.
   *
   * @param action 动作内容
   * @param hal    执行者
   */
  public ActionExecutor(MachineAction action, MiddleHal hal) {
    this(action, hal, null);
  }

  /**
   * 执行动作行为.
   *
   * @param action     动作内容
   * @param hal        执行者
   * @param requestMsg 任务来源,供回复消息使用
   */
  public ActionExecutor(MachineAction action, MiddleHal hal, ACLMessage requestMsg) {
    this.requestMsg = requestMsg;
    this.action = action;
    this.hal = hal;
  }

  @Override
  public void action() {
    LoggerUtil.hal.info("Action: " + action.getCmd() + ", extra: " + action.getExtra().toString());
    while (!hal.executeAction(action)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    LoggerUtil.hal.debug("Action done!");
    // 储存hal 返回消息
    action.setResultExtra(hal.getExtra());
    if (requestMsg != null) {
      ACLMessage reply = requestMsg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
    }
  }
}
