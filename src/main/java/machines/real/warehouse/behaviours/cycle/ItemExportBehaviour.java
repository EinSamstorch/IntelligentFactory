package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import machines.real.commons.request.WarehouseRequest;
import machines.real.warehouse.WarehouseAgent;
import machines.real.warehouse.WarehouseHal;

import java.util.Queue;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemExportBehaviour extends CyclicBehaviour {
    private WarehouseHal hal;
    private Integer posOut;
    private Queue<ACLMessage> requestQueue;

    public ItemExportBehaviour(WarehouseAgent warehouseAgent) {
        super(warehouseAgent);
        hal = warehouseAgent.getHal();
        this.posOut = warehouseAgent.getPosOut();
        requestQueue = warehouseAgent.getExportQueue();
    }

    @Override
    public void action() {
        ACLMessage msg = requestQueue.poll();
        if (msg != null) {
            WarehouseRequest request = null;
            try {
                request = (WarehouseRequest) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            if (request != null) {
                Integer itemPosition = request.getItemPosition();
                // 通知AGV取货
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                myAgent.send(reply);
                if (hal.moveItem(itemPosition, posOut)) {
                    LoggerUtil.hal.info(String.format("Succeed! Export item from %d", itemPosition));
                } else {
                    LoggerUtil.hal.error(String.format("Failed! Export item from %d", itemPosition));
                    myAgent.clean(false);
                }
            } else {
                LoggerUtil.hal.error("Request NPE Error.");
            }
        } else {
            block(1000);
        }
    }
}
