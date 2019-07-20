package commons.tools;

import commons.order.OrderInfo;
import com.alibaba.fastjson.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 用于解析json格式的订单信息.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class OrderTools {
    /**
     * 解析每个订单信息的JSON格式
     *
     * @param jo 订单信息的JSON格式
     * @return OrderInfo类
     */
    public static OrderInfo parseOrderInfo(JSONObject jo) {
        /* 解析每个订单数据， json
         * {
         * 	"orderDate": "2019-2-4 14:58:37",
         * 	"orderDetails": [{
         * 		"goodsId": "001",
         * 		"id": 165,
         * 		"jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
         * 		"jobNum": 1
         *        }],
         * 	"orderId": "004061",
         * 	"orderPrior": "0"
         * }
         */
        String orderDate = jo.getString("orderDate");
        String orderDetails = jo.getString("orderDetails");
        //String orderDtime = jo.getString("orderDtime");
        String orderId = jo.getString("orderId");
        String orderPrior = jo.getString("orderPrior");

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 10);
        date = calendar.getTime();
        String orderDtime = format.format(date);

        return new OrderInfo(orderDate, orderDtime, orderId, orderPrior, orderDetails);
    }


}
