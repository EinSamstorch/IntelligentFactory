package machines.real.arm;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

/**
 * arm robot agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmAgent extends BaseAgent {
    /**
     * 通过反射获取,在 IMachineOnline中调用
     */
    private String armPwd;

    public void setArmPwd(String armPwd) {
        this.armPwd = armPwd;
    }

    @Override
    protected void setup() {
        super.setup();

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }
}
