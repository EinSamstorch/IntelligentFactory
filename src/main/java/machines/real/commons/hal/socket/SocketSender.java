package machines.real.commons.hal.socket;

import com.alibaba.fastjson.JSONObject;
import commons.tools.LoggerUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public SocketSender() {
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
            // 消息末端增加 \n，以表示 一条消息结束 且字符均为小写
            String format = words.toLowerCase() + "\n";
            socket.getOutputStream().write(format.getBytes(StandardCharsets.UTF_8));
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

    public synchronized String sendCmdToServer(JSONObject msg) {
        send(msg.toJSONString());
        return receiveMessage();
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
}
