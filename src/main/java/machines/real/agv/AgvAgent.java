package machines.real.agv;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

/**
 * AGV Agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvAgent extends BaseAgent {
    private String serviceType;
    private Behaviour[] behaviours;

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDf(serviceType);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }
}
