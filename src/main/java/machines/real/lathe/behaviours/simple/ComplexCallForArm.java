package machines.real.lathe.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.behaviours.simple.SimpleProcessItemBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.lathe.LatheAgent;
import machines.real.lathe.LatheHal;

import java.util.Random;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ComplexCallForArm {
    private LatheAgent latheAgent;
    private LatheHal hal;
    private ArmrobotRequest request;
    private Buffer buffer;
    private String password;

    public ComplexCallForArm(LatheAgent latheAgent, ArmrobotRequest request, Buffer buffer, String password) {
        this.latheAgent = latheAgent;
        this.hal = (LatheHal) latheAgent.getHal();
        this.request = request;
        this.buffer = buffer;
        this.password = password;
    }

    public SequentialBehaviour loadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();

        // 复制request
        ArmrobotRequest request = new ArmrobotRequest(this.request);

        // 装夹 工步1 送料到车床
        ArmrobotRequest request1 = new ArmrobotRequest(request);
        request1.setStep(1);
        String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request1, password, conversationId, "Load item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        // 车床夹紧
        s.addSubBehaviour(new GrabItemBehaviour(hal));
        // 装夹工步2 机械手松开离开
        ArmrobotRequest request2 = new ArmrobotRequest(request);
        request2.setStep(2);
        conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request2, password, conversationId, "Release item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));

        // 机床加工
        Behaviour afterProcess = reverseLoadItemBehaviour();
        s.addSubBehaviour(new SimpleProcessItemBehaviour(latheAgent, buffer, afterProcess));
        LoggerUtil.hal.info(String.format("Buffer %d load on machine.", buffer.getIndex()));

        return s;
    }

    private SequentialBehaviour reverseLoadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        // 修改request
        ArmrobotRequest request = new ArmrobotRequest(this.request.getTo(),
                this.request.getTo(),
                this.request.getGoodsid(),
                0);


        // 装夹 工步1 机械手抓住工件
        ArmrobotRequest request1 = new ArmrobotRequest(request);
        request1.setStep(1);
        String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request1, password, conversationId, "Grab item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        // 车床松开
        s.addSubBehaviour(new ReleaseItemBehaviour(hal));

        // 装夹工步2 机械手掉转工件,机床夹紧
        ArmrobotRequest request2 = new ArmrobotRequest(request);
        request2.setStep(2);
        conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request2, password, conversationId, "Reverse item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        // 机床夹紧
        s.addSubBehaviour(new GrabItemBehaviour(hal));

        // 装夹工步3 机械手离开 机床加工
        ArmrobotRequest request3 = new ArmrobotRequest(request);
        request3.setStep(3);
        conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request3, password, conversationId, "Release item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        // 机床加工
        Behaviour afterProcess = unloadItemBehaviour();
        s.addSubBehaviour(new SimpleProcessItemBehaviour(latheAgent, buffer, afterProcess));

        return s;
    }

    private SequentialBehaviour unloadItemBehaviour() {
        SequentialBehaviour s = new SequentialBehaviour();
        // 修改request
        ArmrobotRequest request = new ArmrobotRequest(this.request.getTo(),
                this.request.getFrom(),
                this.request.getGoodsid(),
                0);

        // 装夹 工步1 机械手抓住工件
        ArmrobotRequest request1 = new ArmrobotRequest(request);
        request1.setStep(1);
        String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request1, password, conversationId, "Grab item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        // 车床松开
        s.addSubBehaviour(new ReleaseItemBehaviour(hal));

        // 装夹工步2 机械手带着工件离开
        ArmrobotRequest request2 = new ArmrobotRequest(request);
        request2.setStep(2);
        conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        s.addSubBehaviour(new SendRequstToArm(latheAgent, request2, password, conversationId, "Unload item."));
        // 等待机械手就位
        s.addSubBehaviour(new WaitForArmInform(latheAgent, conversationId));
        s.addSubBehaviour(new ProcessFinishedBehaviour(latheAgent, buffer));

        return s;
    }


}
