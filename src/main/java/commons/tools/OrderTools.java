package commons.tools;

import com.alibaba.fastjson.JSONObject;
import commons.order.OrderInfo;
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
   * 解析每个订单信息的JSON格式.
   *
   * @param jo 订单信息的JSON格式
   * @return OrderInfo类
   */
  public static OrderInfo parseOrderInfo(JSONObject jo) {
    Date date = new Date();
    Calendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(Calendar.HOUR, 10);
    date = calendar.getTime();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String orderDtime = format.format(date);

    String orderDate = jo.getString("orderDate");
    String orderDetails = jo.getString("orderDetails");
    String orderId = jo.getString("orderId");
    String orderPrior = jo.getString("orderPrior");
    return new OrderInfo(orderDate, orderDtime, orderId, orderPrior, orderDetails);
  }
}
