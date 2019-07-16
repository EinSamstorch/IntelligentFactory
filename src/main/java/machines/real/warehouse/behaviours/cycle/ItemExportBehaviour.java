package machines.real.warehouse.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import machines.real.commons.ItemExportRequest;
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
    private WarehouseAgent wagent;
    private WarehouseHal hal;
    private Integer posOut;
    private Queue<ItemExportRequest> requestQueue;

    public ItemExportBehaviour(WarehouseAgent wagent) {
        super(wagent);
        this.wagent = wagent;
        hal = wagent.getHal();
        posOut = wagent.getPosOut();
        requestQueue = wagent.getExportQueue();
    }

    @Override
    public void action() {
        ItemExportRequest request = requestQueue.poll();
        if(request != null) {
            Integer itemPosition = request.getItemPosition();
            if(hal.moveItem(itemPosition, posOut)) {
                LoggerUtil.hal.info(String.format("Export item from %d", itemPosition));
            } else{
                LoggerUtil.hal.error(String.format("Failed! Export item from %d", itemPosition));
            }
        } else {
            block(1000);
        }
    }
}
