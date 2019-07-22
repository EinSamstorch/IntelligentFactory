package machines.virtual.worker;

import commons.AgentTemplate;
import commons.order.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.virtual.worker.behaviours.cycle.HandleRequest;
import machines.virtual.worker.behaviours.cycle.HandleWorkpiece;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 专门负责招投标任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WorkerAgent extends AgentTemplate {
    private Integer detectRatio;
    private Queue<WorkpieceInfo> wpInfoQueue = new LinkedBlockingQueue<>();

    public Queue<WorkpieceInfo> getWpInfoQueue() {
        return wpInfoQueue;
    }


    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.WORKER);

        detectRatio = loadDetectRatio();
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

        Behaviour b = new HandleRequest(this);
        addBehaviour(tbf.wrap(b));

        b = new HandleWorkpiece(this);
        addBehaviour(tbf.wrap(b));
    }

    private int loadDetectRatio() {
        Map<String, String> setting = IniLoader.load(getLocalName());
        return Integer.parseInt(setting.get("detect_ratio"));
    }

    public Integer getDetectRatio() {
        return detectRatio;
    }
}
