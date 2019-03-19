package Warehouse.HAL;

import CommonTools.IniLoader;
import CommonTools.LoggerUtil;
import CommonTools.MySocket.Client.SocketClient;

import java.util.Map;

/**
 * 仓库硬件适配层代理人，提供统一接口函数。具体硬件操作，通过socket server转发至对应socket client处理。.
 *
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseHALAgent {
    private int pos_in;
    private int pos_out;
    private SocketClient sClient;
    private static final int MAX_POS = 70;
    /**
     *  单例化该类
     */
    private WarehouseHALAgent(){
        Map<String, String> settings = IniLoader.load(IniLoader.SECTION_WAREHOUSE);
        pos_in = Integer.parseInt(settings.get("pos_in"));
        pos_out = Integer.parseInt(settings.get("pos_out"));

        sClient = new SocketClient(SocketClient.AGENT);
        sClient.start();

        if((pos_in > 0  && pos_in <= MAX_POS ) && (pos_out > 0  && pos_out <= MAX_POS )){
            // pos_in and pos_out should be between 0 and MAX_POS
        }else {
            LoggerUtil.agent.error(String.format("Settings error, pos_in=%d, pos_out=%d", pos_in, pos_out));
            System.exit(2);
        }
    }
    private static class InstanceHolder{
        private final static WarehouseHALAgent hal = new WarehouseHALAgent();
    }
    public static WarehouseHALAgent getInstance(){
        return InstanceHolder.hal;
    }

    public synchronized boolean import_item(int dest){
        boolean rst = false;



        return rst;
    }
}
