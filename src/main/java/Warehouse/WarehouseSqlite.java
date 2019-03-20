package Warehouse;

import CommonTools.LoggerUtil;
import CommonTools.db.SQLiteJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * warehouse专用sqlite工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseSqlite extends SQLiteJDBC {
    public WarehouseSqlite(String dbName) {
        super(dbName);
    }

    /**
     * 获取 raw 表里的所有数据
     *
     * @return Map<String               position       ,               String               goodsid>
     */
    public Map<String, String> getRawTable() {
        Map<String, String> raw = new HashMap<>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT position, goodsid FROM raw");
            while (rs.next()) {
                raw.put(rs.getString("position"), rs.getString("goodsid"));
            }
        } catch (SQLException e) {
            LoggerUtil.db.error(e.getMessage());
        }
        return raw;
    }

    /**
     * 查询raw table, 对应 goodsid的原料剩余数量
     *
     * @param goodsid 代查询种类
     * @return 剩余原料数量, 不存在的原料 返回数量0
     */
    public int getRawQuantityByGoodsId(String goodsid) {
        String cmd = String.format("SELECT COUNT(*) FROM raw WHERE goodsid='%s'", goodsid);
        int quantity = 0;
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

    /**
     * 获得一个原料, 同时从raw table中删去库存
     *
     * @param goodsid 原料种类
     * @return String positon,  null表示失败
     */
    public String getRaw(String goodsid) {
        String cmd = String.format("SELECT position FROM raw WHERE goodsid='%s' limit 1", goodsid);
        String position = null;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(cmd);
            while (rs.next()) {
                position = rs.getString(1);
            }
            cmd = String.format("UPDATE raw SET goodsid=null WHERE position='%s'", position);
            int rst = stmt.executeUpdate(cmd);
            if (rst != 1) {
                LoggerUtil.db.error(cmd);
                position = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return position;
    }
}
