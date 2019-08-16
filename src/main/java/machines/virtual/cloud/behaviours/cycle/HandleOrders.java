package machines.virtual.cloud.behaviours.cycle;

import commons.order.OrderInfo;
import commons.order.WorkpieceInfo;
import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import machines.virtual.cloud.CloudAgent;

/**
 * 处理新订单.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HandleOrders extends TickerBehaviour {
    private CloudAgent cagent;

    public HandleOrders(CloudAgent cagent, long period) {
        super(cagent, period);
        this.cagent = cagent;
    }

    @Override
    protected void onTick() {
        while (true) {
            OrderInfo oi = cagent.getOrderQueue().poll();
            if (oi == null) {
                break;
            }
            for (WorkpieceInfo wpInfo : oi.getWpInfoList()) {
                try {
                    // TODO 未来此处需要做处理，失败的消息需要记录并重试。
                    ACLMessage msg = DfUtils.createRequestMsg(wpInfo);
                    DfUtils.searchDf(cagent, msg, DfServiceType.WORKER);
                    cagent.send(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
