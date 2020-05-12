package machines.real.agv;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.hal.BaseHalImpl;

/**
 * 多AGV HAL的实现类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MultiAgvHalImpl extends BaseHalImpl implements MultiAgvHal {

  private static final String CMD_EXPORT_ITEM = "export_item";
  private static final String CMD_IMPORT_ITEM = "import_item";
  private static final String CMD_GET_POSTION = "get_position";
  private static final String CMD_MOVE = "move";
  private static final String FIELD_DESTINATION = "destination";

  public MultiAgvHalImpl() {
    super();
  }

  public MultiAgvHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean move(String path) {
    JSONObject extra = new JSONObject();
    extra.put(FIELD_DESTINATION, path);
    return executeCmd(CMD_MOVE, extra);
  }

  @Override
  public boolean exportItem() {
    return executeCmd(CMD_EXPORT_ITEM);
  }

  @Override
  public boolean importItem() {
    return executeCmd(CMD_IMPORT_ITEM);
  }

  @Override
  public int getPosition() {
    while (!executeCmd(CMD_GET_POSTION)) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return Integer.parseInt(getExtraInfo());
  }
}
