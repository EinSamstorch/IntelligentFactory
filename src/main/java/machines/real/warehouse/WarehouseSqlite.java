package machines.real.warehouse;

import commons.order.WorkpieceInfo;
import commons.tools.LoggerUtil;
import commons.tools.db.DbInterface;
import commons.tools.db.SqliteJdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * warehouse专用sqlite工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WarehouseSqlite extends SqliteJdbc implements DbInterface {
    private Map<Integer, String> rawTable;
    private Queue<Integer> productTable;
    private String nullStr = "000";

    public WarehouseSqlite(String dbName) {
        super(dbName);
    }

    public void initTable() {
        rawTable = getRawTable();
        productTable = getProductTable();
    }

    /**
     * 获取 raw 表里的所有数据
     *
     * @return Map<String position, String goodsid>
     */
    private Map<Integer, String> getRawTable() {
        Map<Integer, String> raw = new ConcurrentHashMap<>(35);

        connect();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT position, goodsid FROM raw");
            while (rs.next()) {
                // ConcurrentHashMap 不允许null值 故用 nullStr 代替
                int position = rs.getInt("position");
                String goodsid = rs.getString("goodsid");
                if (goodsid == null) {
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
     * 从product表里获取空位
     *
     * @return 空位 position
     */
    private Queue<Integer> getProductTable() {
        //ArrayList<Integer> product = new ArrayList<>();
        Queue<Integer> product = new LinkedBlockingQueue<>();

        connect();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT position FROM product WHERE orderid IS NULL");
            while (rs.next()) {
                int position = rs.getInt("position");
                product.offer(position);
            }
        } catch (SQLException e) {
            LoggerUtil.db.error(e.getMessage());
        } finally {
            close();
        }
        return product;

    }


    @Override
    public int getRawQuantityByGoodsId(String goodsid) {
        int quantity = 0;
        for (Map.Entry<Integer, String> entry : rawTable.entrySet()) {
            if (Objects.equals(entry.getValue(), goodsid)) {
                quantity += 1;
            }
        }
        return quantity;
    }


    @Override
    public int getProductQuantity() {
        return productTable.size();
    }


    @Override
    public int getRaw(String goodsid) {
        for (Map.Entry<Integer, String> entry : rawTable.entrySet()) {
            if (Objects.equals(entry.getValue(), goodsid)) {
                int position = entry.getKey();
                // 写入到sqlite表中
                removeRawTable(position);
                // 写入到内存中
                rawTable.put(position, nullStr);
                return position;
            }
        }
        return 0;
    }

    private void removeRawTable(int position) {
        String sqlCmd = String.format("UPDATE raw SET goodsid = null WHERE position = %d", position);
        connect();
        try {
            Statement stmt = con.createStatement();
            int rst = stmt.executeUpdate(sqlCmd);
            if (rst != 1) {
                LoggerUtil.db.error(sqlCmd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }


    }


    @Override
    public int getProduct(WorkpieceInfo wpInfo) {
        Integer position = productTable.poll();
        if (position != null) {
            updateProductTable(position, wpInfo);
            return position;
        } else {
            return 0;
        }
    }

    /**
     * 更新product表信息
     *
     * @param position 位置id
     * @param wpInfo   工件信息
     */
    private void updateProductTable(int position, WorkpieceInfo wpInfo) {
        connect();
        try {
            String sqlCmd = String.format("UPDATE product SET orderid='%s', workpieceid='%s' WHERE position=%d",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId(), position);
            Statement stmt = con.createStatement();
            int rst = stmt.executeUpdate(sqlCmd);
            if (rst != 1) {
                LoggerUtil.db.error(sqlCmd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

}
