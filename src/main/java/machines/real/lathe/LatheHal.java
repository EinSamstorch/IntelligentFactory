package machines.real.lathe;

import machines.real.commons.hal.MachineHal;

/**
 * @author junfeng
 */

public interface LatheHal extends MachineHal {
    /**
     * 夹取工件
     *
     * @return 成功true 失败false
     */
    boolean grabItem();

    /**
     * 松开工件
     *
     * @return 成功true, 失败false
     */
    boolean releaseItem();
}
