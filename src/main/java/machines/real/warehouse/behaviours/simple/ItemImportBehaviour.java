package machines.real.warehouse.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import machines.real.commons.request.WarehouseItemMoveRequest;
import machines.real.warehouse.WarehouseAgent;
import machines.real.warehouse.WarehouseHal;

/**
 * 仓库入库请求一次性动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemImportBehaviour extends OneShotBehaviour {

  private WarehouseHal hal;
  private Integer posIn;
  private WarehouseItemMoveRequest request;

  /**
   * 仓库入库请求.
   *
   * @param whAgent 仓库agent
   * @param request 入库请求
   */
  public ItemImportBehaviour(WarehouseAgent whAgent, WarehouseItemMoveRequest request) {
    super(whAgent);
    hal = whAgent.getHal();
    this.posIn = whAgent.getPosIn();
    this.request = request;
  }

  @Override
  public void action() {
    if (request != null) {
      Integer itemPosition = request.getItemPosition();
      if (hal.moveItem(posIn, itemPosition)) {
        LoggerUtil.hal.info(String.format("Succeed! Import item to %d", itemPosition));
      } else {
        LoggerUtil.hal.info(String.format("Failed! Import item to %d", itemPosition));
      }
    }
  }
}
