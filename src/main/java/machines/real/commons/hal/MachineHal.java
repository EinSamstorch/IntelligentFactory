package machines.real.commons.hal;

import commons.order.WorkpieceStatus;

public interface MachineHal {

  /**
   * 加工处理工件.
   *
   * @param wpInfo 工件信息
   * @return 成功true
   */
  boolean process(WorkpieceStatus wpInfo);

  /**
   * 预估加工时间.
   *
   * @param wpInfo 工件信息
   * @return 加工时间 单位秒
   */
  int evaluate(WorkpieceStatus wpInfo);
}
