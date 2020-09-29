package machines.real.commons.actions;

import commons.order.WorkpieceStatus;
import machines.real.commons.hal.MachineHal;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Or;

/**
 * 加工工件动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ProcessAction extends AbstractMachineAction {

  private WorkpieceStatus wpInfo;

  /**
   * 加工行为
   *
   * @param wpInfo 工件信息
   */
  public ProcessAction(WorkpieceStatus wpInfo) {
    this.wpInfo = wpInfo;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  @Override
  public String getCmd() {
    return MachineHal.CMD_PROCESS;
  }

  @Override
  public Object getExtra() {
    return wpInfo;
  }
}
