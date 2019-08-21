package machines.real.lathe.behaviours.sequential;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.ProcessItemInterface;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.sequantial.SequentialCallForArm;
import machines.real.commons.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.commons.behaviours.simple.ProcessItemBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.lathe.LatheHalImpl;
import machines.real.lathe.behaviours.simple.LatheHalBehaviours;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheProcessItem implements ProcessItemInterface {
    private RealMachineAgent latheAgent;
    private LatheHalImpl hal;
    private ArmrobotRequest request;
    private Buffer buffer;
    private String password;

    public LatheProcessItem(RealMachineAgent latheAgent, Buffer buffer, String password) {
        this.latheAgent = latheAgent;
        this.hal = (LatheHalImpl) latheAgent.getHal();
        this.request = new ArmrobotRequest(
                String.valueOf(buffer.getIndex()),
                latheAgent.getLocalName(),
                buffer.getWpInfo().getGoodsId());
        this.buffer = buffer;
        this.password = password;
    }

    @Override
    public Behaviour getBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        s.addSubBehaviour(loadItemBehaviour());
        s.addSubBehaviour(reverseLoadItemBehaviour());
        s.addSubBehaviour(unloadItemBehaviour());
        return s;
    }

    private SequentialBehaviour loadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();

        // 装夹 工步1 送料到车床
        ArmrobotRequest request1 = ArmrobotRequest.nextStep(this.request);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent,
                        request1,
                        password,
                        String.format("Load item from buffer %d.", buffer.getIndex())));
        // 车床夹紧
        s.addSubBehaviour(LatheHalBehaviours.grabItem(hal));


        // 装夹工步2 机械手松开离开
        ArmrobotRequest request2 = ArmrobotRequest.nextStep(request1);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent,
                        request2,
                        password,
                        "Release item"));
        // 机床加工
        s.addSubBehaviour(ProcessItemBehaviour.getBehaviour(hal, buffer));

        return s;
    }

    private SequentialBehaviour reverseLoadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        // 修改request
        ArmrobotRequest reverseRequest = ArmrobotRequest.reverseRequest(request);


        // 装夹 工步1 机械手抓住工件
        ArmrobotRequest request1 = ArmrobotRequest.nextStep(reverseRequest);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent, request1, password, "Grab item."));
        // 车床松开
        s.addSubBehaviour(LatheHalBehaviours.releaseItem(hal));

        // 装夹工步2 机械手掉转工件,机床夹紧
        ArmrobotRequest request2 = ArmrobotRequest.nextStep(request1);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent,
                        request2,
                        password,
                        "Reverse item."));
        // 机床夹紧
        s.addSubBehaviour(LatheHalBehaviours.grabItem(hal));

        // 装夹工步3 机械手离开 机床加工
        ArmrobotRequest request3 = ArmrobotRequest.nextStep(request2);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent, request3, password, "Release item."));
        // 机床加工
        s.addSubBehaviour(ProcessItemBehaviour.getBehaviour(hal, buffer));
        return s;
    }

    private SequentialBehaviour unloadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        // 修改request
        ArmrobotRequest unloadRequest = ArmrobotRequest.unloadRequest(this.request);

        // 装夹 工步1 机械手抓住工件
        ArmrobotRequest request1 = ArmrobotRequest.nextStep(unloadRequest);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent, request1, password, "Grab item."));
        // 车床松开
        s.addSubBehaviour(LatheHalBehaviours.releaseItem(hal));

        // 装夹工步2 机械手带着工件离开
        ArmrobotRequest request2 = ArmrobotRequest.nextStep(request1);
        s.addSubBehaviour(SequentialCallForArm
                .getBehaviour(latheAgent,
                        request2,
                        password,
                        String.format("Unload item to buffer %d.", buffer.getIndex())));
        s.addSubBehaviour(new ProcessFinishedBehaviour(latheAgent, buffer));

        return s;
    }


}
