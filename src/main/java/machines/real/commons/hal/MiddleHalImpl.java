package machines.real.commons.hal;

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
    return executeCmd(action.getCmd(), action.getExtra());
  }

  @Override
  public Serializable getExtra() {
    return getExtraInfo();
  }
}
