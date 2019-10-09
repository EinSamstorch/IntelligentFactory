package machines.real.commons.hal;

import com.alibaba.fastjson.JSONObject;
import commons.tools.JsonTool;
import machines.real.commons.hal.socket.SocketListener;
import machines.real.commons.hal.socket.SocketMessage;
import machines.real.commons.hal.socket.SocketSender;

import java.util.Map;
import java.util.Objects;

/**
 * 封装HAL底层通信.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BaseHal {
    private static int taskNo = 0;


    private SocketSender sender;
    private SocketListener listener;
    private Object extraInfo;

    public static final String HAL_ONLINE = "online";

    public BaseHal() {
        sender = new SocketSender();
        listener = new SocketListener();

        listener.start();
    }

    public BaseHal(int port) {
        sender = new SocketSender(port);
        listener = new SocketListener(port);

        listener.start();
    }

    /**
     * 向SocketServer发送命令
     *
     * @param cmd   命令
     * @param extra 额外内容
     * @return 任务号
     */
    protected int sendMessageToMachine(String cmd, Object extra) {
        JSONObject message = new JSONObject();
        message.put(SocketMessage.FIELD_TASK_NO, ++taskNo);
        message.put(SocketMessage.FIELD_CMD, cmd);
        message.put(SocketMessage.FIELD_EXTRA, extra);

        sender.sendMessageToMachine(message);
        return taskNo;
    }

    /**
     * 获取命令解析结果
     *
     * @param taskNo 任务号
     * @return 结果
     */
    protected JSONObject getCmdResponse(int taskNo) {
        return getResponse(SocketMessage.FIELD_CMD_RESULT, taskNo);
    }

    protected JSONObject getActionResponse(int taskNo) {
        return getResponse(SocketMessage.FIELD_ACTION_RESULT, taskNo);
    }

    private JSONObject getResponse(String responseType, int taskNo) {
        Map<Integer, JSONObject> m = null;
        switch (responseType) {
            case SocketMessage.FIELD_CMD_RESULT:
                m = listener.getCmdResponseMap();
                break;
            case SocketMessage.FIELD_ACTION_RESULT:
                m = listener.getActionResponseMap();
                break;

            default:
                throw new IllegalArgumentException("UNKOWN RESPONSE TYPE");
        }
        while (true) {
            JSONObject response = m.get(taskNo);
            if (response != null) {
                return response;
            }
            sleep(500);
        }
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    protected boolean executeCmd(String cmd, Object extra) {
        int taskNo = sendMessageToMachine(cmd, extra);

        JSONObject cmdResponse = getCmdResponse(taskNo);
        if (!Objects.equals(SocketMessage.RESULT_SUCCESS,
                cmdResponse.getString(SocketMessage.FIELD_CMD_RESULT))) {
            return false;
        }
        extraInfo = cmdResponse.get(SocketMessage.FIELD_EXTRA);

        JSONObject actionResponse = getActionResponse(taskNo);
        if (!Objects.equals(SocketMessage.RESULT_SUCCESS,
                actionResponse.getString(SocketMessage.FIELD_ACTION_RESULT))) {
            return false;
        }
        extraInfo = actionResponse.get(SocketMessage.FIELD_EXTRA);

        return true;
    }

    public boolean checkHalOnline() {
        JSONObject msg = new JSONObject();
        msg.put(SocketMessage.FIELD_ACTION, SocketMessage.ACTION_CHECK);
        msg.put(SocketMessage.FIELD_VALUE, SocketMessage.TYPE_MACHINE);
        String response = sender.sendCmdToServer(msg);
        JSONObject json = JsonTool.parse(response);
        // 包含 result:success, info: online 返回true
        return json.containsKey(SocketMessage.FIELD_RESULT)
                && json.containsKey(SocketMessage.FIELD_INFO)
                && HAL_ONLINE.equals(json.getString(SocketMessage.FIELD_INFO))
                && SocketMessage.RESULT_SUCCESS.equals(json.getString(SocketMessage.FIELD_RESULT));
    }

    public Object getExtraInfo() {
        return extraInfo;
    }
}
