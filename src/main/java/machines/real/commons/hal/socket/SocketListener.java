package machines.real.commons.hal.socket;

import com.alibaba.fastjson.JSONObject;
import commons.tools.JsonTool;
import commons.tools.LoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听socket返回消息并存入自身Map<taskNo, resultStr>中.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SocketListener extends Thread {
    private static final String HAL_ONLINE = "online";
    private static final String HAL_OFFLINE = "offline";
    private Socket socket;
    private int port;
    private Map<Integer, JSONObject> cmdResponseMap = new HashMap<>();
    private Map<Integer, JSONObject> actionResponseMap = new HashMap<>();
    private boolean halOnline = false;

    public SocketListener(int port) {
        this.port = port;
    }

    public SocketListener() {
        this(5656);
    }

    public boolean isHalOnline() {
        return halOnline;
    }

    /**
     * 1. 初始化
     * 2. 循环监听socket server返回内容
     * 3. 解析内容
     * 4. 根据内容类型和taskNo放入Map
     */
    @Override
    public void run() {
        this.init();
        while (true) {
            String response = receiveMessage();
            if (isActionResponse(response)) {
                JSONObject message = JsonTool.parse(response);
                String result = message.getString(SocketMessage.FIELD_RESULT);
                String info = message.getString(SocketMessage.FIELD_INFO);
                if (!SocketMessage.RESULT_SUCCESS.equals(result)) {
                    LoggerUtil.hal.error(info);
                }
            } else if (checkMsgComplete(response)) {
                JSONObject message = JsonTool.parse(response);
                int taskNo = message.getIntValue(SocketMessage.FIELD_TASK_NO);
                String type = parseMsgType(response);
                switch (type) {
                    case SocketMessage.FIELD_CMD_RESULT:
                        cmdResponseMap.put(taskNo, message);
                        break;
                    case SocketMessage.FIELD_ACTION_RESULT:
                        actionResponseMap.put(taskNo, message);
                        break;
                    default:
                }
            } else if (checkActionCmdResponse(response)) {
                JSONObject message = JsonTool.parse(response);
                String result = message.getString(SocketMessage.FIELD_RESULT);
                String info = message.getString(SocketMessage.FIELD_INFO);
                if (SocketMessage.RESULT_SUCCESS.equals(result)) {
                    if (HAL_ONLINE.equals(info)) {
                        halOnline = true;
                    } else if (HAL_OFFLINE.equals(info)) {
                        halOnline = false;
                    }
                }
            }
        }
    }

    /**
     * 检查是否属于 action: xxx, value:xxx 系列命令的回复。
     *
     * @return true, 内容包含 result: success/failed, info: xxx. 否则false
     */
    private boolean checkActionCmdResponse(String msg) {
        if (msg == null) {
            return false;
        }
        JSONObject message;
        try {
            message = JsonTool.parse(msg);
        } catch (IllegalArgumentException e) {
            LoggerUtil.agent.error(e.getMessage());
            return false;
        }
        return message.containsKey(SocketMessage.FIELD_RESULT)
                && message.containsKey(SocketMessage.FIELD_INFO);
    }

    /**
     * 检查输入的字符串是否是 动作语句的回复
     *
     * @param msg 待检查字符串
     * @return 包含 result和 info字段的json语句，返回true, 否则 false
     */
    private boolean isActionResponse(String msg) {
        if (msg == null) {
            return false;
        }

        JSONObject message;
        try {
            // 尝试json解析
            message = JsonTool.parse(msg);
        } catch (IllegalArgumentException e) {
            // 解析失败 则返回 false
            LoggerUtil.agent.error(e.getMessage());
            return false;
        }

        return message.containsKey(SocketMessage.FIELD_RESULT)
                && message.containsKey(SocketMessage.FIELD_INFO);

    }

    /**
     * 检查消息完整性
     *
     * @param response socket server返回的消息
     * @return 是否完整
     */
    private boolean checkMsgComplete(String response) {
        if (response == null) {
            return false;
        }

        JSONObject message;
        try {
            // 尝试json解析
            message = JsonTool.parse(response);
        } catch (IllegalArgumentException e) {
            // 解析失败 则返回 false
            LoggerUtil.agent.error(e.getMessage());
            return false;
        }

        // 验证消息结构完整性
        boolean msgComplete = message.containsKey(SocketMessage.FIELD_TASK_NO)
                && message.containsKey(SocketMessage.FIELD_EXTRA)
                && (message.containsKey(SocketMessage.FIELD_CMD_RESULT)
                || message.containsKey(SocketMessage.FIELD_ACTION_RESULT));
        if (!msgComplete) {
            // 消息结构缺失则返回false
            LoggerUtil.agent.warn("Message incomplete");
            return false;
        }

        return true;
    }

    private String parseMsgType(String response) {
        JSONObject message = JsonTool.parse(response);
        if (message.containsKey(SocketMessage.FIELD_CMD_RESULT)) {
            return SocketMessage.FIELD_CMD_RESULT;
        }
        if (message.containsKey(SocketMessage.FIELD_ACTION_RESULT)) {
            return SocketMessage.FIELD_ACTION_RESULT;
        }
        return "UNKNOWN";
    }

    /**
     * 连接SocketServer 并注册 type:agent
     */
    private void init() {
        this.connect();
        this.registerType();
    }

    /**
     * 连接socketServer
     */
    private void connect() {
        try {
            socket = new Socket(SocketMessage.HOST, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册设备类型 action:register, value: agent
     */
    private void registerType() {
        JSONObject msg = new JSONObject();
        msg.put(SocketMessage.FIELD_ACTION, SocketMessage.ACTION_REGISTER);
        msg.put(SocketMessage.FIELD_VALUE, SocketMessage.TYPE_AGENT);
        send(msg.toJSONString());
    }

    /**
     * 向socketserver发送字符串
     *
     * @param words 待发送字符串
     */
    private void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
    }

    /**
     * 接收socketserver返回的字符串
     *
     * @return 接收到的字符串
     */
    private String receiveMessage() {
        String line = null;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8));
            line = br.readLine();
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage(), e);
        }
        return line;
    }

    public Map<Integer, JSONObject> getCmdResponseMap() {
        return cmdResponseMap;
    }

    public Map<Integer, JSONObject> getActionResponseMap() {
        return actionResponseMap;
    }
}
