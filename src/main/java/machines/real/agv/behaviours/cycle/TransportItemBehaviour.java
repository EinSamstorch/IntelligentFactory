package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import machines.real.commons.TransportRequest;
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
        ACLMessage msg = aagent.getTransportRequestQueue().poll();
        TransportRequest request = null;
        try {
            request = (TransportRequest) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        if(request != null) {
            int from = request.getFrom();
            int to = request.getTo();
            if(hal.move(from, to)) {
                LoggerUtil.hal.info(String.format("Request from %d to %d.", from, to));
                // 通知取货
                // 修改aid 向 上一道工序的机床通知取货
                AID receiver = myAgent.getAID();
                receiver.setLocalName(request.getWpInfo().getPreOwnerId());

                ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                inform.addReceiver(receiver);
                inform.setLanguage("BUFFER_INDEX");
                inform.setContent(Integer.toString(from));

                // 通知到货
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                aagent.send(reply);
            } else {
                LoggerUtil.hal.error(String.format("Failed! Request from %d to %d.", from, to));
            }
        } else {
            block(1000);
        }
    }
}
