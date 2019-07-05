package warehouse;

import commons.tools.LoggerUtil;
import commons.tools.db.SQLiteJDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * warehouse专用sqlite工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseSqlite extends SQLiteJDBC {
    private Map<Integer, String> rawTable;
    public WarehouseSqlite(String dbName) {
        super(dbName);
    }
    private String nullStr = "000";

    public void initTable(){
        rawTable = getRawTable();
    }
    /**
     * 获取 raw 表里的所有数据
     *
     * @return Map<String position, String goodsid>
     */
    private Map<Integer, String> getRawTable() {
        Map<Integer, String> raw = new ConcurrentHashMap<>();

        connect();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT position, goodsid FROM raw");
            while (rs.next()) {
                // ConcurrentHashMap 不允许null值 故用 nullStr 代替
                int position = rs.getInt("position");
                String goodsid = rs.getString("goodsid");
                if(goodsid == null) {
                    goodsid = nullStr;
                }
                raw.put(position, goodsid);
            }
        } catch (SQLException e) {
            LoggerUtil.db.error(e.getMessage());
        } finally {
            close();
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
        int quantity = 0;
        for (Map.Entry<Integer, String> entry : rawTable.entrySet()) {
            if(Objects.equals(entry.getValue(), goodsid)) {
                quantity += 1;
            }
        }
        return quantity;
//        String cmd = String.format("SELECT COUNT(*) FROM raw WHERE goodsid='%s'", goodsid);
//        int quantity = 0;
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(cmd);
//            while (rs.next()) {
//                quantity = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            LoggerUtil.db.error(e.getMessage());
//        }
//        return quantity;
    }

    /**
     * 获得一个原料, 同时从raw table中删去库存
     *
     * @param goodsid 原料种类
     * @return int positon,  0表示失败
     */
    public int getRaw(String goodsid) {
        for (Map.Entry<Integer, String> entry : rawTable.entrySet()) {
            if(Objects.equals(entry.getValue(), goodsid)) {
                int position = entry.getKey();
                // 写入到sqlite表中
                removeRawTable(position);
                // 写入到内存中
                rawTable.put(position, nullStr);
                return position;
            }
        }
        return 0;
//        String cmd = String.format("SELECT position FROM raw WHERE goodsid='%s' limit 1", goodsid);
//        String position = null;
//        try {
//            Statement stmt = con.createStatement();
//            ResultSet rs = stmt.executeQuery(cmd);
//            while (rs.next()) {
//                position = rs.getString(1);
//            }
//            cmd = String.format("UPDATE raw SET goodsid=null WHERE position='%s'", position);
//            int rst = stmt.executeUpdate(cmd);
//            if (rst != 1) {
//                LoggerUtil.db.error(cmd);
//                position = null;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return position;
    }

    private void removeRawTable(int position) {
        String sqlCmd = String.format("UPDATE raw SET goodsid = null WHERE position = %d", position);
        try {
            Statement stmt = con.createStatement();
            int rst = stmt.executeUpdate(sqlCmd);
            if (rst != 1) {
                LoggerUtil.db.error(sqlCmd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
