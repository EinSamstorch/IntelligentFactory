package commons.tools.db;

import commons.tools.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Sqlite连接器.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class SQLiteJDBC {
    protected Connection con = null;
    private String dbName;

    public SQLiteJDBC(String dbName) {
        this.dbName = dbName;
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.con = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbName));
            this.con.setAutoCommit(true);
        } catch (Exception e) {
            LoggerUtil.db.error(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public void close() {
        if (this.con != null) {
            try {
                this.con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
