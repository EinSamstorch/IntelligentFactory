package machines.real.agv;

import machines.real.commons.hal.BaseHal;

/**
 * 单AGV系统的AGV agent硬件层.
 *
 * <p>早期硬件层兄弟不愿分离出功能
 *
 * @author junfeng
 */
public interface SingleAgvHal extends BaseHal {

  /**
   * 运输工件.
   *
   * @param from 起始工位台
   * @param to   目的工位台
   * @return 成功true
   */
  boolean move(int from, int to);
}
