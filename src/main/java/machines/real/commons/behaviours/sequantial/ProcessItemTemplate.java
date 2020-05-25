package machines.real.commons.behaviours.sequantial;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.ProcessItem;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.commons.behaviours.simple.ProcessItemBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.ArmRequest;

/**
 * 处理加工行为模板.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public abstract class ProcessItemTemplate implements ProcessItem {

  @Override
  public Behaviour getBehaviour(RealMachineAgent realAgent, Buffer buffer, String armPwd) {
    return processBehaviour(realAgent, buffer, armPwd);
  }

  /**
   * 返回加工行为， 目前实现为简单加工行为，即加工后放下，如需复杂加工行为可覆盖此方法.
   *
   * @param realAgent agent
   * @param buffer 工位台
   * @param armPwd 机械手密码
   * @return 组装好的加工行为
   */
  protected abstract Behaviour processBehaviour(
      RealMachineAgent realAgent, Buffer buffer, String armPwd);

  protected void armMoveItem(SequentialBehaviour s, ArmRequest request, String armPwd) {
    s.addSubBehaviour(new SequentialCallForArm(request, armPwd,
        String.format("Move item from: %s to: %s, step: %d",
            request.getFrom(),
            request.getTo(),
            request.getStep())));
  }

  protected void processItem(SequentialBehaviour s, MiddleHal hal, Buffer buffer) {
    s.addSubBehaviour(new ProcessItemBehaviour(hal, buffer));
  }

  protected void finishedProcess(SequentialBehaviour s, RealMachineAgent realAgent, Buffer buffer) {
    s.addSubBehaviour(new ProcessFinishedBehaviour(realAgent, buffer));
  }
}
