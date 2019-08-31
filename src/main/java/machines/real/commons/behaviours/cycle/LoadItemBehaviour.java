package machines.real.commons.behaviours.cycle;


import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import machines.real.commons.ProcessItem;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;

/**
 * 挑选工件装载入机床
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoadItemBehaviour extends CyclicBehaviour {
    private RealMachineAgent machineAgent;
    private ProcessItem processItem;

    public LoadItemBehaviour(RealMachineAgent machineAgent, ProcessItem processItem) {
        this.machineAgent = machineAgent;
        this.processItem = processItem;
    }

    @Override
    public void action() {
        // 机床空闲
        if (!machineAgent.getMachineState().isBusy()) {
            // 获取等待加工的buffer
            Buffer buffer = machineAgent.getBufferManger().getWaitingBuffer();
            if (buffer != null) {
                // 请求机械手 装载入机床
                machineAgent.getMachineState().setBusy();
                String password = machineAgent.getArmPwd();
                Behaviour b = processItem.getBehaviour(machineAgent, buffer, password);
                myAgent.addBehaviour(b);
            }
        }
        block(1000);
    }
}
