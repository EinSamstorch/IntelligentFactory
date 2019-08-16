package machines.real.vision;

import commons.tools.DfServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.cycle.LoadItemBehaviour;
import machines.real.commons.behaviours.cycle.MaintainBufferBehaviour;
import machines.real.vision.behaviours.cycle.CheckItemContractNetResponder;
import machines.real.vision.behaviours.sequential.VisionProcessItem;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VisionAgent extends RealMachineAgent {
    private VisionHal hal;

    @Override
    protected void setup() {
        super.setup();
        registerDf(DfServiceType.VISION);

        hal = new VisionHal(halPort);
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

        Behaviour b = new LoadItemBehaviour(this, VisionProcessItem.class);
        addBehaviour(tbf.wrap(b));

        b = new MaintainBufferBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    @Override
    protected void addContractNetResponder(ThreadedBehaviourFactory tbf) {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new CheckItemContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }

    public VisionHal getVisionHal() {
        return hal;
    }
}
