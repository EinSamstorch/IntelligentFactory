package CommonTools;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


/**
 * 配置载入工具.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class IniLoader {
    /**
     * 配置文件默认路径：{@value CONFIG_NAME}
     */
    private static final String CONFIG_NAME = "./resources/config/setting.ini";
    /**
     * 通用配置区块
     */
    public static final String SECTION_COMMON = "common";
    /**
     * cloud专用配置
     */
    public static final String SECTION_CLOUD = "cloud";
    /**
     * 仓库专用配置
     */
    public static final String SECTION_WAREHOUSE = "warehouse";
    /**
     * mysql专用配置
     */
    public static final String SECTION_MYSQL = "mysql";

    /**
     * 读取配置文件并返回ini文件实例，如果配置文件读取失败，则终止程序.
     *
     * @return 加载配置文件的ini实例.
     */
    private static Ini loadINI() {
        try {
            Ini ini = new Ini(new FileReader(CONFIG_NAME));
            return ini;
        } catch (InvalidFileFormatException ife) {
            LoggerUtil.db.error("Reading ini file error, Invalid file format. " + ife.getMessage());
            System.exit(1);
        } catch (IOException e) {
            LoggerUtil.db.error("Reading ini file error, IOException. " + e.getMessage());
            System.exit(1);
        }
        System.exit(1);
        return null;
    }

//    /**
//     * 通用配置读取方法.
//     * @param secStr Section名字
//     * @param keys 待读取参数列表
//     * @return JSONObject 格式：{key1:value1,key2:value2, …} .
//     */
//    private static JSONObject load(String secStr, ArrayList<String> keys){
//        Ini ini = loadINI();
//        Ini.Section section = ini.get(secStr);
//
//
//        JSONObject setting = new JSONObject();
//
//        for (String k : keys) {
//            setting.put(k,section.get(k).trim());
//        }
//        return setting;
//    }

    public static Map<String, String> load(String secName) {
        Ini ini = loadINI();
        return ini.get(secName);
    }
//    public static JSONObject loadCommon(){
//        final String HOST = "host";
//        ArrayList<String> keys = new ArrayList<>();
//        keys.add(HOST);
//        return load(SECTION_COMMON, keys);
//    }
//
//    /**
//     * 读取Cloud配置参数.
//     * @return 云端网址，Mysql数据库信息
//     */
//    public static JSONObject loadCloud(){
//        final String WEBSITE = "website";
//        final String MYSQL_USER = "mysql_user";
//        final String MYSQL_PWD = "mysql_pwd";
//        final String MYSQL_DB = "mysql_db";
//        final String MYSQL_IP = "mysql_ip";
//        final String MYSQL_PORT = "mysql_port";
//
//        ArrayList<String> keys = new ArrayList<>();
//        keys.add(WEBSITE);
//        keys.add(MYSQL_USER);
//        keys.add(MYSQL_PWD);
//        keys.add(MYSQL_DB);
//        keys.add(MYSQL_IP);
//        keys.add(MYSQL_PORT);
//
//        return load(SECTION_CLOUD, keys);
//    }


}
