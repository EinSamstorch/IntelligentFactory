package machines.virtual.cloud.behaviours.cycle;

import machines.virtual.cloud.CloudAgent;
import machines.virtual.cloud.CloudMysql;
import commons.tools.HttpRequest;
import commons.tools.LoggerUtil;
import commons.OrderInfo;
import commons.tools.OrderTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import jade.core.behaviours.TickerBehaviour;

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
        String result = HttpRequest.GetOrder(cagent.getWebsite(), 0, 0);


        // 如果无新订单，则return
        if (result.equals("[]")) return;
        if (result.equals("{\"message\": \"The resultSet is empty！\"}")) return;
        if(result.equals("{\"flag\":\"failure\",\"message\":\"The resultSet is empty！\"}")) return;
        LoggerUtil.agent.info(result);

        // CloudMysql mysqlTool = new CloudMysql(cagent.getMysqlSetting());
        /*
          解析订单 JSONArray
          [{订单1},{订单2},{订单3}]
         */
        JSONObject rst_jo = (JSONObject) JSONObject.parse(result);
        if(rst_jo.getString("flag").equals("success")){
            String orderList_str = rst_jo.getString("orderList");
            try{
                JSONArray jsonArray = JSONArray.parseArray(orderList_str);
                for (Object o : jsonArray) {
                    JSONObject jo = (JSONObject) o;
                    // 加入到 cloud Agent 待分配 列表中
                    OrderInfo oi = OrderTools.parseOrderInfo(jo);
                    // ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                    cagent.getOrderQueue().offer(oi);

                    // 储存订单信息到MYSQL中
                    //mysqlTool.storeOrderInfo(oi);
                }
            }catch (JSONException e){
                // 解析失败JSONARRAY
                LoggerUtil.agent.error(e.getMessage());
            }
        }




    }
}
