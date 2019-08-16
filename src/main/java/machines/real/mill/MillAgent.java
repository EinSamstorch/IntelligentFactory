package machines.real.mill;

import commons.tools.DfServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.cycle.LoadItemBehaviour;
import machines.real.commons.behaviours.cycle.MaintainBufferBehaviour;
import machines.real.mill.behaviours.simple.MillProcessItem;

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
        registerDf(DfServiceType.MILL);

        hal = new MillHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

//        Behaviour b = new LoadItemBehaviour(this, LoadItemBehaviour.MILL);
        Behaviour b = new LoadItemBehaviour(this, MillProcessItem.class);
        addBehaviour(tbf.wrap(b));

        b = new MaintainBufferBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }
}
