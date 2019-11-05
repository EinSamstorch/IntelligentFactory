package machines.real.commons.hal;

import commons.order.WorkpieceStatus;

/**
 * 机床Hal 包含 加工和时间预估 两个功能.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MachineHalImpl extends BaseHalImpl implements MachineHal {

  private static final String CMD_PROCESS = "process";
  private static final String CMD_EVALUATE = "evaluate";

  public MachineHalImpl() {
    super();
  }

  public MachineHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean process(WorkpieceStatus wpInfo) {
    if (executeCmd(CMD_PROCESS, wpInfo)) {
      wpInfo.setNextProcessStep();
      return true;
    }
    return false;
  }

  @Override
  public int evaluate(WorkpieceStatus wpInfo) {
    if (executeCmd(CMD_EVALUATE, wpInfo)) {
      return (int) Float.parseFloat(getExtraInfo());
    } else {
      return 0;
    }
  }
}
