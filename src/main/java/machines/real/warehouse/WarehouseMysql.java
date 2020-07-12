package machines.real.warehouse;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import commons.tools.db.MysqlJdbc;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseMysql extends MysqlJdbc implements DbInterface {

  /**
   * 构造器 .
   *
   * @param mysqlInfo 包含 mysql_ip, mysql_port, mysql_db, mysql_user 字段
   */
  public WarehouseMysql(Map<String, String> mysqlInfo) {
    super(mysqlInfo);
  }


  @Override
  public int getRaw(String goodsid) {
    String cmd = String.format("SELECT position FROM raw WHERE goodsid='%s' limit 1", goodsid);
    int position = 0;
    connect();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(cmd);
      while (rs.next()) {
        position = rs.getInt(1);
      }
      cmd = String.format("UPDATE raw SET goodsid='000' WHERE position='%s'", position);
      int rst = stmt.executeUpdate(cmd);
      if (rst != 1) {
        LoggerUtil.db.error(cmd);
        position = 0;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    if (position == 0) {
      throw new IllegalArgumentException("Position value can't be 0");
    }
    return position;
  }


  @Override
  public int getRawQuantityByGoodsId(String goodsid) {
    String cmd = String.format("SELECT COUNT(*) FROM raw WHERE goodsid='%s'", goodsid);
    int quantity = 0;
    connect();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(cmd);
      while (rs.next()) {
        quantity = rs.getInt(1);
      }
    } catch (SQLException e) {
      LoggerUtil.db.error(e.getMessage());
    }
    return quantity;
  }


  @Override
  public int getProduct(WorkpieceStatus wpInfo) {
    int position = 0;
    connect();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(
          "SELECT position FROM product WHERE orderid IS NULL and workpieceid IS NULL");
      while (rs.next()) {
        position = rs.getInt(1);
      }

      if (position == 0) {
        throw new IllegalArgumentException("Position value can't be 0");
      }

      PreparedStatement pstmt = con
          .prepareStatement("UPDATE product SET orderid = ?, workpieceid = ? WHERE position = ?");
      pstmt.setString(1, wpInfo.getOrderId());
      pstmt.setString(2, wpInfo.getWorkpieceId());
      pstmt.setInt(3, position);

      int result = pstmt.executeUpdate();
      if (result != 1) {
        LoggerUtil.db
            .error(String.format("Failed to update product table on position: %d", position));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      LoggerUtil.db.error(e.getMessage());
    } finally {
      close();
    }
    return position;
  }


  @Override
  public int getProductQuantity() {
    int quantity = 0;
    connect();
    try {
      Statement stmt = con.createStatement();
      ResultSet rs = stmt.executeQuery(
          "SELECT COUNT(*) FROM product WHERE orderid IS NULL and workpieceid IS NULL");
      while (rs.next()) {
        quantity = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      close();
    }
    return quantity;
  }

}
