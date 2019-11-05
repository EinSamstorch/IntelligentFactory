package machines.real.agv;

import machines.real.commons.hal.BaseHal;

public interface AgvHal extends BaseHal {

  /**
   * 运输工件.
   *
   * @param from 起始工位台
   * @param to 目的工位台
   * @return 成功true
   */
  boolean move(int from, int to);
}
