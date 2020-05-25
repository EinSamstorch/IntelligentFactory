package machines.real.commons.actions;

import commons.order.WorkpieceStatus;
import machines.real.commons.hal.MachineHal;

/**
 * 预估加工时间动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class EvaluateAction extends AbstractMachineAction {

  private WorkpieceStatus wpInfo;

  public EvaluateAction(WorkpieceStatus wpInfo) {
    this.wpInfo = wpInfo;
  }

  @Override
  public String getCmd() {
    return MachineHal.CMD_EVALUATE;
  }

  @Override
  public Object getExtra() {
    return wpInfo;
  }
}
