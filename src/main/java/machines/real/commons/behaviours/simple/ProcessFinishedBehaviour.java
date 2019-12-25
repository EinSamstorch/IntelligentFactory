package machines.real.commons.behaviours.simple;

import commons.order.WorkpieceStatus;
import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;

/**
 * 加工完成,转交给worker进行下一工序招投标.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessFinishedBehaviour extends OneShotBehaviour {

  private RealMachineAgent machineAgent;
  private Buffer buffer;

  /**
   * 加工完成,转交给worker进行下一工序招投标.
   *
   * @param machineAgent 加工方agent
   * @param buffer       工位台对象
   */
  public ProcessFinishedBehaviour(RealMachineAgent machineAgent, Buffer buffer) {
    super(machineAgent);
    this.machineAgent = machineAgent;
    this.buffer = buffer;
  }

  @Override
  public void action() {
    // 复位机床状态和工位台状态
    machineAgent.getMachineState().resetBusy();
    buffer.getBufferState().setState(BufferState.STATE_PROCESSED);
    // 提交给 worker 进行 下一步招标以及状态更新
    sendToWorker(buffer.getWpInfo());
    LoggerUtil.hal.info(String.format("Buffer %d unload from machine", buffer.getIndex()));
  }


  private void sendToWorker(WorkpieceStatus wpInfo) {
    try {
      ACLMessage msg = DfUtils.createRequestMsg(wpInfo);
      DfUtils.searchDf(myAgent, msg, DfServiceType.WORKER);
      myAgent.send(msg);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
