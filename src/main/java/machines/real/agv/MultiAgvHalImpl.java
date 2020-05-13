package machines.real.agv;

import java.io.Serializable;
import machines.real.agv.actions.MachineAction;
import machines.real.commons.hal.BaseHalImpl;

/**
 * 多AGV HAL的实现类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MultiAgvHalImpl extends BaseHalImpl implements MultiAgvHal {

  public MultiAgvHalImpl() {
    super();
  }

  public MultiAgvHalImpl(int port) {
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
