package machines.real.lathe;

import commons.tools.DFServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.behaviours.cycle.LoadItemBehaviour;
import machines.real.commons.behaviours.cycle.MaintainBufferBehaviour;
import machines.real.lathe.behaviours.sequential.LatheProcessItem;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheAgent extends RealMachineAgent {
    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.LATHE);

        hal = new LatheHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        addContractNetResponder(tbf);

        Behaviour b = new MaintainBufferBehaviour(this);
        addBehaviour(tbf.wrap(b));

//        b = new LoadItemBehaviour(this, LoadItemBehaviour.LATHE);
        b = new LoadItemBehaviour(this, LatheProcessItem.class);
        addBehaviour(tbf.wrap(b));
    }
}
