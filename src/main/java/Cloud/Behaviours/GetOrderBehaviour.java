package Cloud.Behaviours;

import Cloud.CloudAgent;
import Cloud.CloudMysql;
import CommonTools.HttpRequest;
import CommonTools.LoggerUtil;
import CommonTools.OrderInfo;
import CommonTools.OrderTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jade.core.behaviours.TickerBehaviour;

import java.util.Iterator;

/**
 * 从云端获取新订单并储存至本地mysql数据库中.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class GetOrderBehaviour extends TickerBehaviour {
    private CloudAgent cagent;

    public GetOrderBehaviour(CloudAgent ca, long period) {
        super(ca, period);
        this.cagent = ca;
    }

    @Override
    protected void onTick() {
        String result = HttpRequest.GetOrder(cagent.getWebsite(), 0, 0);


        // 如果无新订单，则return
        if (result.equals("[]")) return;
        LoggerUtil.agent.info(result);

        CloudMysql mysqlTool = new CloudMysql(cagent.getMysqlSetting());
        /*
          解析订单 JSONArray
          [{订单1},{订单2},{订单3}]
         */
        JSONArray jsonArray = JSONArray.parseArray(result);
        Iterator<Object> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject jo = (JSONObject) iterator.next();
            // 加入到 Cloud Agent 待分配 列表中
            OrderInfo oi = OrderTools.parseOrderInfo(jo);
            cagent.getOrderQueue().offer(oi);

            // 储存订单信息到MYSQL中
            mysqlTool.storeOrderInfo(oi);
        }
    }
}
