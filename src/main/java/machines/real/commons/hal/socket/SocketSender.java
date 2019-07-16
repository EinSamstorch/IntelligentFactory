package machines.real.commons.hal.socket;

import com.alibaba.fastjson.JSONObject;
import commons.tools.LoggerUtil;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 消息发送者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SocketSender {

    private Socket socket;
    private int port;



    public SocketSender(int port) {
        this.port = port;
        connect();
    }

    public SocketSender(){
        this(5656);
    }

    public void connect() {
        try {
            socket = new Socket(SocketMessage.HOST, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
    }

    public void sendMessageToMachine(Object msg) {
        this.sendMessage(SocketMessage.TYPE_MACHINE, msg);
    }

    public void sendMessageToRfid(Object msg) {
        this.sendMessage(SocketMessage.TYPE_RFID, msg);
    }

    private synchronized void sendMessage(String to, Object msg) {
        JSONObject msgStruct = new JSONObject();
        msgStruct.put(SocketMessage.FIELD_TO, to);
        msgStruct.put(SocketMessage.FIELD_MESSAGE, msg);
        send(msgStruct.toJSONString());
    }
}
