package machines.real.lathe.behaviours.simple;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WaitForArmInform extends SimpleBehaviour {
    private boolean isDone = false;
    private String conversationId;

    public WaitForArmInform(Agent a, String conversationId) {
        super(a);
        this.conversationId = conversationId;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
            if (receive.getPerformative() == ACLMessage.INFORM) {
                isDone = true;
            }
        } else {
            block();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
