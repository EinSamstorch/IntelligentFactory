package machines.real.agv;

import machines.real.commons.hal.MiddleHal;

/**
 * 多AGV系统 agv agent硬件层.
 *
 * <p>分离出多个功能模块.
 *
 * @author junfeng
 */
public interface MultiAgvHal extends MiddleHal {

  String CMD_EXPORT_ITEM = "export_item";
  String CMD_IMPORT_ITEM = "import_item";
  String CMD_GET_POSITION = "get_position";
  String CMD_MOVE = "move";
}
