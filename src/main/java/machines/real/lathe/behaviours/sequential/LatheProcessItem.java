package machines.real.lathe.behaviours.sequential;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.agent.RealMachineAgent;
import machines.real.commons.behaviours.sequential.ProcessItemTemplate;
import machines.real.commons.behaviours.simple.ActionExecutor;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.ArmRequest;
import machines.real.commons.request.LatheRequest;
import machines.real.lathe.actions.GrabReleaseAction;

/**
 * 车床加工动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class LatheProcessItem extends ProcessItemTemplate {

  @Override
  protected Behaviour processBehaviour(RealMachineAgent realAgent, Buffer buffer, String armPwd) {
    return latheProcessItem(realAgent, buffer, armPwd);
  }

  private Behaviour latheProcessItem(RealMachineAgent realAgent, Buffer buffer, String armPwd) {
    SequentialBehaviour s = new SequentialBehaviour();
    ArmRequest request =
        new ArmRequest(
            String.valueOf(buffer.getIndex()), realAgent.getLocalName(), buffer.getWpInfo());
    MiddleHal hal = realAgent.getHal();
    // 上料加工1
    s.addSubBehaviour(loadAndProcess(request, buffer, hal, armPwd));
    // 掉头装夹加工
    ArmRequest reverseRequest = ArmRequest.reverseRequest(request);
    s.addSubBehaviour(reverseAndProcess(reverseRequest, buffer, hal, armPwd));
    // 卸料
    ArmRequest unloadRequest = ArmRequest.unloadRequest(request);
    s.addSubBehaviour(unload(unloadRequest, buffer, hal, armPwd));
    // 完成后续动作
    finishedProcess(s, realAgent, buffer);
    return s;
  }

  @SuppressWarnings("Duplicates")
  private Behaviour loadAndProcess(ArmRequest step0, Buffer buffer, MiddleHal hal, String armPwd) {
    SequentialBehaviour s = new SequentialBehaviour();
    ArmRequest step1 = ArmRequest.nextStep(step0);
    // step1 机械手送料到车床
    armMoveItem(s, step1, armPwd);
    // 车床夹紧
    LatheRequest latheRequest = new LatheRequest(true, buffer.getWpInfo());
    latheGrabItem(s, hal, latheRequest);
    // step2 机械手松手离开
    ArmRequest step2 = ArmRequest.nextStep(step1);
    armMoveItem(s, step2, armPwd);
    // 结束动作组
    armMoveItem(s, ArmRequest.endStep(step2), armPwd);
    // 启动加工
    processItem(s, hal, buffer);
    return s;
  }

  private Behaviour reverseAndProcess(
      ArmRequest step0, Buffer buffer, MiddleHal hal, String armPwd) {
    SequentialBehaviour s = new SequentialBehaviour();
    // 抓住工件
    ArmRequest step1 = ArmRequest.nextStep(step0);
    armMoveItem(s, step1, armPwd);
    // 车床松开
    LatheRequest latheRequest = new LatheRequest(false, buffer.getWpInfo());
    latheGrabItem(s, hal, latheRequest);
    // 掉头装夹
    ArmRequest step2 = ArmRequest.nextStep(step1);
    armMoveItem(s, step2, armPwd);
    // 车床夹紧
    LatheRequest latheRequest2 = new LatheRequest(true, buffer.getWpInfo());
    latheGrabItem(s, hal, latheRequest2);
    // 机械手离开
    ArmRequest step3 = ArmRequest.nextStep(step2);
    armMoveItem(s, step3, armPwd);
    // 完成动作组
    armMoveItem(s, ArmRequest.endStep(step3), armPwd);
    // 启动加工
    processItem(s, hal, buffer);
    return s;
  }

  private Behaviour unload(ArmRequest step0, Buffer buffer, MiddleHal hal, String armPwd) {
    SequentialBehaviour s = new SequentialBehaviour();
    // 抓住工件
    ArmRequest step1 = ArmRequest.nextStep(step0);
    armMoveItem(s, step1, armPwd);
    // 车床松开
    LatheRequest latheRequest = new LatheRequest(false, buffer.getWpInfo());
    latheGrabItem(s, hal, latheRequest);
    // 放回工位台
    ArmRequest step2 = ArmRequest.nextStep(step1);
    armMoveItem(s, step2, armPwd);
    // 结束动作组
    armMoveItem(s, ArmRequest.endStep(step2), armPwd);
    return s;
  }

  private void latheGrabItem(SequentialBehaviour s, MiddleHal hal, LatheRequest latheRequest) {
    s.addSubBehaviour(new ActionExecutor(new GrabReleaseAction(latheRequest), hal));
  }
}
