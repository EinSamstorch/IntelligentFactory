package machines.real.vision.behaviours.sequential;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.ProcessItemInterface;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.sequantial.SequentialCallForArm;
import machines.real.commons.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.commons.behaviours.simple.ProcessItemBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MachineHal;
import machines.real.commons.request.ArmrobotRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionProcessItem implements ProcessItemInterface {
    private RealMachineAgent visionAgent;
    private MachineHal hal;
    private ArmrobotRequest request;
    private Buffer buffer;
    private String password;

    public VisionProcessItem(RealMachineAgent visionAgent, Buffer buffer, String password) {
        this.visionAgent = visionAgent;
        this.buffer = buffer;
        this.password = password;

        this.hal = visionAgent.getHal();
        this.request = new ArmrobotRequest(
                String.valueOf(buffer.getIndex()),
                visionAgent.getLocalName(),
                buffer.getWpInfo().getGoodsId());
    }

    @Override
    public Behaviour getBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        // 上料
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(visionAgent,
                        request,
                        password,
                        String.format("Load item from buffer %d.", buffer.getIndex())));

        // 检查
        s.addSubBehaviour(ProcessItemBehaviour.getBehaviour(hal, buffer));
        // 下料
        ArmrobotRequest unloadRequest = ArmrobotRequest.unloadRequest(request);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(visionAgent,
                        unloadRequest,
                        password,
                        String.format("Unload item from buffer %d.", buffer.getIndex())));
        // 下一步工序 更新机床和工位台状态
        s.addSubBehaviour(new ProcessFinishedBehaviour(visionAgent, buffer));
        return s;
    }
}
