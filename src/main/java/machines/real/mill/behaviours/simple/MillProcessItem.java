package machines.real.mill.behaviours.simple;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.ProcessItem;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.sequantial.SequentialCallForArm;
import machines.real.commons.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.commons.behaviours.simple.ProcessItemBehaviour2;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.ArmRequest;

/**
 * 铣床加工工件行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillProcessItem implements ProcessItem {

  @Override
  public Behaviour getBehaviour(RealMachineAgent realAgent, Buffer buffer, String armPwd) {

    MiddleHal hal = realAgent.getHal();
    ArmRequest request = new ArmRequest(
        String.valueOf(buffer.getIndex()),
        realAgent.getLocalName(),
        buffer.getWpInfo().getGoodsId());
    SequentialBehaviour s = new SequentialBehaviour();

    // 上料
    s.addSubBehaviour(new SequentialCallForArm(request, armPwd,
        "Load item from buffer: " + buffer.getIndex()));
    // 加工
    s.addSubBehaviour(new ProcessItemBehaviour2(hal, buffer));

    // 下料
    ArmRequest unloadRequest = ArmRequest.unloadRequest(request);
    s.addSubBehaviour(new SequentialCallForArm(unloadRequest, armPwd,
        "Unload item from buffer: " + buffer.getIndex()));
    // 下一步工序招标 更新机器和工位台状态
    s.addSubBehaviour(new ProcessFinishedBehaviour(realAgent, buffer));
    return s;
  }
}
