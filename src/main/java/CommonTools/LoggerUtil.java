package CommonTools;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger工具,
 * 整个项目通用的Logger工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoggerUtil {
    /**
     * 配置文件默认位置 ：config/log4j.properties
     */
    static {
        PropertyConfigurator.configure("./resources/config/log4j.properties");
    }

    public static Logger db = Logger.getLogger("db");
    public static Logger agent = Logger.getLogger("agent");
}