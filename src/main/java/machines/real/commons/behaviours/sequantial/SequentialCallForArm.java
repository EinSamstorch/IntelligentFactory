package machines.real.commons.behaviours.sequantial;

import jade.core.behaviours.SequentialBehaviour;
import java.util.Random;
import machines.real.commons.behaviours.simple.SendRequstToArm;
import machines.real.commons.behaviours.simple.WaitResponse;
import machines.real.commons.request.ArmRequest;

/**
 * 向机械臂发出任务请求 并 等待任务执行成功.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SequentialCallForArm extends SequentialBehaviour {

  /**
   * 封装机械手搬运请求,透明内部通信细节.
   *
   * @param request  机械手搬运任务
   * @param password 机械手密码
   * @param infoStr  消息字符串,用于日志显示
   */
  public SequentialCallForArm(ArmRequest request, String password, String infoStr) {
    String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
    addSubBehaviour(new SendRequstToArm(request, password, conversationId, infoStr));
    addSubBehaviour(new WaitResponse(conversationId));
  }
}
