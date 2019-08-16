package machines.real.agv.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.agv.AgvAgent;

/**
 * 接收运输任务请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RecvTransportRequestBehaviour extends CyclicBehaviour {
    private AgvAgent aagent;

    public RecvTransportRequestBehaviour(AgvAgent aagent) {
        super(aagent);
        this.aagent = aagent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        ACLMessage receive = aagent.receive(mt);
        if (receive != null) {
            aagent.getTransportRequestQueue().offer(receive);
        } else {
            block();
        }
    }
}
