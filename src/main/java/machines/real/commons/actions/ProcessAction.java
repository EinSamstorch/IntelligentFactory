package machines.real.commons.actions;

import commons.order.WorkpieceStatus;
import machines.real.commons.hal.MachineHal2;

/**
 * 加工工件动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ProcessAction extends AbstractMachineAction {

  private WorkpieceStatus wpInfo;

  public ProcessAction(WorkpieceStatus wpInfo) {
    this.wpInfo = wpInfo;
  }

  @Override
  public String getCmd() {
    return MachineHal2.CMD_PROCESS;
  }

  @Override
  public Object getExtra() {
    return wpInfo;
  }
}
