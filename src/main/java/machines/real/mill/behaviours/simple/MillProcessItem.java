package machines.real.mill.behaviours.simple;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.ProcessItemInterface;
import machines.real.commons.behaviours.sequantial.SequentialCallForArm;
import machines.real.commons.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.commons.behaviours.simple.ProcessItemBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.mill.MillAgent;
import machines.real.mill.MillHalImpl;

/**
 * 铣床加工工件行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillProcessItem implements ProcessItemInterface {
    private MillAgent millAgent;
    private MillHalImpl hal;
    private ArmrobotRequest request;
    private Buffer buffer;
    private String password;

    public MillProcessItem(MillAgent millAgent, Buffer buffer, String password) {
        this.millAgent = millAgent;
        this.buffer = buffer;
        this.password = password;

        this.hal = (MillHalImpl) millAgent.getHal();
        this.request = new ArmrobotRequest(
                String.valueOf(buffer.getIndex()),
                millAgent.getLocalName(),
                buffer.getWpInfo().getGoodsId());
    }

    @Override
    public Behaviour getBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();

        // 上料
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(millAgent,
                        request,
                        password,
                        String.format("Load item from buffer %d.", buffer.getIndex())));
        // 加工
        s.addSubBehaviour(ProcessItemBehaviour.getBehaviour(hal, buffer));
        // 下料
        ArmrobotRequest unloadRequest = ArmrobotRequest.unloadRequest(request);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(millAgent,
                        unloadRequest,
                        password,
                        String.format("Unload item from buffer %d.", buffer.getIndex())));
        // 下一步工序招标 更新机器和工位台状态
        s.addSubBehaviour(new ProcessFinishedBehaviour(millAgent, buffer));
        return s;
    }
}
