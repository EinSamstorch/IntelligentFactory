package CommonTools.SocketServer;

import CommonTools.LoggerUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * 处理单个socket连接.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class ChatSocket extends Thread {
    private Socket socket;
    private String selfType;

    public ChatSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * 向客户端发送消息
     *
     * @param str 消息字符串
     */
    public void send(String str) {
        try {
            // 消息末端增加 \n，以表示 一条消息结束
            str += "\n";
            socket.getOutputStream().write(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 服务该socket连接
     * 1. 注册类型：向ChatManager 注册该socket客户端设备类型
     * 2. 转发消息：向ChatManager 告知转发消息给谁
     * 3. 忽略其他字符串
     */
    @Override
    public void run() {
        while (true) {
            if (socket.isClosed()) {
                // 如果连接断开，则从登记列表中删除该连接
                ChatManager.GetChatManager().deregisterType(selfType);
                break;
            }
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    LoggerUtil.commonTools.debug(line);
                    // 尝试json解析字符串，若异常，则跳过
                    JSONObject jo = (JSONObject) JSONObject.parse(line);
                    // 解析 是否 为 注册类型 命令
                    String type = jo.getString("type");
                    if (type != null) {
                        selfType = type;
                        ChatManager.GetChatManager().registerType(selfType, this);
                    }
                    // 解析 是否 为 转发消息 命令
                    String dest = jo.getString("to");
                    if (dest != null) {
                        String msg = jo.getString("message");
                        ChatManager.GetChatManager().Send(dest, msg);
                    }
                }
            } catch (JSONException e) {
                // do nothing
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
