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

  /**
   * 预估时间动作.
   *
   * @param wpInfo 工件信息
   */
  public EvaluateAction(WorkpieceStatus wpInfo) {
    this.wpInfo = wpInfo;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
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
