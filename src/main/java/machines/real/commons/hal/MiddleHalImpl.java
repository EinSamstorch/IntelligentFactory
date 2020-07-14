package machines.real.commons.hal;

import commons.tools.LoggerUtil;
import java.io.Serializable;
import machines.real.commons.actions.MachineAction;

/**
 * 中间层hal实现.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class MiddleHalImpl extends BaseHalImpl implements MiddleHal {

  public MiddleHalImpl() {
  }

  public MiddleHalImpl(int port) {
    super(port);
  }

  @Override
  public boolean executeAction(MachineAction action) {
    LoggerUtil.hal.info("Action: " + action.getCmd() + ", extra: " + action.getExtra().toString());
    return executeCmd(action.getCmd(), action.getExtra(),
        action.getOrderId(), action.getWorkpieceId());
  }

  @Override
  public Serializable getExtra() {
    return getExtraInfo();
  }
}
