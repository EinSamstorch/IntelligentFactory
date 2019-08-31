package commons.tools;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 解析JSON工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class JsonTool {
    public static JSONObject parse(String msg) throws IllegalArgumentException {
        JSONObject jsonMsg;
        if (null == msg || "".equals(msg) || "{}".equals(msg)) {
            throw new IllegalArgumentException("Empty Message");
        }
        try {
            jsonMsg = JSONObject.parseObject(msg);
        } catch (JSONException e) {
            // 将 stackTrace 写入到 logger中
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionMsg = sw.toString();
            LoggerUtil.agent.warn(exceptionMsg);
            throw new IllegalArgumentException("Not a json.");
        }
        return jsonMsg;
    }
}
