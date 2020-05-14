package machines.real.commons.hal;

import java.io.Serializable;
import machines.real.commons.actions.MachineAction;

/**
 * 临时增加一层Hal.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface MiddleHal extends BaseHal {

  /**
   * 执行动作.
   *
   * @param action 具体动作
   * @return 成功与否
   */
  boolean executeAction(MachineAction action);

  /**
   * 获取额外信息.
   *
   * @return 根据协议返回额外信息，若无则返回空字符串
   */
  Serializable getExtra();
}
