package CommonTools.db;

import CommonTools.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Sqlite连接器.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class SQLiteJDBC {
    public Connection con = null;

    public SQLiteJDBC(String dbName) {
        try {
            Class.forName("org.sqlite.JDBC");
            this.con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbName));
            this.con.setAutoCommit(true);
        } catch (Exception e) {
            LoggerUtil.db.error(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
