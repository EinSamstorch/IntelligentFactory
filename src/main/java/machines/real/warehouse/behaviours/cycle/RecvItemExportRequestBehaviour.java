package machines.real.warehouse.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.warehouse.WarehouseAgent;

import java.util.Queue;

/**
 * receieve item export request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RecvItemExportRequestBehaviour extends CyclicBehaviour {
    private Queue<ACLMessage> queue;

    public RecvItemExportRequestBehaviour(WarehouseAgent warehouseAgent) {
        super(warehouseAgent);
        queue = warehouseAgent.getExportQueue();
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
