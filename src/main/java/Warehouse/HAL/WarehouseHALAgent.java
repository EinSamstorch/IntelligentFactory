package Warehouse.HAL;

import CommonTools.IniLoader;
import CommonTools.LoggerUtil;
import CommonTools.MySocket.Client.SocketClient;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 仓库硬件适配层代理人，提供统一接口函数。具体硬件操作，通过socket server转发至对应socket client处理。.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseHALAgent {
    private static final int MAX_POS = 70;
    private static final String MOVE_ITEM = "move_item";
    private static final String IMPORT_ITEM = "import_item";
    private static final String EXPORT_ITEM = "export_item";
    private static final String READ_RFID = "read_rfid";
    private static final String WRITE_RFID = "write_rfid";
    private int pos_in;
    private int pos_out;
    private SocketClient sClient;
    private int taskCnt = 0;
    private Lock lock = new ReentrantLock();


    private WarehouseHALAgent() {
        Map<String, String> settings = IniLoader.load(IniLoader.SECTION_WAREHOUSE);
        pos_in = Integer.parseInt(settings.get("pos_in"));
        pos_out = Integer.parseInt(settings.get("pos_out"));

        sClient = new SocketClient(SocketClient.AGENT);
        sClient.start();

        if ((pos_in > 0 && pos_in <= MAX_POS) && (pos_out > 0 && pos_out <= MAX_POS)) {
            // pos_in and pos_out should be between 0 and MAX_POS
        } else {
            LoggerUtil.agent.error(String.format("Settings error, pos_in=%d, pos_out=%d", pos_in, pos_out));
            System.exit(2);
        }
    }

    public static WarehouseHALAgent getInstance() {
        return InstanceHolder.hal;
    }

    /**
     * 将货物存入仓库
     *
     * @param dest 存入目的地
     * @return 成功 true, 失败 中断程序
     */
    public synchronized boolean import_item(int dest) {
        lock.lock();
        // 1. 从入库口 传送进 工件
        JSONObject msg = packMsg(IMPORT_ITEM, "");
        sendCmd(SocketClient.MACHINE, msg);

        // 2. 从入库口移动至 目标位置
        JSONObject extra = new JSONObject();
        extra.put("from", pos_in);
        extra.put("to", dest);
        JSONObject msg2 = packMsg(MOVE_ITEM, extra.toJSONString());
        sendCmd(SocketClient.MACHINE, msg2);

        lock.unlock();
        return true;
    }

    /**
     * 从仓库内取出一件货物
     *
     * @param source   货物存放点
     * @param rfid_msg 需要写入rfid的信息, 不需要写入，则传入null
     * @return 成功 true, 若有错误，则程序终止。
     */
    public synchronized boolean export_item(int source, String rfid_msg) {
        lock.lock();
        // 1. 从 源位置取出货物
        JSONObject extra = new JSONObject();
        extra.put("to", pos_out);
        extra.put("from", source);
        JSONObject msg = packMsg(MOVE_ITEM, extra.toJSONString());
        sendCmd(SocketClient.MACHINE, msg);

        // 2. 将信息写入rfid
        if (rfid_msg != null) {
            JSONObject msg2 = packMsg(WRITE_RFID, rfid_msg);
            sendCmd(SocketClient.RFID, msg2);
        }

        // 3. 从 出货口 输出
        JSONObject msg3 = packMsg(EXPORT_ITEM, "");
        sendCmd(SocketClient.MACHINE, msg3);

        lock.unlock();
        return true;
    }

    /**
     * 发送控制命令，失败重试3次，超过最大失败次数，结束进程
     *
     * @param dest 指令接收方
     * @param msg  消息本体
     * @return 响应字符串
     */
    private String sendCmd(String dest, JSONObject msg) {
        String ans = null;
        while (true) {
            int retry = 0;
            int RETRY_MAX = 3;

            ans = sClient.sendCmd(dest, msg.toJSONString());
            JSONObject ans_jo = (JSONObject) JSONObject.parse(ans);

            if (parseAns(msg, ans_jo)) {
                break;
            }
            if (retry++ > RETRY_MAX) {
                LoggerUtil.agent.error("Hardware retry reach max. " + msg.toJSONString());
                System.exit(2);
            }
        }
        return ans;
    }

    /**
     * 将信息构成JSON
     *
     * @param cmd   控制命令
     * @param extra 额外信息
     * @return 构建完成的JSON字符串
     */
    private JSONObject packMsg(String cmd, String extra) {
        JSONObject msg = new JSONObject();
        msg.put("task_no", taskCnt++);
        msg.put("cmd", cmd);
        msg.put("extra", extra);
        return msg;
    }

    /**
     * 解析响应
     *
     * @param msg 发送出去的消息
     * @param ans 响应回来的消息
     * @return 是否成功执行任务
     */
    private boolean parseAns(JSONObject msg, JSONObject ans) {
        if (!"success".equals(ans.getString("result"))) {
            return false;
        }
        if (msg.getInteger("task_no") != ans.getInteger("task_no")) {
            return false;
        }
        return true;
    }

    /**
     * 单例化该类
     */
    private static class InstanceHolder {
        private final static WarehouseHALAgent hal = new WarehouseHALAgent();
    }
}
