package machines.real.arm;

import machines.real.commons.hal.MiddleHal;

/**
 * 定义机械手hal命令.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface ArmHal extends MiddleHal {

  String CMD_MOVE_ITEM = "move_item";
  String FIELD_FROM = "from";
  String FIELD_TO = "to";
  String FIELD_GOODSID = "goodsId";
  String FIELD_STEP = "step";
}
