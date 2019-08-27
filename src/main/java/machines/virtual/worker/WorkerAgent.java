package machines.virtual.worker;

import commons.BaseAgent;
import commons.order.WorkpieceStatus;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

import java.util.Queue;

/**
 * 专门负责招投标任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WorkerAgent extends BaseAgent {
    private Integer detectRatio;
    private String serviceType;
    private Queue<WorkpieceStatus> wpInfoQueue;
    private Behaviour[] behaviours;

    public Queue<WorkpieceStatus> getWpInfoQueue() {
        return wpInfoQueue;
    }

    public void setWpInfoQueue(Queue<WorkpieceStatus> wpInfoQueue) {
        this.wpInfoQueue = wpInfoQueue;
    }

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public Integer getDetectRatio() {
        return detectRatio;
    }

    public void setDetectRatio(Integer detectRatio) {
        this.detectRatio = detectRatio;
    }
}
