package machines.real.lathe;

import machines.real.commons.hal.MachineHal;

/**
 * 车床Hal接口.
 *
 * @author junfeng
 */

public interface LatheHal extends MachineHal {

  /**
   * 夹取工件.
   *
   * @return 成功true 失败false
   */
  boolean grabItem();

  /**
   * 松开工件.
   *
   * @return 成功true, 失败false
   */
  boolean releaseItem();
}
