package machines.real.arm;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

import java.util.Queue;

/**
 * arm robot agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmAgent extends BaseAgent {
    private String serviceType;
    private String armPwd;
    private Behaviour[] behaviours;

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public void setArmPwd(String armPwd) {
        this.armPwd = armPwd;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDf(serviceType, armPwd);


        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }
}
