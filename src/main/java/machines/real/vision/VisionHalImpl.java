package machines.real.vision;

import com.alibaba.fastjson.JSONObject;
import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import machines.real.commons.hal.MachineHalImpl;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionHalImpl extends MachineHalImpl {

  private static final String CMD_CHECK = "check";
  private String checkValue;

  public VisionHalImpl() {
    super();
  }

  public VisionHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean process(WorkpieceStatus wpInfo) {
    String goodsId = wpInfo.getGoodsId();
    JSONObject extra = new JSONObject();
    extra.put("goodsId", goodsId);
    if (executeCmd(CMD_CHECK, extra)) {
      checkValue = getExtraInfo();
      LoggerUtil.hal.info(String.format("OrderId: %s, WorkpieceId: %s, GoodsId: %s. Values: %s",
          wpInfo.getOrderId(),
          wpInfo.getWorkpieceId(),
          wpInfo.getGoodsId(),
          checkValue));
      return true;
    }
    checkValue = null;
    return false;
  }

  public Object getCheckValue() {
    return checkValue;
  }
}
