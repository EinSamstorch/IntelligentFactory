package machines.real.lathe.behaviours.simple;

import commons.order.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;
import machines.real.lathe.LatheAgent;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessFinishedBehaviour extends OneShotBehaviour {
    private boolean isDone = false;
    private LatheAgent latheAgent;
    private Buffer buffer;

    public ProcessFinishedBehaviour(LatheAgent latheAgent, Buffer buffer) {
        super(latheAgent);
        this.latheAgent = latheAgent;
        this.buffer = buffer;
    }

    @Override
    public void action() {
        // 复位机床状态和工位台状态
        latheAgent.getMachineState().resetBusy();
        buffer.getBufferState().setState(BufferState.STATE_PROCESSED);
        // 提交给 worker 进行 下一步招标以及状态更新
        sendToWorker(buffer.getWpInfo());
        LoggerUtil.hal.info(String.format("Buffer %d unload from machine", buffer.getIndex()));
    }


    private void sendToWorker(WorkpieceInfo wpInfo) {
        try {
            ACLMessage msg = DFUtils.createRequestMsg(wpInfo);
            DFUtils.searchDF(myAgent, msg, DFServiceType.WORKER);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
