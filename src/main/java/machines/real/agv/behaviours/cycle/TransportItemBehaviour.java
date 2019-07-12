package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import commons.tools.TransportRequest;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import machines.real.agv.AgvAgent;
import machines.real.agv.AgvHal;

/**
 * 执行运输任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class TransportItemBehaviour extends CyclicBehaviour {
    private AgvAgent aagent;
    private AgvHal hal;
    public TransportItemBehaviour(AgvAgent aagent) {
        super(aagent);
        this.aagent = aagent;
        this.hal = aagent.getHal();
    }

    @Override
    public void action() {
        TransportRequest request = aagent.getTransportRequestQueue().poll();
        if(request != null) {
            int from = request.getFrom();
            int to = request.getTo();
            if(hal.move(from, to)) {
                LoggerUtil.hal.error(String.format("Request from %d to %d succeed!", from, to));
            } else {
                LoggerUtil.hal.error(String.format("Request from %d to %d failed!", from, to));
            }
        } else {
            block(1000);
        }
    }
}
