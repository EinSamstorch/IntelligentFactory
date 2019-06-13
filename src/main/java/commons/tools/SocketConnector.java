package commons.tools;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 连接socket server.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SocketConnector {
    private Socket socket;
    private int port;

    private static final String host = "127.0.0.1";
    private static final String FIELD_TO = "to";
    private static final String FIELD_MESSAGE = "message";
    private static final String FIELD_TYPE = "type";
    private static final String TYPE_AGENT = "agent";
    private static final String TYPE_MACHINE = "machine";
    private static final String TYPE_RFID = "rfid";

    public SocketConnector() {
        this(5656);
    }
    public SocketConnector(int port) {
        this.port = port;
    }

    private void connect() {
        try {
            socket = new Socket(host, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册成为agent
     */
    private void registerType() {
        JSONObject type = new JSONObject();
        type.put(FIELD_TYPE, TYPE_AGENT);
        send(type.toJSONString());
    }

    /**
     * 初始化操作 连接socket server, 注册成为agent
     */
    public void init() {
        this.connect();
        this.registerType();
    }

    protected void sendMessageToMachine(Object msg) {
        this.sendMessage(TYPE_MACHINE, msg);
    }

    protected void sendMessageToRfid(Object msg) {
        this.sendMessage(TYPE_RFID, msg);
    }

    private synchronized void sendMessage(String to, Object msg) {
        JSONObject msgStruct = new JSONObject();
        msgStruct.put(FIELD_TO, to);
        msgStruct.put(FIELD_MESSAGE, msg);
        send(msgStruct.toJSONString());
    }

    protected synchronized String receiveMessage() {
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

    private void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
    }
}
