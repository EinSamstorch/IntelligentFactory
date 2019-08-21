package commons.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 工件类，储存工件信息.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class Workpiece implements Serializable {
    /**
     * 工件种类
     */
    private String goodsId;
    /**
     * 工件id
     */
    private String id;
    /**
     * 工件数量
     */
    private int jobNum;
    /**
     * 工件具体尺寸参数 - JSON String
     */
    private String jobDes;


    private String orderId;

    /**
     * 构造器，通过JSONObject解析出工件信息
     *
     * @param jo 工件信息
     *           <pre>
     *                                 {
     *                                  "goodsId": "001",
     *                                  "id": 165,
     *                                  "jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
     *                                  "jobNum": 1
     *                                 }
     *                                </pre>
     */
    public Workpiece(String orderId, JSONObject jo) {
        this.orderId = orderId;
        goodsId = jo.getString("goodsId");
        id = jo.getString("id");
        jobDes = jo.getString("jobDes");
        jobNum = jo.getInteger("jobNum");
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getId() {
        return id;
    }

    public int getJobNum() {
        return jobNum;
    }

    public String getJobDes() {
        return jobDes;
    }


    public String getOrderId() {
        return orderId;
    }

    /**
     * @return 生成JSON String
     * 如下：
     * <pre>
     * {
     * "goodsId": "001",
     * "id": 165,
     * "jobDes": "{\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}",
     * "jobNum": 1
     * }
     * </pre>
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
