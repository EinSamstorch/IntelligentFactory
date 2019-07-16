package machines.real.mill.behaviours.simple;

import commons.Buffer;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.TransportRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CallForAgv extends SimpleBehaviour {
    private TransportRequest request;
    private Buffer buffer;
    private boolean init = true;
    private boolean isDone = false;
    private String conversationId;

    public CallForAgv(Agent a, TransportRequest request, Buffer buffer) {
        super(a);
        this.request = request;
        this.buffer = buffer;
    }

    @Override
    public void action() {
        if(init) {
            // 发送运输请求
            ACLMessage msg = null;
            try {
                msg = DFUtils.createRequestMsg(request);
                DFUtils.searchDF(myAgent, msg, DFServiceType.AGV);
                // 发送运输请求
                conversationId = msg.getConversationId();
                myAgent.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            init = false;
        } else {
            MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
            ACLMessage receive = myAgent.receive(mt);
            if(receive != null) {
                if(receive.getPerformative() == ACLMessage.INFORM) {
                    // 已到达
                    isDone = true;
                    buffer.setArrived(true);
                } else {
                    LoggerUtil.agent.error("Performative error.");
                }
            } else {
                block();
            }
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
