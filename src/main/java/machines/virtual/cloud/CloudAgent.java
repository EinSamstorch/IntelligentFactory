package machines.virtual.cloud;


import commons.BaseAgent;
import commons.order.OrderInfo;
import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;

import java.util.Map;
import java.util.Queue;
import java.util.Vector;

/**
 * cloud，负责与云端交互.
 * 1. 定时尝试获取新订单.
 * 2. 定时检测是否有新消息需要推送.
 * 3. 对新订单解析，并向仓库发起招标
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.1
 * @since 1.8
 */
public class CloudAgent extends BaseAgent {
    /**
     * 新状态推送列表.
     */
    Queue<String> stateQueue;
    /**
     * 新位置推送列表.
     */
    Queue<String> posQueue;
    /**
     * 云端地址.
     */
    private String website;
    /**
     * mysql配置信息
     */
    private Map<String, String> mysqlSetting;
    /**
     * 新订单列表.
     */
    private Behaviour[] behaviours;

    private Queue<OrderInfo> orderQueue;
    private String serviceType;

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Map<String, String> getMysqlSetting() {
        return mysqlSetting;
    }

    public Queue<OrderInfo> getOrderQueue() {
        return orderQueue;
    }

    public void setOrderQueue(Queue<OrderInfo> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public Queue<String> getStateQueue() {
        return stateQueue;
    }

    public void setStateQueue(Queue<String> stateQueue) {
        this.stateQueue = stateQueue;
    }

    public Queue<String> getPosQueue() {
        return posQueue;
    }

    public void setPosQueue(Queue<String> posQueue) {
        this.posQueue = posQueue;
    }

    @Override
    protected void setup() {
        super.setup();
        // 载入未完成订单
        //loadOrders();
        // 注册DF服务 订单状态更新服务
        registerDf(serviceType);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }


    /**
     * 从mysql数据库中载入未完成订单
     */
    private void loadOrders() {
        Vector<OrderInfo> list = new CloudMysql(mysqlSetting).loadOrders();
        for (OrderInfo oi : list) {
            orderQueue.offer(oi);
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
            LoggerUtil.agent.error("Deregister DF service error!" + fe.getMessage());
        }
    }
}
