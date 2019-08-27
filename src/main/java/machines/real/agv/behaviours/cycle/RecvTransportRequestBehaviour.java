package machines.real.agv.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.agv.AgvAgent;

import java.util.Queue;

/**
 * 接收运输任务请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RecvTransportRequestBehaviour extends CyclicBehaviour {
    private Queue<ACLMessage> queue;

    public void setQueue(Queue<ACLMessage> queue) {
        this.queue = queue;
    }

    public RecvTransportRequestBehaviour() {
        super();
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
