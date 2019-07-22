import commons.tools.*;
import commons.tools.db.MysqlJDBC;
import commons.tools.db.SQLiteJDBC;
import commons.order.OrderInfo;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.*;

/**
 * 测试Commons.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class CommonsTest {
    @Test
    public void testINILoader() {
        IniLoader ini = new IniLoader();
        assertNotNull(ini);

        Map<String, String> load = ini.load(IniLoader.SECTION_MYSQL);

        String mysql_user = load.get("mysql_user");
        assertEquals(mysql_user, "root");
    }

    @Test
    public void testLoggerUtil() {
        assertNotNull(LoggerUtil.db);
        assertNotNull(LoggerUtil.agent);
    }

    @Test
    public void testSQLiteJDBC() {
        SQLiteJDBC sqlite = new SQLiteJDBC("resources/db/test.db");
        assertNotNull(sqlite);
    }

    @Test
    public void testMysqlJDBC() {
        Map<String, String> map = new HashMap<>();
        map.put("mysql_ip", "127.0.0.1");
        map.put("mysql_port", String.valueOf(3306));
        map.put("mysql_db", "smartfactory");
        map.put("mysql_user", "root");
        map.put("mysql_pwd", "endlessloop");

        MysqlJDBC mysql = new MysqlJDBC(map);
        assertNotNull(mysql);
    }

    @Test
    public void testHttp() {
        String rst = HttpRequest.sendGet("http://www.baidu.com", "");
        assertNotNull(rst);
    }

    @Test
    public void testParseOrderInfo() {
        String orderId = "999999";
        String orderDate = "2019-2-4 14:58:37";
        String orderPrior = "0";
        String orderDetails = "[{ \"goodsId\": \"001\", \"id\": 999, \"jobDes\": {\"D1\":\"80\",\"D2\":\"35.5\",\"D3\":\"58.0\",\"D4\":\"8.0\",\"D5\":\"70.0\",\"N\":\"4\",\"L1\":\"15.0\",\"L2\":\"30.0\",\"Motto\":\"南航智造\"}, \"jobNum\": 1 }]";


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, 10);
        date = calendar.getTime();
        String orderDtime = format.format(date);

        OrderInfo oi = new OrderInfo(orderDate, orderDtime, orderId, orderPrior, orderDetails);

        JSONObject jo = (JSONObject) JSONObject.parse(oi.toString());
        OrderInfo oi2 = OrderTools.parseOrderInfo(jo);

        class OrderInfoComparator implements Comparator<OrderInfo> {

            @Override
            public int compare(OrderInfo o1, OrderInfo o2) {
                if (!o1.getOrderId().equals(o2.getOrderId())) {
                    return 1;
                }
                if (!o1.getOrderDate().equals(o2.getOrderDate())) {
                    return 2;
                }
//                if (!o1.getOrderDtime().equals(o2.getOrderDtime())) {
//                    return 3;
//                }
                if (!o1.getOrderPrior().equals((o2.getOrderPrior()))) {
                    return 4;
                }
                if (!o1.getOrderDetails().equals(o2.getOrderDetails())) {
                    return 5;
                }
                return 0;
            }
        }
        OrderInfoComparator oic = new OrderInfoComparator();
        assertEquals(0, oic.compare(oi, oi2));
    }

    @Test
    public void testGetOrder() {
        String rst = HttpRequest.GetOrder("120.26.53.90:8080", 0, 0);
        boolean matches = rst.matches("^\\[.*\\]$");
        System.out.println(rst);
        assertTrue(matches);
    }

//    @Test
//    public void testWarehouseSqlite() {
//        WarehouseSqlite sqlite = new WarehouseSqlite("resources/db/warehouse.db");
//        String goodsid = "001";
//        int cnt = 0;
//        Map<String, String> raw = sqlite.getRawTable();
//        for (String position : raw.keySet()) {
//            String gid = raw.get(position);
//            if (gid == null) continue;
//            if (gid.equals(goodsid)) cnt++;
//            //System.out.println(String.format("position: %s , goodsid: %s.", position, gid));
//        }
//        assertEquals(cnt, sqlite.getRawQuantityByGoodsId(goodsid));
//        String position = sqlite.getRaw(goodsid);
//        assertNotNull(position);
//        assertEquals(cnt - 1, sqlite.getRawQuantityByGoodsId(goodsid));
//
//        class testSqlite extends WarehouseSqlite {
//            public testSqlite(String dbName) {
//                super(dbName);
//            }
//
//            public boolean putGoodsid(String position, String goodsid) {
//                String cmd = String.format("UPDATE raw SET goodsid = '%s' WHERE position = '%s'", goodsid, position);
//                try {
//                    Statement stmt = con.createStatement();
//                    int rst = stmt.executeUpdate(cmd);
//                    if (rst != 1) {
//                        System.err.println(cmd);
//                        return false;
//                    }
//                    return true;
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        }
//
//        testSqlite ts = new testSqlite("resources/db/warehouse.db");
//        assertTrue(ts.putGoodsid(position, goodsid));
//    }

}


