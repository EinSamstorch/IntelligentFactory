package commons.tools.hal;

import com.alibaba.fastjson.JSONObject;
import commons.tools.JsonTool;
import commons.tools.LoggerUtil;
import commons.tools.SocketConnector;

import java.util.Objects;

/**
 * 封装HAL底层通信.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BaseHal extends SocketConnector {
    private static int taskNo = 0;
    private static final String FIELD_TASK_NO = "task_no";
    private static final String FIELD_CMD = "cmd";
    private static final String FIELD_EXTRA = "extra";

    private static final String FIELD_CMD_RESULT = "cmd_result";
    private static final String FIELD_ACTION_RESULT = "action_result";

    private static final String RESULT_SUCCESS = "success";
    private static final String RESULT_FAILED = "failed";

    private Object responseExtra;

    protected Object popResponseExtra() {
        Object res = responseExtra;
        responseExtra = null;
        return res;
    }

    protected void setResponseExtra(Object responseExtra) {
        this.responseExtra = responseExtra;
    }

    public BaseHal() {
        super();
    }

    public BaseHal(int port) {
        super(port);
    }

    protected void sendMessageToMachine(String cmd, Object extra) {
        JSONObject message = new JSONObject();
        message.put(FIELD_TASK_NO, ++taskNo);
        message.put(FIELD_CMD, cmd);
        message.put(FIELD_EXTRA, extra);

        this.sendMessageToMachine(message);
    }

    /**
     * 检查返回消息 对命令或动作是否执行成功
     * @param response 返回消息
     * @return true 成功, false 失败
     */
    protected boolean checkResponse(String response) {
        JSONObject message;
        try {
            // 尝试json解析
            message = JsonTool.parseObject(response);
        } catch (IllegalArgumentException e) {
            // 解析失败 则返回 false
            LoggerUtil.agent.error(e.getMessage());
            return false;
        }
        // 验证消息结构完整性
        boolean msgComplete = message.containsKey(FIELD_TASK_NO)
                && message.containsKey(FIELD_EXTRA)
                && (message.containsKey(FIELD_CMD_RESULT)
                    || message.containsKey(FIELD_ACTION_RESULT));
        if(!msgComplete) {
            // 消息结构缺失则返回false
            LoggerUtil.agent.warn("Message incomplete");
            return false;
        }
        // 提取 extra信息 并保存
        setResponseExtra(message.get(FIELD_EXTRA));
        // 检查 cmd_result字段 或 action_result字段 是否成功
        boolean resultSuccess = Objects.equals(RESULT_SUCCESS, message.getString(FIELD_CMD_RESULT))
                || Objects.equals(RESULT_SUCCESS, message.getString(FIELD_ACTION_RESULT));
        if(resultSuccess) {
            // 命令解析或动作执行成功 返回true
            return true;
        } else {
            // 失败返回false
            LoggerUtil.agent.warn(message.getString(FIELD_EXTRA));
            return false;
        }
    }

    protected boolean executeCmd(String cmd, Object extra) {
        sendMessageToMachine(cmd, extra);

        String response = receiveMessage();
        if(!checkResponse(response)) {
            // 命令解析失败
            return false;
        }
        response = receiveMessage();
        if(!checkResponse(response)){
            // 动作执行失败
            return false;
        }
        return true;
    }
}
