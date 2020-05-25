package machines.real.warehouse.actions;

import machines.real.commons.actions.AbstractMachineAction;
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

  public ImExportItemAction(boolean importMode) {
    this.importMode = importMode;
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
