package commons.tools.db;

import commons.tools.LoggerUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Mysql工具，通过JSONObject中的数据库信息，连接Mysql数据库.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 2.0.0.0
 * @since 1.8
 */
public class MysqlJdbc {
    /**
     * 数据库连接.
     */
    protected Connection con;
    private String url;
    private String user;
    private String password;

    /**
     * 构造器 .
     *
     * @param mysqlInfo Map<String, String> 包含 mysql_ip, mysql_port, mysql_db, mysql_user 字段
     */
    public MysqlJdbc(Map<String, String> mysqlInfo) {
        url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8",
                mysqlInfo.get("mysql_ip"),
                mysqlInfo.get("mysql_port"),
                mysqlInfo.get("mysql_db"));
        user = mysqlInfo.get("mysql_user");
        password = mysqlInfo.get("mysql_pwd");
    }

    protected void connect() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);
            this.con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            LoggerUtil.db.error("无法连接MYSQL驱动");
            e.printStackTrace();
        } catch (Exception e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }
    }

    protected void close() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
