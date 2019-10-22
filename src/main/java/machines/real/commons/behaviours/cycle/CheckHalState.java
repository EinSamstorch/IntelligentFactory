package machines.real.commons.behaviours.cycle;

import commons.BaseAgent;
import jade.core.behaviours.TickerBehaviour;
import machines.real.commons.hal.BaseHal;

/**
 * 检查HAL是否在线，修改agent在线状态.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CheckHalState<T extends BaseAgent, H extends BaseHal> extends TickerBehaviour {
    private T tAgent;
    private H hal;

    /**
     * Construct a <code>TickerBehaviour</code> that call its
     * <code>onTick()</code> method every <code>period</code> ms.
     *
     * @param a      is the pointer to the agent
     * @param period the tick period in ms
     */
    public CheckHalState(T a, long period, H hal) {
        super(a, period);
        this.tAgent = a;
        this.hal = hal;
    }

    @Override
    protected void onTick() {
        if (hal.checkHalOnline()) {
            tAgent.setAgentOnline(true);
        } else {
            tAgent.setAgentOnline(false);
        }
    }
}
