package machines.real.warehouse;

import commons.tools.SocketConnector;
import commons.tools.hal.*;
import commons.tools.IniLoader;
import commons.tools.LoggerUtil;
import commons.tools.SocketClient;
import com.alibaba.fastjson.JSONObject;

import java.net.SocketTimeoutException;
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
public class WarehouseHal extends BaseHal {
    private static final String CMD_MOVE_ITEM = "move_item";
    private static final String FIELD_FROM = "from";
    private static final String FIELD_TO = "to";
    public WarehouseHal() {
        super();
    }

    public WarehouseHal(int port) {
        super(port);
    }

    public boolean moveItem(int from, int to) {
        JSONObject extra = new JSONObject();
        extra.put(FIELD_FROM, from);
        extra.put(FIELD_TO, to);
        return executeCmd(CMD_MOVE_ITEM, extra);
    }
}
