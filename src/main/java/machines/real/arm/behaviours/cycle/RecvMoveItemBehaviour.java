package machines.real.arm.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.arm.ArmAgent;

import java.util.Queue;

/**
 * receive transport request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RecvMoveItemBehaviour extends CyclicBehaviour {
    private Queue<ACLMessage> queue;

    public RecvMoveItemBehaviour(ArmAgent armAgent) {
        super(armAgent);
        this.queue = armAgent.getRequestQueue();
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
            queue.offer(receive);
        } else {
            block();
        }
    }
}

