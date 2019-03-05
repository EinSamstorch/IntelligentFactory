package Cloud;

import Cloud.Behaviours.DetectUpdateBehaviour;
import Cloud.Behaviours.GetOrderBehaviour;
import CommonTools.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

import java.util.Map;
import java.util.Vector;

/**
 * Cloud，负责与云端交互.
 * 1. 定时尝试获取新订单.
 * 2. 定时检测是否有新消息需要推送.
 * 3. 对新订单解析，并向仓库发起招标
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.1
 * @since 1.8
 */
public class CloudAgent extends AgentTemplate {
    /**
     * TICKER周期，单位ms
     */
    private static final int TICKER_TIME = 10000;
    /**
     * 云端地址.
     */
    private String website;
    /**
     * ini配置信息.
     */
    private Map<String, String> setting;
    /**
     * 新订单列表.
     */
    private Vector<OrderInfo> orderList = new Vector<>();
    /**
     * 新状态推送列表.
     */
    Vector<String> stateList = new Vector<>();
    /**
     * 新位置推送列表.
     */
    Vector<String> posList = new Vector<>();

    public String getWebsite() {
        return website;
    }

    public Map<String, String> getSetting() {
        return setting;
    }

    public Vector<OrderInfo> getOrderList() {
        return orderList;
    }

    public Vector<String> getStateList() {
        return stateList;
    }

    public Vector<String> getPosList() {
        return posList;
    }

    @Override
    protected void setup() {
        super.setup();
        // 载入未完成订单
        loadOrders();
        // 注册DF服务 订单状态更新服务
        registerDF(DFServiceType.CLOUD_UPDATE);


        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        //独立线程 定时从云端 获取新订单
        Behaviour b = new GetOrderBehaviour(this, TICKER_TIME);
        addBehaviour(tbf.wrap(b));

        // 独立线程 定时检查新推送消息
        b = new DetectUpdateBehaviour(this, TICKER_TIME);
        addBehaviour(tbf.wrap(b));


    }

    /**
     * 载入INI文件 并读取对应配置
     */
    protected void loadINI() {
        setting = IniLoader.load(IniLoader.SECTION_CLOUD);
        website = setting.get("website");

        if (website == null) {
            LoggerUtil.agent.error("Load website info error. Null pointer");
            System.exit(2);
        }
    }

    /**
     * 从mysql数据库中载入未完成订单
     */
    private void loadOrders() {
        Vector<OrderInfo> list = new CloudMysql(IniLoader.load(IniLoader.SECTION_MYSQL)).loadOrders();
        for (OrderInfo oi : list) {
            orderList.add(oi);
        }
    }
}
