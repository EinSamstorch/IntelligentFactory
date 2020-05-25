package machines.real.arm.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.arm.ArmHal;
import machines.real.commons.actions.AbstractMachineAction;
import machines.real.commons.request.ArmRequest;

/**
 * 机械手移动物品.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class MoveItemAction extends AbstractMachineAction {

  private JSONObject extra;

  /**
   * 构造移动物品动作.
   *
   * @param from    起始位置
   * @param to      终点位置
   * @param goodsId 货物种类
   * @param step    工步，车床拥有多个装夹工步， 铣床 检测仪没有该属性，填0
   */
  public MoveItemAction(String from, String to, String goodsId, int step) {
    JSONObject extra = new JSONObject();
    extra.put(ArmHal.FIELD_FROM, from);
    extra.put(ArmHal.FIELD_TO, to);
    extra.put(ArmHal.FIELD_GOODSID, goodsId);
    if (step != 0) {
      extra.put(ArmHal.FIELD_STEP, step);
    }
    this.extra = extra;
  }

  /**
   * 构造移动物品动作.
   *
   * @param from    起始位置
   * @param to      终点位置
   * @param goodsId 货物种类
   */
  public MoveItemAction(String from, String to, String goodsId) {
    this(from, to, goodsId, 0);
  }

  /**
   * 构造移动物品动作.
   *
   * @param request 移动物品请求
   */
  public MoveItemAction(ArmRequest request) {
    this(request.getFrom(),
        request.getTo(),
        request.getGoodsId(),
        request.getStep());
  }

  @Override
  public String getCmd() {
    return ArmHal.CMD_MOVE_ITEM;
  }

  @Override
  public Object getExtra() {
    return extra;
  }
}
