package machines.real.buffer;

import machines.real.commons.hal.BaseHal;

public interface BufferHal extends BaseHal {

  /**
   * 入料.
   *
   * @param bufferNo 工位台编号
   * @return 成功true, 否则false
   */
  boolean importItem(int bufferNo);

  /**
   * 出料.
   *
   * @param bufferNo 工位台编号
   * @return 成功true, 否则false
   */
  boolean exportItem(int bufferNo);
}
