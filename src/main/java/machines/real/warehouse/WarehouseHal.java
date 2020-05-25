package machines.real.warehouse;

/**
 * 仓库hal.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface WarehouseHal {

  String CMD_MOVE_ITEM = "move_item";
  String FIELD_FROM = "from";
  String FIELD_TO = "to";
  String CMD_EXPORT_ITEM = "export_item";
  String CMD_IMPORT_ITEM = "import_item";
}
