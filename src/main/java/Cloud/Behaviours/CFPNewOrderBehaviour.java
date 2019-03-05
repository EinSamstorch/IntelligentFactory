package Cloud.Behaviours;

import Cloud.CloudAgent;
import jade.core.behaviours.TickerBehaviour;

/**
 * 拆分新订单内的工件列表，同时为工件向仓库进行招标.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class CFPNewOrderBehaviour extends TickerBehaviour {
    private CloudAgent cagent;

    public CFPNewOrderBehaviour(CloudAgent cagent, long period) {
        super(cagent, period);
        this.cagent = cagent;
    }

    @Override
    protected void onTick() {
        while (cagent.getOrderList().size() > 0) {

        }
    }
}
