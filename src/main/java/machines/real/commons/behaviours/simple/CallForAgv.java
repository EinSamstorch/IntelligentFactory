package machines.real.commons.behaviours.simple;


import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.buffer.BufferState;
import machines.real.commons.request.AgvRequest;
import machines.real.commons.buffer.Buffer;

import java.util.Random;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CallForAgv extends SimpleBehaviour {
    private AgvRequest request;
    private Buffer buffer;
    private boolean init = true;
    private boolean isDone = false;
    private String conversationId;

    public CallForAgv(Agent a, AgvRequest request, Buffer buffer) {
        super(a);
        this.request = request;
        this.buffer = buffer;
        conversationId = String.format("CALL_FOR_AGV_%d", new Random().nextInt());
    }

    @Override
    public void action() {
        if(init) {
            // 发送运输请求
            initSendRequest();
        } else {
            // 等待通知完成
            waitForInform();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }

    private void initSendRequest() {
        ACLMessage msg = null;
        try {
            msg = DFUtils.createRequestMsg(request);
            DFUtils.searchDF(myAgent, msg, DFServiceType.AGV);
            // 发送运输请求
            msg.setConversationId(conversationId);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        init = false;
        LoggerUtil.hal.debug("Call for agv.");
    }

    private void waitForInform() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        ACLMessage receive = myAgent.receive(mt);
        if(receive != null) {
            if(receive.getPerformative() == ACLMessage.INFORM) {
                // 已到达
                isDone = true;
                buffer.getBufferState().setState(BufferState.STATE_WAITING);
                LoggerUtil.agent.info(String.format("Workpiece arrived at %d", buffer.getIndex()));
            } else {
                LoggerUtil.agent.error("Performative error.");
            }
        } else {
            block();
        }
    }
}
