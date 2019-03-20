package CommonTools.MySocket.Client;

import CommonTools.LoggerUtil;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * 用于与socket server建立连接并发送消息.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class SocketClient extends Thread {
    /**
     * 设备类型
     */
    public static final String AGENT = "agent";
    public static final String MACHINE = "machine";
    public static final String RFID = "rfid";
    /**
     * Socket server 监听地址
     */
    private static final String host = "127.0.0.1";
    /**
     * 用于构建 {"type": type_str}, 向 socket server 注册类型
     */
    private JSONObject jo_type = new JSONObject();
    /**
     * Socket Server 默认端口 5656
     */
    private int port;
    /**
     * Socket连接实例
     */
    private Socket socket;


    public SocketClient(String type) {
        jo_type.put("type", type);
        this.port = 5656; // default port
        connect();
    }

    public SocketClient(String type, int port) {
        jo_type.put("type", type);
        this.port = port;
        connect();
    }

    /**
     * 发送命令
     *
     * @param dest 命令目的地
     * @param cmd  命令内容
     * @return 对方回应内容
     */
    public String sendCmd(String dest, String cmd) throws SocketTimeoutException {
        JSONObject jo = new JSONObject();
        jo.put("to", dest);
        jo.put("message", cmd);
        String msg = jo.toJSONString();
        send(msg);
        String result = receive();
        return result;
    }

    /**
     * 接受回应消息，超时20秒
     *
     * @return 回应消息， 失败则返回Null
     */
    private synchronized String receive() throws SocketTimeoutException {
        String line = null;
        int timeout = 20000;
        try {
            socket.setSoTimeout(timeout);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            line = br.readLine();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        } finally {
            try {
                // cancel time out
                socket.setSoTimeout(0);
            } catch (SocketException e) {
                LoggerUtil.commonTools.error(e.getMessage());
                System.exit(2);
            }
        }
        return line;
    }

    /**
     * 发送字符串
     *
     * @param words 待发送字符串
     */
    private synchronized void send(String words) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            words += "\n";
            socket.getOutputStream().write(words.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            LoggerUtil.agent.error(e.getMessage());
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
    }

    /**
     * 连接Socket Server
     */
    private void connect() {
        try {
            LoggerUtil.agent.debug("Connecting to Socket Server");
            socket = new Socket(host, port);
            if (socket.isConnected()) {
                LoggerUtil.agent.debug("Connected Successfully");
                send(jo_type.toJSONString());
            }
        } catch (IOException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
    }


    /**
     * 线程主循环，检查是否连接上Socket Server
     * 若未连接上，则建立连接
     */
    @Override
    public void run() {
        while (true) {
            if (socket == null) {
                connect();
            } else if (socket.isClosed()) {
                connect();
            }
        }
    }
}
