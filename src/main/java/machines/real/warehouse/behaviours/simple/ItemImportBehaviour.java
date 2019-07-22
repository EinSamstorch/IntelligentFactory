package machines.real.warehouse.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import machines.real.commons.ItemMoveRequest;
import machines.real.warehouse.WarehouseAgent;
import machines.real.warehouse.WarehouseHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemImportBehaviour extends OneShotBehaviour {
    private WarehouseHal hal;
    private Integer posIn;
    private ItemMoveRequest request;

    public ItemImportBehaviour(WarehouseAgent whagent, ItemMoveRequest request) {
        super(whagent);
        hal = whagent.getHal();
        posIn = whagent.getPosIn();
        this.request = request;
    }

    @Override
    public void action() {
        if (request != null) {
            Integer itemPosition = request.getItemPosition();
            if (hal.moveItem(posIn, itemPosition)) {
                LoggerUtil.hal.info(String.format("Succeed! Import item to %d", itemPosition));
            } else {
                LoggerUtil.hal.info(String.format("Failed! Import item to %d", itemPosition));
            }
        }
    }
}
