package commons.tools;

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
     * 车床专用配置
     */
    public static final String SECTION_LATHE = "lathe";

    public static final String SECTION_ARMROBOT = "armrobot";

    public static final String SECTION_MILL = "mill";

    /**
     * 配置文件默认路径：{@value CONFIG_NAME}
     */
    private static final String CONFIG_NAME = "./resources/config/setting.ini";

    /**
     * 读取配置文件并返回ini文件实例，如果配置文件读取失败，则终止程序.
     *
     * @return 加载配置文件的ini实例.
     */
    private static Ini loadIni() {
        try {
            Ini ini = new Ini(new FileReader(CONFIG_NAME));
            return ini;
        } catch (InvalidFileFormatException ife) {
            LoggerUtil.agent.error("Reading ini file error, Invalid file format. " + ife.getMessage());
            System.exit(1);
        } catch (IOException e) {
            LoggerUtil.agent.error("Reading ini file error, IOException. " + e.getMessage());
            System.exit(1);
        }
        System.exit(1);
        return null;
    }


    public static Map<String, String> load(String secName) {
        Ini ini = loadIni();
        return ini.get(secName);
    }

    /**
     * load arm_password from [{agent_local_name}] in setting.ini
     *
     * @return {arm_password} or {null} if there is no arm_password
     */
    public static String loadArmPassword(String sectionName) {
        Map<String, String> setting = IniLoader.load(sectionName);

        final String SETTING_ARM_PASSWORD = "arm_password";
        String password = setting.get(SETTING_ARM_PASSWORD);
        return password == null ? "" : password;
    }


    /**
     * load hal_port from [{agent_local_name}] in setting.ini
     *
     * @return {hal_port} or {5656} if there is no hal_port
     */
    public static Integer loadHalPort(String sectionName) {
        Map<String, String> setting = IniLoader.load(sectionName);

        String SETTING_HAL_PORT = "hal_port";

        String portStr = setting.get(SETTING_HAL_PORT);
        // 无hal_port配置项 则默认 5656
        if (portStr != null) {
            return new Integer(portStr);
        } else {
            return 5656;
        }

    }


    /**
     * load buffer_index from [{agent_local_name}] in setting.ini
     *
     * @return int[] or {null} if there is no buffer_index
     */
    public static int[] loadBuffers(String sectionName) {
        Map<String, String> setting = IniLoader.load(sectionName);

        final String SETTING_BUFFER_INDEX = "buffer_index";
        String bufferIndexStr = setting.get(SETTING_BUFFER_INDEX);
        if (bufferIndexStr == null) {
            return null;
        } else {
            String[] split = bufferIndexStr.split(",");
            int[] indexes = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                indexes[i] = Integer.parseInt(split[i].trim());
            }
            return indexes;
        }

    }

}
