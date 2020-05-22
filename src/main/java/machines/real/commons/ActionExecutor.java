package machines.real.commons;

import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
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

  private MachineAction action;
  private MiddleHal hal;

  public ActionExecutor(MachineAction action, MiddleHal hal) {
    this.action = action;
    this.hal = hal;
  }

  @Override
  public void action() {
    LoggerUtil.hal.info("Action: " + action.getCmd() + ", extra: " + action.getExtra().toString());
    while (!hal.executeAction(action)) {
      block(3000);
    }
    LoggerUtil.hal.debug("Action done!");
  }
}
