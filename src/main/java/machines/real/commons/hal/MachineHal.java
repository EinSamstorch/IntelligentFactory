package machines.real.commons.hal;

import commons.order.WorkpieceStatus;

public interface MachineHal extends BaseHal {

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

  /**
   * 预估加工时间, 超时失败.
   *
   * @param wpInfo 工件信息
   * @param timeoutMills 超时时间, 0表示不超时.
   * @return 负数代表超时, 正数为预估加工时间
   */
  int evaluate(WorkpieceStatus wpInfo, long timeoutMills);
}
