package machines.real.commons.behaviours.sequential;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.agent.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.ArmRequest;

/**
 * 简单加工工件行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class SimpleProcessItem extends ProcessItemTemplate {

  private Behaviour simpleProcess(RealMachineAgent realAgent, Buffer buffer, String armPwd) {
    MiddleHal hal = realAgent.getHal();
    ArmRequest request =
        new ArmRequest(
            String.valueOf(buffer.getIndex()), realAgent.getLocalName(), buffer.getWpInfo());

    SequentialBehaviour s = new SequentialBehaviour();
    // 上料
    armMoveItem(s, request, armPwd);
    // 加工
    processItem(s, hal, buffer);
    // 下料
    armMoveItem(s, ArmRequest.unloadRequest(request), armPwd);
    // 收尾工作
    finishedProcess(s, realAgent, buffer);
    return s;
  }

  @Override
  protected Behaviour processBehaviour(RealMachineAgent realAgent, Buffer buffer, String armPwd) {
    return simpleProcess(realAgent, buffer, armPwd);
  }
}
