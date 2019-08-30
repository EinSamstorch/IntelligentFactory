package machines.real.arm.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.real.arm.ArmHal;
import machines.real.commons.request.ArmRequest;

/**
 * executor move item request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public final class MoveItemBehaviour extends CyclicBehaviour {
    private ArmHal hal;
    private final MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
            MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
    private static final int STATE_FREE = 0;
    private static final int STATE_BUSY = 1;
    private int state = STATE_FREE;
    private String language = "";
    private int oldStep = 0;

    public MoveItemBehaviour() {
        super();
    }

    public void setHal(ArmHal hal) {
        this.hal = hal;
    }

    @Override
    public void action() {
        switch (state) {
            case STATE_FREE:
                inStateFree();
                break;
            case STATE_BUSY:
                inStateBusy();
                break;
            default:
                throw new IllegalArgumentException("Illegal state: " + state);
        }
    }

    private void inStateFree() {
        ACLMessage msg = receiveMsg(mt);
        if (msg == null) {
            return;
        }
        ArmRequest request = getRequest(msg);
        String from = request.getFrom();
        String to = request.getTo();
        String goodsid = request.getGoodsid();
        int step = request.getStep();
        if (step > 1) {
            throw new IllegalArgumentException("Illegal step: " + step);
        }
        if (step == 1) {
            state = STATE_BUSY;
            language = request.toString();
            oldStep = step;
        }
        doTask(msg, from, to, goodsid, step);
    }

    private void inStateBusy() {
        MessageTemplate mt1 = MessageTemplate.and(mt,
                MessageTemplate.MatchLanguage(language));
        ACLMessage msg = receiveMsg(mt1);
        if (msg == null) {
            return;
        }
        ArmRequest request = getRequest(msg);
        String from = request.getFrom();
        String to = request.getTo();
        String goodsid = request.getGoodsid();
        int step = request.getStep();
        int endStep = -1;
        if (step == endStep) {
            state = STATE_FREE;
            language = "";

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            myAgent.send(reply);

            return;
        }
        if (step != oldStep + 1) {
            throw new IllegalArgumentException(String.format("Step: %d, oldStep: %d", step, oldStep));
        }
        oldStep = step;
        doTask(msg, from, to, goodsid, step);
    }

    /**
     * 执行搬运请求
     * @param msg 搬运请求的消息
     * @param from 起始地
     * @param to 目的地
     * @param goodsid 种类
     * @param step 步骤
     */
    private void doTask(ACLMessage msg, String from, String to, String goodsid, int step) {
        if (hal.moveItem(from, to, goodsid, step)) {
            LoggerUtil.hal.info(String.format("Move item from %s to %s, goodsid: %s, step: %d.",
                    from, to, goodsid, step));
            // 搬运完成通知
            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            myAgent.send(reply);
        } else {
            LoggerUtil.hal.error(String.format("Failed! Move item from %s to %s, goodsid: %s, step: %d.",
                    from, to, goodsid, step));
        }
    }

    /**
     * 接收 搬运请求的 ACLMessage
     *
     * @param mt 消息模板
     * @return 搬运请求消息, 无则null
     */
    private ACLMessage receiveMsg(MessageTemplate mt) {
        ACLMessage msg = myAgent.receive(mt);
        if (msg == null) {
            block();
            return null;
        }
        return msg;
    }

    /**
     * 获取搬运请求
     *
     * @param msg 搬运请求消息
     * @return 搬运请求, 不为null
     */
    private ArmRequest getRequest(ACLMessage msg) {
        ArmRequest request = null;
        try {
            request = (ArmRequest) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        if (request == null) {
            throw new IllegalArgumentException("Request NEP Error.");
        }
        return request;
    }
}
