package machines.real.agv.actions;

import commons.order.WorkpieceStatus;
import machines.real.agv.MultiAgvHal;
import machines.real.commons.actions.AbstractMachineAction;

/**
 * Agv进出货物.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class InExportAction extends AbstractMachineAction {

  private boolean in;

  /**
   * agv进出货物行为.
   *
   * @param in true，agv进货； false agv出货。
   * @param wpInfo 工件信息
   */
  public InExportAction(boolean in, WorkpieceStatus wpInfo) {
    this.in = in;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  @Override
  public String getCmd() {
    return in ? MultiAgvHal.CMD_IMPORT_ITEM : MultiAgvHal.CMD_EXPORT_ITEM;
  }

  @Override
  public Object getExtra() {
    return "";
  }
}
