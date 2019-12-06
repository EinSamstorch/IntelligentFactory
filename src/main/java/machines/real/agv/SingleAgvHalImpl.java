package machines.real.agv;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.hal.BaseHalImpl;

/**
 * Agv对应硬件层.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class SingleAgvHalImpl extends BaseHalImpl implements SingleAgvHal {

  private static final String CMD_MOVE = "move";
  private static final String FIELD_FROM = "from";
  private static final String FIELD_TO = "to";

  public SingleAgvHalImpl() {
    super();
  }

  public SingleAgvHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean move(int from, int to) {
    JSONObject extra = new JSONObject();
    extra.put(FIELD_FROM, from);
    extra.put(FIELD_TO, to);
    return executeCmd(CMD_MOVE, extra);
  }
}
