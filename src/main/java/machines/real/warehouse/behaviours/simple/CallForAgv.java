package machines.real.warehouse.behaviours.simple;

import commons.exceptions.MsgCreateFailedException;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.ItemMoveRequest;
import machines.real.commons.TransportRequest;
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
    private TransportRequest request;
    private boolean init = true;
    private boolean done = false;
    private String conversationId;

    public CallForAgv(Agent a, TransportRequest request) {
        super(a);
        this.request = request;
        conversationId = String.format("CALL_FOR_AGV_%d", new Random().nextInt());
    }

    @Override
    public void action() {
        if(init) {
            // 发送运输请求
            ACLMessage msg = null;
            try{
                msg = DFUtils.createRequestMsg(request);
                DFUtils.searchDF(myAgent, msg, DFServiceType.AGV);
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
            if(receive!=null) {
                if(receive.getPerformative() == ACLMessage.INFORM) {
                    // 已到达
                    done = true;
                    // 发送搬运请求
                    ItemMoveRequest request = new ItemMoveRequest(this.request.getWpInfo().getWarehousePosition());
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
