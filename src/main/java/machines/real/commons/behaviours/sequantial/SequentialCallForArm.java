package machines.real.commons.behaviours.sequantial;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import java.util.Random;
import machines.real.commons.behaviours.simple.SendRequstToArm;
import machines.real.commons.behaviours.simple.WaitForArmInform;
import machines.real.commons.request.ArmRequest;

/**
 * 向机械臂发出任务请求 并 等待任务执行成功.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SequentialCallForArm {

  private SequentialCallForArm() {
  }

  public static SequentialBehaviour getBehaviour(
      Agent agent, ArmRequest request, String password, String infoStr) {
    SequentialBehaviour s = new SequentialBehaviour();

    String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
    s.addSubBehaviour(new SendRequstToArm(agent, request, password, conversationId, infoStr));
    s.addSubBehaviour(new WaitForArmInform(agent, conversationId));
    return s;
  }
}
