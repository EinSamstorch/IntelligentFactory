package machines.real.armrobot.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import machines.real.armrobot.ArmrobotAgent;
import machines.real.armrobot.ArmrobotHal;
import machines.real.commons.ArmrobotMoveItemRequest;

import java.util.Queue;

/**
 * executor move item request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MoveItemBehaviour extends CyclicBehaviour {
    private ArmrobotAgent armagent;
    private ArmrobotHal hal;
    private Queue<ACLMessage> requestQueue;

    public MoveItemBehaviour(ArmrobotAgent a) {
        super(a);
        this.armagent = a;
        hal = armagent.getHal();
        requestQueue = armagent.getRequestQueue();
    }

    @Override
    public void action() {
        ACLMessage msg = requestQueue.poll();
        if(msg != null) {
            ArmrobotMoveItemRequest request = null ;
            try {
                request = (ArmrobotMoveItemRequest) msg.getContentObject();
            } catch (UnreadableException e) {
                LoggerUtil.hal.error("Get Request Error.");
                e.printStackTrace();
            }
            if (request != null) {
                String from = request.getFrom();
                String to = request.getTo();
                String goodsid = request.getGoodsid();
                Integer step = request.getStep();
                if (hal.moveItem(from, to, goodsid, step)) {
                    LoggerUtil.hal.info(String.format("Move item from %s to %s, goodsid: %s, step: %d.",
                            from, to, goodsid, step));
                    // 搬运完成通知
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    armagent.send(reply);
                } else {
                    LoggerUtil.hal.error(String.format("Failed! Move item from %s to %s, goodsid: %s, step: %d.",
                            from, to, goodsid, step));
                }
            } else {
                LoggerUtil.hal.error("Request NEP Error.");
            }
        } else {
            block(1000);
        }


    }
}
