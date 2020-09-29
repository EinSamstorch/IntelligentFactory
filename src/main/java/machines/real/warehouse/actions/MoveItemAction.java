package machines.real.warehouse.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.actions.AbstractMachineAction;
import machines.real.commons.request.WarehouseConveyorRequest;
import machines.real.commons.request.WarehouseItemMoveRequest;
import machines.real.warehouse.WarehouseHal;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Or;

/**
 * 仓库移动物品动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class MoveItemAction extends AbstractMachineAction {

  private Object extra;

  /**
   * 仓库移动物品动作.
   *
   * @param from 起点
   * @param to 终点
   */
  public MoveItemAction(int from, int to, WarehouseItemMoveRequest request) {
    JSONObject extra = new JSONObject();
    extra.put(WarehouseHal.FIELD_FROM, from);
    extra.put(WarehouseHal.FIELD_TO, to);
    this.extra = extra;
    this.orderId = request.getOrderId();
    this.workpieceId = request.getWorkpieceId();
  }

  @Override
  public String getCmd() {
    return WarehouseHal.CMD_MOVE_ITEM;
  }

  @Override
  public Object getExtra() {
    return extra;
  }
}
