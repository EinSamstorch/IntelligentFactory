package machines.real.warehouse.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.actions.AbstractMachineAction;
import machines.real.warehouse.WarehouseHal2;

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
   * @param to   终点
   */
  public MoveItemAction(int from, int to) {
    JSONObject extra = new JSONObject();
    extra.put(WarehouseHal2.FIELD_FROM, from);
    extra.put(WarehouseHal2.FIELD_TO, to);
    this.extra = extra;
  }

  @Override
  public String getCmd() {
    return WarehouseHal2.CMD_MOVE_ITEM;
  }

  @Override
  public Object getExtra() {
    return extra;
  }
}
