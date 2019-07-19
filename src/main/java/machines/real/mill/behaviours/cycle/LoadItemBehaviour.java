package machines.real.mill.behaviours.cycle;


import jade.core.behaviours.CyclicBehaviour;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.behaviours.simple.SimpleCallForArm;

/**
 * 挑选工件装载入机床
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoadItemBehaviour extends CyclicBehaviour {
    private RealMachineAgent machineAgent;

    public LoadItemBehaviour(RealMachineAgent machineAgent) {
        super(machineAgent);
        this.machineAgent = machineAgent;
    }

    @Override
    public void action() {
        // 机床空闲
        if(!machineAgent.getMachineState().isBusy()){
            // 获取等待加工的buffer
            Buffer buffer = machineAgent.getBufferManger().getWaitingBuffer();
            if(buffer != null) {
                // 请求机械手 装载入机床
                machineAgent.getMachineState().setBusy();
                String from = String.valueOf(buffer.getIndex());
                String to = machineAgent.getLocalName();
                String goodsId = buffer.getWpInfo().getGoodsId();
                ArmrobotRequest request = new ArmrobotRequest(from, to, goodsId);
                machineAgent.addBehaviour(new SimpleCallForArm(machineAgent, request, buffer,
                        machineAgent.getArmPwd(), SimpleCallForArm.LOAD));
            }
        } else {
            block(1000);
        }
    }
}
