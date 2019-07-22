package machines.real.commons.behaviours.cycle;


import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.simple.SimpleCallForArm;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.lathe.LatheAgent;
import machines.real.lathe.behaviours.simple.ComplexCallForArm;

/**
 * 挑选工件装载入机床
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoadItemBehaviour extends CyclicBehaviour {
    public static final int SIMPLE = 1;
    public static final int COMPLEX = 2;
    private Integer type;
    private RealMachineAgent machineAgent;

    public LoadItemBehaviour(RealMachineAgent machineAgent, int type) {
        super(machineAgent);
        this.machineAgent = machineAgent;
        this.type = type;
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
                String from = String.valueOf(buffer.getIndex());
                String to = machineAgent.getLocalName();
                String goodsId = buffer.getWpInfo().getGoodsId();
                ArmrobotRequest request = new ArmrobotRequest(from, to, goodsId);
                String password = machineAgent.getArmPwd();
                if (type == SIMPLE) {
                    machineAgent.addBehaviour(new SimpleCallForArm(machineAgent, request, buffer,
                            password, SimpleCallForArm.LOAD));
                } else if (type == COMPLEX) {
                    Behaviour b = new ComplexCallForArm((LatheAgent) machineAgent, request, buffer, password)
                            .loadItemBehaviour();
                    machineAgent.addBehaviour(b);
                }
            }
        }
        block(1000);

    }
}
