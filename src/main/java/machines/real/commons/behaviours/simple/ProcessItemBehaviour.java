package machines.real.commons.behaviours.simple;

import commons.order.WorkpieceStatus;
import jade.core.behaviours.Behaviour;
import java.lang.reflect.Method;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MachineHal;

/**
 * 执行加工动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessItemBehaviour {

  /**
   * 获取加工物品的behaviour对象.
   *
   * @param hal 加工方的hal
   * @param buffer 货物所处buffer
   * @return 加工行为对象
   */
  public static Behaviour getBehaviour(MachineHal hal, Buffer buffer) {
    Method processMethod = null;
    try {
      processMethod = hal.getClass().getMethod("process", WorkpieceStatus.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return new ExecuteActionBehaviour(hal, processMethod, buffer, "Process item.");
  }
}
