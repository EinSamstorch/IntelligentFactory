package machines.virtual.recorder;

import commons.order.OrderTrace;
import commons.tools.LoggerUtil;
import commons.tools.db.MysqlJdbc;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * 订单追踪mysql.
 *
 * @author <a href="mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @date: 2020/10/19 16:59
 */
public class OrderTraceMysql extends MysqlJdbc {
  /**
   * 构造器 .
   *
   * @param mysqlInfo Map[String, String] 包含 mysql_ip, mysql_port, mysql_db, mysql_user 字段
   */
  public OrderTraceMysql(Map<String, String> mysqlInfo) {
    super(mysqlInfo);
  }

  /**
   * 更新数据.
   *
   * @param orderTrace 订单追踪信息
   * @return 是否成功
   */
  public boolean updateInfo(OrderTrace orderTrace) {

    boolean flag = false;
    connect();

    String sql =
        "insert into ordertrace(orderId,workpieceId,bufferId,owner,extra) values(?,?,?,?,?)";
    try {
      PreparedStatement ps = this.con.prepareStatement(sql);
      ps.setString(1, orderTrace.getOrderId());
      ps.setString(2, orderTrace.getWorkpieceId());
      ps.setInt(3, orderTrace.getBufferId());
      ps.setString(4, orderTrace.getOwner());
      ps.setString(5, orderTrace.getExtra());

      int result = ps.executeUpdate();

      if (result > 0) {
        LoggerUtil.db.info(orderTrace.getOwner() + " 插入成功");
        flag = true;
      } else {
        LoggerUtil.db.error(orderTrace.getOwner() + " 插入失败");
        flag = false;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return flag;
  }
}
