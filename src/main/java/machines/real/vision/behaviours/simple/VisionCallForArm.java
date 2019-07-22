package machines.real.vision.behaviours.simple;

import jade.core.behaviours.SequentialBehaviour;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.request.ArmrobotRequest;
import machines.real.lathe.behaviours.simple.ProcessFinishedBehaviour;
import machines.real.lathe.behaviours.simple.SendRequstToArm;
import machines.real.lathe.behaviours.simple.WaitForArmInform;
import machines.real.vision.VisionAgent;

import java.util.Random;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionCallForArm {
    public static SequentialBehaviour loadItemBehaviour(
            VisionAgent visionAgent, ArmrobotRequest request, Buffer buffer, String password) {
        SequentialBehaviour s = new SequentialBehaviour();

        String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
        // 机械手上料
        s.addSubBehaviour(new SendRequstToArm(visionAgent, request, password, conversationId));
        // 等待上料完成
        s.addSubBehaviour(new WaitForArmInform(visionAgent, conversationId));
        // 检测
        s.addSubBehaviour(new CheckItemBehaviour(visionAgent, buffer));

        ArmrobotRequest unloadRequest = new ArmrobotRequest(
                request.getTo(),
                request.getFrom(),
                request.getGoodsid(),
                request.getStep()
        );
        // 机械手下料
        s.addSubBehaviour(new SendRequstToArm(visionAgent, unloadRequest, password, conversationId));
        // 等待下料完成
        s.addSubBehaviour(new WaitForArmInform(visionAgent, conversationId));
        // 完工 等待招投标
        s.addSubBehaviour(new ProcessFinishedBehaviour(visionAgent, buffer));
        return s;
    }
}
