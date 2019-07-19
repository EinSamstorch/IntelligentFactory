package machines.real.armrobot;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
import machines.real.armrobot.behaviours.cycle.MoveItemBehaviour;
import machines.real.armrobot.behaviours.cycle.RecvMoveItemBehaviour;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * arm robot agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmrobotAgent extends AgentTemplate {
    private ArmrobotHal hal;
    private Queue<ACLMessage> requestQueue = new LinkedBlockingQueue<>();
    @Override
    protected void setup() {
        super.setup();

        String armPwd = IniLoader.loadArmPassword(getLocalName());
        halPort = IniLoader.loadHalPort(getLocalName());
        registerDF(DFServiceType.ARMROBOT, armPwd);

        hal = new ArmrobotHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        Behaviour b = new RecvMoveItemBehaviour(this);
        addBehaviour(tbf.wrap(b));

        b = new MoveItemBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }


    public ArmrobotHal getHal() {
        return hal;
    }

    public Queue<ACLMessage> getRequestQueue() {
        return requestQueue;
    }
}
