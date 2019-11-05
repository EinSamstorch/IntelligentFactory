package machines.real.arm;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.hal.BaseHalImpl;

/**
 * hal for arm robot.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmHalImpl extends BaseHalImpl implements ArmHal {

  private static final String CMD_MOVE_ITEM = "move_item";
  private static final String FIELD_FROM = "from";
  private static final String FIELD_TO = "to";
  private static final String FIELD_GOODSID = "goodsId";
  private static final String FIELD_STEP = "step";

  public ArmHalImpl() {
    super();
  }

  public ArmHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean moveItem(String from, String to, String goodsid, int step) {
    JSONObject extra = new JSONObject();
    extra.put(FIELD_FROM, from);
    extra.put(FIELD_TO, to);
    extra.put(FIELD_GOODSID, goodsid);
    if (step != 0) {
      extra.put(FIELD_STEP, step);
    }
    return executeCmd(CMD_MOVE_ITEM, extra);
  }

  @Override
  public boolean moveItem(String from, String to, String goodsid) {
    return moveItem(from, to, goodsid, 0);
  }
}
