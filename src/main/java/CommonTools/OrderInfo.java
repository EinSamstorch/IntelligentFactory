package CommonTools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Vector;


/**
 * @version 1.0.0.0
 * @description: 订单信息类
 * {
 * "orderDate": "2019-2-4 14:58:37",
 * "orderDetails": [{
 * "goodsId": "001",
 * "id": 165,
 * "jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
 * "jobNum": 1
 * }],
 * "orderDtime": "2020-05-20 00:00:00",
 * "orderId": "004061",
 * "orderPrior": "0"
 * }
 * @author: junfeng
 * @since: 2019-02-26
 */

/**
 * 订单信息类.
 * 云端获取的订单信息格式如下：
 * <pre>
 * {
 *  "orderDate": "2019-2-4 14:58:37",
 *  "orderDetails": [{
 *          "goodsId": "001",
 *          "id": 165,
 *          "jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
 *          "jobNum": 1
 *        }],
 *  "orderDtime": "2020-05-20 00:00:00",
 *  "orderId": "004061",
 *  "orderPrior": "0"
 * }
 * </pre>
 *
 * @author      <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class OrderInfo {
    /**
     * 订单生成时间
     */
    private String orderDate;
    /**
     * 订单截止时间
     */
    private String orderDtime;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单优先级
     */
    private String orderPrior;
    /**
     * 订单具体内容
     */
    private String orderDetails;
    /**
     * 根据订单具体内容拆分出来的工件列表
     */
    private Vector<Workpiece> workpieceList = new Vector<>();
    /**
     * 本地生成工件Id，工件数量计数器
     */
    private int wpCnt = 0;

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderDtime() {
        return orderDtime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderPrior() {
        return orderPrior;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public Vector<Workpiece> getWorkpieceList() {
        return workpieceList;
    }

    /**
     * 构造器.
     * @param orderDate 订单生成时间
     * @param orderDtime 订单截止时间
     * @param orderId 订单id
     * @param orderPrior 订单优先级
     * @param orderDetails 订单详细内容
     */
    public OrderInfo(String orderDate, String orderDtime, String orderId, String orderPrior, String orderDetails) {
        this.orderDate = orderDate;
        this.orderDtime = orderDtime;
        this.orderId = orderId;
        this.orderPrior = orderPrior;
        this.orderDetails = orderDetails;
        parseDetails();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 根据订单详细内容，解析工件信息.
     * 详细内容格式如下：
     * <pre>
     *     [{工件信息1},{工件信息2},…]
     * </pre>
     * 工件信息1内容如下：
     * <pre>
     *  {
     *      "goodsId": "001",
     *      "id": 165,
     *      "jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
     *      "jobNum": 1
     *  }
     * </pre>
     */
    private void parseDetails() {
        // orderDetails 不能为空 或 null
        if (!orderDetails.equals("") && orderDetails != null && !orderDetails.equals("[]")) {
            JSONArray details = JSONArray.parseArray(orderDetails);
            Iterator<Object> iterator = details.iterator();
            while (iterator.hasNext()) {
                JSONObject jo = (JSONObject) iterator.next();
                // 因为云端去掉了工件编号属性，故本地生成该属性
                jo.put("id", String.format("%03d", ++wpCnt));
                Workpiece wp = new Workpiece(orderId, jo);
                workpieceList.add(wp);
            }
        }
    }
}
