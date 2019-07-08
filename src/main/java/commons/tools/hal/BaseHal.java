package commons.tools.hal;

import com.alibaba.fastjson.JSONObject;
import commons.tools.JsonTool;
import commons.tools.hal.socket.SocketListener;
import commons.tools.hal.socket.SocketMessage;
import commons.tools.hal.socket.SocketSender;

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
        }
    }


    protected boolean executeCmd(String cmd, Object extra) {
        int taskNo = sendMessageToMachine(cmd, extra);

        JSONObject cmdResponse = getCmdResponse(taskNo);
        if (!Objects.equals(SocketMessage.RESULT_SUCCESS,
                cmdResponse.getString(SocketMessage.FIELD_CMD_RESULT))) {
            return false;
        }

        JSONObject actionResponse = getActionResponse(taskNo);
        if(!Objects.equals(SocketMessage.RESULT_SUCCESS,
                actionResponse.getString(SocketMessage.FIELD_ACTION_RESULT))) {
            return false;
        }

        return true;
    }
}
