package machines.real.vision;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.hal.BaseHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionHal extends BaseHal {
    private static final String CMD_CHECK = "check";

    public VisionHal() {
        super();
    }
    public VisionHal(int port) {
        super(port);
    }

    public String check(String goodsid) {
        JSONObject extra = new JSONObject();
        extra.put("goodsid", goodsid);
        if(executeCmd(CMD_CHECK, extra)) {
            return ((JSONObject)getExtraInfo()).toJSONString();
        }
        return null;
    }
}
