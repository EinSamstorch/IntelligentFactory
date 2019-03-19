package CommonTools.MySocket.Server;

import java.util.Hashtable;
import java.util.Map;


/**
 * Socket管理.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class ChatManager {
    private static final ChatManager cm = new ChatManager();
    Map<String, ChatSocket> address = new Hashtable<>();

    /*
     * 单例化
     * 因为一个聊天系统只有一个聊天管理，所以需进行单例化private
     */
    private ChatManager() {
    }

    public static ChatManager GetChatManager() {
        return cm;
    }

    /**
     * 注册设备类型
     *
     * @param type 设备类型
     * @param cs   通信socket实例
     */
    public void registerType(String type, ChatSocket cs) {
        address.put(type, cs);
    }

    /**
     * 移除已注册类型
     *
     * @param type 设备类型
     */
    public void deregisterType(String type) {
        address.remove(type);
    }


    /**
     * 转发消息
     *
     * @param dest 消息目的地
     * @param msg  消息本体
     */
    public void Send(String dest, String msg) {
        ChatSocket cs = address.get(dest);
        if (cs != null) {
            cs.send(msg);
        }
    }
}
