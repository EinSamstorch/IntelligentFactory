package machines.real.agv;

import commons.BaseAgent;
import commons.tools.DfServiceType;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
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

public class AgvAgent extends BaseAgent {
    private Queue<ACLMessage> transportRequestQueue = new LinkedBlockingQueue<>();
    private AgvHal hal;

    @Override
    protected void setup() {
        super.setup();
        registerDf(DfServiceType.AGV);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        // 接收运输请求
        Behaviour b = new RecvTransportRequestBehaviour(this);
        addBehaviour(tbf.wrap(b));

        // 执行运输请求
        b = new TransportItemBehaviour(this);
        addBehaviour(tbf.wrap(b));
    }

    public Queue<ACLMessage> getTransportRequestQueue() {
        return transportRequestQueue;
    }

    public AgvHal getHal() {
        return hal;
    }
}
