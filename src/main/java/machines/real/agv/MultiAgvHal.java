package machines.real.agv;

import java.io.Serializable;
import machines.real.agv.actions.MachineAction;
import machines.real.commons.hal.BaseHal;

/**
 * 多AGV系统 agv agent硬件层.
 *
 * <p>分离出多个功能模块.
 *
 * @author junfeng
 */
public interface MultiAgvHal extends BaseHal {

  String CMD_EXPORT_ITEM = "export_item";
  String CMD_IMPORT_ITEM = "import_item";
  String CMD_GET_POSTION = "get_position";
  String CMD_MOVE = "move";
  String FIELD_DESTINATION = "destination";

  /**
   * 执行动作.
   *
   * @param action 具体动作
   * @return 成功与否
   */
  boolean executeAction(MachineAction action);

  /**
   * 获取额外信息.
   *
   * @return 根据协议返回额外信息，若无则返回空字符串
   */
  Serializable getExtra();
}
