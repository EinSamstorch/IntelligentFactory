package warehouse.behaviours.cycle;

import commons.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import warehouse.WarehouseAgent;
import warehouse.WarehouseHal;

/**
 * 硬件控制.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class HalBehaviour extends CyclicBehaviour {
    private WarehouseAgent whagent;
    private WarehouseHal hal;
    public HalBehaviour(WarehouseAgent a) {
        super(a);
        whagent = a;
        hal = a.getHal();
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
                MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
        );
        ACLMessage msg = whagent.receive(mt);
        if(msg != null) {
            WorkpieceInfo wpInfo;
            try {
                wpInfo = (WorkpieceInfo) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
                return;
            }
            Integer warehousePosition = wpInfo.getWarehousePosition();
            if(hal.moveItem(warehousePosition, whagent.getPosOut())) {
                LoggerUtil.hal.info(String.format("item moved out from %s successfully", warehousePosition));
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                whagent.send(reply);
            } else {
                LoggerUtil.hal.warn(String.format("item moved out from %s failed", warehousePosition));
            }
        } else {
            block();
        }
    }
}
