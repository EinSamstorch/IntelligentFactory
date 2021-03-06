package machines.real.warehouse.behaviours.simple;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.request.AgvRequest;
import machines.real.commons.request.WarehouseRequest;
import machines.real.warehouse.WarehouseAgent;

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
    private boolean init = true;
    private boolean done = false;
    private String conversationId;

    public CallForAgv(Agent a, AgvRequest request) {
        super(a);
        this.request = request;
        conversationId = String.format("CALL_FOR_AGV_%d", new Random().nextInt());
    }

    @Override
    public void action() {
        if (init) {
            // 发送运输请求
            ACLMessage msg = null;
            try {
                msg = DfUtils.createRequestMsg(request);
                DfUtils.searchDf(myAgent, msg, DfServiceType.AGV);
                msg.setConversationId(conversationId);
                myAgent.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            init = false;
            LoggerUtil.agent.debug("Call for agv.");
        } else {
            MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
            ACLMessage receive = myAgent.receive(mt);
            if (receive != null) {
                if (receive.getPerformative() == ACLMessage.INFORM) {
                    // 已到达
                    done = true;
                    // 发送搬运请求
                    WarehouseRequest request = new WarehouseRequest(this.request.getWpInfo().getWarehousePosition());
                    myAgent.addBehaviour(new ItemImportBehaviour((WarehouseAgent) myAgent, request));

                    LoggerUtil.agent.info("Workpiece arrived at import location");
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
        return done;
    }
}
