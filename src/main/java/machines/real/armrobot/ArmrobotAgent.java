package machines.real.armrobot;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ReceiverBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
import machines.real.armrobot.behaviours.cycle.MoveItemBehaviour;
import machines.real.armrobot.behaviours.cycle.RecvMoveItemBehaviour;
import machines.real.commons.ArmrobotMoveItemRequest;

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
    private String armPwd;
    private ArmrobotHal hal;
    private Queue<ACLMessage> requestQueue = new LinkedBlockingQueue<>();
    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.ARMROBOT, armPwd);

        hal = new ArmrobotHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        Behaviour b = new RecvMoveItemBehaviour(this);
        addBehaviour(tbf.wrap(b));

        b = new MoveItemBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    /**
     * load arm password
     */
    @Override
    protected void loadINI() {
        Map<String, String> setting = IniLoader.load(IniLoader.SECTION_COMMON);

        final String SETTING_ARM_PASSWORD = "arm_password";
        armPwd = setting.get(SETTING_ARM_PASSWORD);
    }

    public ArmrobotHal getHal() {
        return hal;
    }

    public Queue<ACLMessage> getRequestQueue() {
        return requestQueue;
    }
}
