package machines.real.commons;

import commons.AgentTemplate;
import commons.tools.IniLoader;

import java.util.Map;

/**
 * template for real machine agents.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RealMachineAgentTemplate extends AgentTemplate {
    protected Integer halPort;
    protected String armPwd;

    @Override
    protected void loadINI() {
        loadHalPort();
        loadArmPassword();
    }

    /**
     * load hal_port from [common] in setting.ini
     */
    private void loadHalPort(){
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_COMMON);

        String SETTING_HAL_PORT = "hal_port";
        halPort = new Integer(setting.get(SETTING_HAL_PORT));
    }

    private void loadArmPassword(){
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_COMMON);

        final String SETTING_ARM_PASSWORD = "arm_password";
        armPwd = setting.get(SETTING_ARM_PASSWORD);
    }
}
