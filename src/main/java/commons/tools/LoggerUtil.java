package commons.tools;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Logger工具, 整个项目通用的Logger工具, 配置文件默认位置 ：config/log4j.properties.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoggerUtil {

  public static final Logger db = Logger.getLogger("db");
  public static final Logger agent = Logger.getLogger("agent");
  public static final Logger commonTools = Logger.getLogger("tools");
  public static final Logger hal = Logger.getLogger("hal");

  static {
    PropertyConfigurator.configure("./resources/log4j.properties");
  }
}