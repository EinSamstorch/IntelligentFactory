package Cloud.Behaviours.MainBehaviours;

import Cloud.CloudAgent;
import Cloud.Behaviours.SimpleBehaviours.Push2CloudBehaviour;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;


/**
 * TickerBehaviour类，检测是否有新状态信息需要推送.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class DetectUpdateMsg extends TickerBehaviour {
    private CloudAgent cagent;

    public DetectUpdateMsg(CloudAgent cagent, long period) {
        super(cagent, period);
        this.cagent = cagent;
    }

    @Override
    protected void onTick() {
        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        Behaviour b;
        String param;
        while (true) {
            param = cagent.getStateQueue().poll();
            if (param == null) break;
            b = new Push2CloudBehaviour(cagent, param, Push2CloudBehaviour.UPDATE_STATE);
            cagent.addBehaviour(tbf.wrap(b));
        }
        while (true) {
            param = cagent.getPosQueue().poll();
            if (param == null) break;
            b = new Push2CloudBehaviour(cagent, param, Push2CloudBehaviour.UPDATE_POSITION);
            cagent.addBehaviour(tbf.wrap(b));
        }
    }
}
