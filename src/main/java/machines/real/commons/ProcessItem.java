package machines.real.commons;

import jade.core.behaviours.Behaviour;
import machines.real.commons.buffer.Buffer;

/**
 * 定义处理工件的动作集合，一般是 SequentialBehaviour.
 * @author junfeng
 */

public interface ProcessItem {

  /**
   * 定义处理工件的动作集合，一般是 SequentialBehaviour.
   *
   * @param realAgent agent
   * @param buffer 工位台
   * @param armPwd 机械手密码
   * @return 加工动作
   */
  Behaviour getBehaviour(RealMachineAgent realAgent, Buffer buffer, String armPwd);
}
