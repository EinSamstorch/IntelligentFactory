package machines.real.vision;

import com.alibaba.fastjson.JSONObject;
import commons.order.WorkpieceInfo;
import commons.tools.LoggerUtil;
import machines.real.commons.hal.MachineHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionHal extends MachineHal {
    private static final String CMD_CHECK = "check";
    private Object checkValue;

    public VisionHal() {
        super();
    }

    public VisionHal(int port) {
        super(port);
    }

    @Override
    public boolean process(WorkpieceInfo wpInfo) {
        String goodsId = wpInfo.getGoodsId();
        JSONObject extra = new JSONObject();
        extra.put("goodsid", goodsId);
        if (executeCmd(CMD_CHECK, extra)) {
            checkValue = ((JSONObject) getExtraInfo()).toJSONString();
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
