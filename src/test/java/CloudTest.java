import cloud.CloudMysql;
import commons.OrderInfo;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;

/**
 * 测试CloudAgent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class CloudTest {
    @Test
    public void testCloudMysql() {
        Map<String, String> map = new HashMap<>();
        map.put("mysql_ip", "127.0.0.1");
        map.put("mysql_port", String.valueOf(3306));
        map.put("mysql_db", "smartfactory");
        map.put("mysql_user", "root");
        map.put("mysql_pwd", "endlessloop");

        CloudMysql mysql = new CloudMysql(map);
        assertNotNull(mysql);

        Connection con = mysql.getCon();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("delete from orderinfo where orderId='999999'");
        } catch (SQLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }


        String orderId = "999999";
        String orderDate = "2019-2-4 14:58:37";
        String orderPrior = "0";
        String orderDetails = "[{ \"goodsId\": \"001\", \"id\": 999, \"jobDes\": {\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}, \"jobNum\": 1 }]";
        String orderDtime = "2020-05-20 00:00:00";

//        JSONObject wp_json = new JSONObject();
//        wp_json.put("goodsId","001");
//        wp_json.put("id","999");
//        wp_json.put("jobDes","This is test");
//        wp_json.put("Motto", "这是测试");
//        wp_json.put("jobNum", 999);
//
//        Workpiece wp = new Workpiece(orderId, wp_json);

        // 加入到 cloud Agent 待分配 列表中
        OrderInfo oi = new OrderInfo(orderDate, orderDtime, orderId, orderPrior, orderDetails);
//        oi.getWpInfoList().add(wp);

        mysql.storeOrderInfo(oi);
    }

}
