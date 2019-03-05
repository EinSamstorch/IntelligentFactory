package Cloud;

import CommonTools.LoggerUtil;
import CommonTools.OrderInfo;
import CommonTools.Workpiece;
import CommonTools.db.MysqlJDBC;
import com.alibaba.fastjson.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Vector;

/**
 * CloudAgent专用Mysql类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.1
 * @since 1.8
 */
public class CloudMysql extends MysqlJDBC {
    public CloudMysql(Map<String, String> mysqlInfo) {
        super(mysqlInfo);
    }

    /**
     * 储存订单信息
     *
     * @param oi 订单信息
     */
    public void storeOrderInfo(OrderInfo oi) {
        try {
            String cmd = String.format("INSERT INTO OrderInfo(orderDate, orderDtime, orderId, orderPrior) VALUES (?,?,?,?)");
            PreparedStatement ps = con.prepareStatement(cmd);
            ps.setString(1, oi.getOrderDate());
            ps.setString(2, oi.getOrderDtime());
            ps.setString(3, oi.getOrderId());
            ps.setString(4, oi.getOrderPrior());
            int rst = ps.executeUpdate();
            if (rst != 1) {
                LoggerUtil.db.error(String.format("INSERT OrderInfo FAILED, %s", oi.toString()));
            }
            for (Workpiece wp : oi.getWorkpieceList()) {
                cmd = String.format("INSERT INTO OrderDetails(orderId,id,goodsid,num,jobdes) VALUES (?,?,?,?,?)");
                ps = con.prepareStatement(cmd);
                ps.setString(1, oi.getOrderId());
                ps.setString(2, wp.getId());
                ps.setString(3, wp.getGoodsId());
                ps.setInt(4, wp.getJobNum());
                ps.setString(5, wp.getJobDes());
                rst = ps.executeUpdate();
                if (rst != 1) {
                    LoggerUtil.db.error(String.format("INSERT OrderDetails FAILED, %s", wp.toString()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从数据库载入未完成订单及工件信息
     *
     * @return 未完成订单列表
     */
    public Vector<OrderInfo> loadOrders() {
        Vector<OrderInfo> list = new Vector<>();
        try {
            // 从 mysql载入 状态 不等于 "FINISHED" 的 订单基本信息
            Statement statement = con.createStatement();
            String cmd = "SELECT orderstate.orderId,orderinfo.orderDate,orderinfo.orderDtime,orderinfo.orderPrior FROM orderstate, orderinfo WHERE orderstate.orderId = orderinfo.orderId AND orderstate.state != 'FINISHED'";
            ResultSet rs = statement.executeQuery(cmd);
            while (rs.next()) {
                String orderId = rs.getString("orderId");
                String orderDate = rs.getString("orderDate");
                String orderDtime = rs.getString("orderDtime");
                String orderPrior = rs.getString("orderPrior");
                list.add(new OrderInfo(orderDate, orderDtime, orderId, orderPrior, ""));
            }
            statement.close();
            rs.close();

            // 遍历所有 未完成订单 ， 从mysql载入未完成的工件信息
            cmd = "SELECT od.id, od.num, od.goodsid, od.jobdes FROM orderdetails AS od, workpiecestate AS wps WHERE od.orderId = ? AND od.orderId = wps.orderId AND od.id = wps.id AND wps.state != 'FINISHED'";
            for (OrderInfo oi : list) {
                PreparedStatement ps = con.prepareStatement(cmd);
                ps.setString(1, oi.getOrderId());
                ResultSet rs2 = ps.executeQuery();
                while (rs2.next()) {
                    JSONObject jo = new JSONObject();
                    jo.put("id", rs2.getString("id"));
                    jo.put("goodsId", rs2.getString("goodsid"));
                    jo.put("jobNum", rs2.getInt("num"));
                    jo.put("jobDes", rs2.getString("jobDes"));
                    Workpiece wp = new Workpiece(oi.getOrderId(), jo);
                    oi.getWorkpieceList().add(wp);
                }
                ps.close();
                rs2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
