package CommonTools.db;

import CommonTools.LoggerUtil;

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
public class MysqlJDBC {
    /**
     * 数据库连接.
     */
    public Connection con;

    /**
     * 测试代码时使用，用于编写额外的 mysql语句 清空测试结果
     *
     * @return mysql connection
     */
    public Connection getCon() {
        return con;
    }

    /**
     * 构造器 .
     *
     * @param mysqlInfo Map<String, String> 包含 mysql_ip, mysql_port, mysql_db, mysql_user 字段
     */
    public MysqlJDBC(Map<String, String> mysqlInfo) {
        String driver = "com.mysql.jdbc.Driver";
        String url = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8",
                mysqlInfo.get("mysql_ip"),
                mysqlInfo.get("mysql_port"),
                mysqlInfo.get("mysql_db"));
        String user = mysqlInfo.get("mysql_user");
        String password = mysqlInfo.get("mysql_pwd");
        try {
            Class.forName(driver);
            this.con = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            LoggerUtil.db.error("无法连接MYSQL驱动");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
