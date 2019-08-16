package machines.virtual.cloud.behaviours.cycle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import commons.order.OrderInfo;
import commons.tools.HttpRequest;
import commons.tools.LoggerUtil;
import commons.tools.OrderTools;
import jade.core.behaviours.TickerBehaviour;
import machines.virtual.cloud.CloudAgent;

/**
 * 从云端获取新订单并储存至本地mysql数据库中.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class GetOrder extends TickerBehaviour {
    private CloudAgent cagent;

    public GetOrder(CloudAgent ca, long period) {
        super(ca, period);
        this.cagent = ca;
    }

    @Override
    protected void onTick() {
        String result = HttpRequest.getOrder(cagent.getWebsite(), 0, 0);


        // 如果无新订单，则return
        if ("[]".equals(result)) {
            return;
        }
        if ("{\"message\": \"The resultSet is empty！\"}".equals(result)) {
            return;
        }
        if ("{\"flag\":\"failure\",\"message\":\"The resultSet is empty！\"}".equals(result)) {
            return;
        }
        LoggerUtil.agent.info(result);

        /*
          解析订单 JSONArray
          [{订单1},{订单2},{订单3}]
         */
        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
        if ("success".equals(resultJson.getString("flag"))) {
            String orderListStr = resultJson.getString("orderList");

            try {
                JSONArray jsonArray = JSONArray.parseArray(orderListStr);
                for (Object o : jsonArray) {
                    JSONObject jo = (JSONObject) o;
                    // 加入到 cloud Agent 待分配 列表中
                    OrderInfo oi = OrderTools.parseOrderInfo(jo);
                    cagent.getOrderQueue().offer(oi);

                    // 储存订单信息到MYSQL中
                    //mysqlTool.storeOrderInfo(oi);
                }
            } catch (JSONException e) {
                LoggerUtil.agent.error(e.getMessage());
            }
        }


    }
}
