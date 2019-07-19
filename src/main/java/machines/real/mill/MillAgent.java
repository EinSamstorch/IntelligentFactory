package machines.real.mill;

import commons.tools.DFServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.RealMachineAgent;
import machines.real.mill.behaviours.cycle.LoadItemBehaviour;
import machines.real.commons.behaviours.cycle.MaintainBufferBehaviour;
import machines.real.mill.behaviours.cycle.MillContractNetResponder;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillAgent extends RealMachineAgent {

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.MILL);

        hal = new MillHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

        Behaviour b = new LoadItemBehaviour(this);
        addBehaviour(tbf.wrap(b));

        b = new MaintainBufferBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    private void addContractNetResponder(ThreadedBehaviourFactory tbf) {

        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new MillContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }

}
