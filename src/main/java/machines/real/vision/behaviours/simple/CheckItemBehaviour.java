package machines.real.vision.behaviours.simple;

import commons.order.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;
import machines.real.vision.VisionAgent;
import machines.real.vision.VisionHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CheckItemBehaviour extends OneShotBehaviour {
    private Buffer buffer;
    private VisionHal hal;

    public CheckItemBehaviour(VisionAgent visionAgent, Buffer buffer) {
        super(visionAgent);
        this.buffer = buffer;
        this.hal = visionAgent.getVisionHal();
    }

    @Override
    public void action() {
        buffer.getBufferState().setState(BufferState.STATE_PROCESSING);
        LoggerUtil.hal.info(String.format("Start processing item from buffer %d", buffer.getIndex()));
        WorkpieceInfo wpInfo = buffer.getWpInfo();
        String result = hal.check(wpInfo.getGoodsId());
        if(result != null) {
            LoggerUtil.hal.info(String.format("Item orderid:%s, workpieceid:%s check. Values: %s",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId(), result));
//            wpInfo.setNextProcessStep();
//            String from = myAgent.getLocalName();
//            String to = String.valueOf(buffer.getIndex());
        } else {
            LoggerUtil.hal.error(String.format("Failed! Item orderid:%s, workpieceid:%s check.",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId()));

        }

    }
}
