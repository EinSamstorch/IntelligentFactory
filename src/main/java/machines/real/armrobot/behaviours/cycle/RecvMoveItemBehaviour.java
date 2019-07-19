package machines.real.armrobot.behaviours.cycle;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.armrobot.ArmrobotAgent;

/**
 * receive transport request.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RecvMoveItemBehaviour extends CyclicBehaviour {
    private ArmrobotAgent armagent;

    public RecvMoveItemBehaviour(ArmrobotAgent a) {
        super(a);
        this.armagent = a;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        ACLMessage receive = armagent.receive(mt);
        if(receive!=null){
            armagent.getRequestQueue().offer(receive);
        } else {
            block();
        }
    }
}

