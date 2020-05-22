package machines.real.commons.behaviours.simple;

import commons.tools.LoggerUtil;
import machines.real.commons.actions.ProcessAction;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;

/**
 * 加工工件行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ProcessItemBehaviour2 extends ActionExecutor {

  private Buffer buffer;

  public ProcessItemBehaviour2(MiddleHal hal, Buffer buffer) {
    super(new ProcessAction(buffer.getWpInfo()), hal);
    this.buffer = buffer;
  }

  @Override
  public void action() {
    buffer.startProcess();
    LoggerUtil.agent.info("Processing buffer: " + buffer.getIndex());
    super.action();
    buffer.getWpInfo().setNextProcessStep();
  }
}
