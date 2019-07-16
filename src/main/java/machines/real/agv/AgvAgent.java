package machines.real.agv;

import commons.AgentTemplate;
import commons.tools.DFServiceType;
import jade.lang.acl.ACLMessage;
import machines.real.commons.TransportRequest;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.agv.behaviours.cycle.RecvTransportRequestBehaviour;
import machines.real.agv.behaviours.cycle.TransportItemBehaviour;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * AGV Agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvAgent extends AgentTemplate {
    private Queue<ACLMessage> transportRequestQueue = new LinkedBlockingQueue<>();
    private AgvHal hal;

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.AGV);

        hal = new AgvHal(halPort);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        // 接收运输请求
        Behaviour b = new RecvTransportRequestBehaviour(this);
        addBehaviour(tbf.wrap(b));

        // 执行运输请求
        b = new TransportItemBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    @Override
    protected void loadINI() {

    }

    public Queue<ACLMessage> getTransportRequestQueue() {
        return transportRequestQueue;
    }

    public AgvHal getHal() {
        return hal;
    }
}
