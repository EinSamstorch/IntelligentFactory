package machines.real.mill.behaviours.simple;

import commons.Buffer;
import commons.Workpiece;
import commons.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.ArmrobotMoveItemRequest;
import machines.real.mill.MillAgent;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CallForArm extends SimpleBehaviour {
    public static final String LOAD = "load";
    public static final String UNLOAD = "unload";
    private ArmrobotMoveItemRequest request;
    private Boolean init = true;
    private Boolean isDone = false;
    private String conversationId;
    private String password;
    private Buffer buffer;
    private String operation;
    private MillAgent magent;

    public CallForArm(MillAgent magent, ArmrobotMoveItemRequest request, Buffer buffer, String password, String operation) {
        super(magent);
        this.request = request;
        this.password = password;
        this.buffer = buffer;
        this.operation = operation;
    }

    @Override
    public void action() {
        if(init) {
            // 发送移动货物请求
            ACLMessage msg = null;
            try{
                msg = DFUtils.createRequestMsg(request);
                DFUtils.searchDF(myAgent, msg, DFServiceType.ARMROBOT, password);

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
                    isDone = true;
                    switch (operation) {
                        case LOAD:
                            buffer.setOnMachine(true);
                            break;
                        case UNLOAD:
                            magent.setBusy(false);
                            buffer.setOnMachine(false);
                            buffer.setProcessed(true);
                            sendToWorker(buffer.getWpInfo());
                            break;
                    }
                }
            } else {
                block();
            }
        }
    }

    private void sendToWorker(WorkpieceInfo wpInfo) {
        try {
            ACLMessage msg = DFUtils.createRequestMsg(wpInfo);
            msg = DFUtils.searchDF(myAgent, msg, DFServiceType.WORKER);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean done() {
        return isDone;
    }
}
