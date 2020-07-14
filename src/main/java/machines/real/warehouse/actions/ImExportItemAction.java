package machines.real.warehouse.actions;

import machines.real.commons.actions.AbstractMachineAction;
import machines.real.commons.request.WarehouseConveyorRequest;
import machines.real.warehouse.WarehouseHal;

/**
 * 仓库传送带进出货动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ImExportItemAction extends AbstractMachineAction {

  private boolean importMode;

  /**
   * 仓库传送带进出货控制.
   *
   * @param request 进出货请求
   */
  public ImExportItemAction(WarehouseConveyorRequest request) {
    this.importMode = request.isImportMode();
    this.orderId = request.getOrderId();
    this.workpieceId = request.getWorkpieceId();
  }

  @Override
  public String getCmd() {
    return importMode
        ? WarehouseHal.CMD_IMPORT_ITEM
        : WarehouseHal.CMD_EXPORT_ITEM;
  }

  @Override
  public Object getExtra() {
    return "";
  }
}
