package machines.virtual.worker.behaviours.cycle;

import commons.order.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import machines.virtual.worker.WorkerAgent;

/**
 * 接受REQUEST请求，并放入处理队列中
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HandleRequest extends CyclicBehaviour {
    private WorkerAgent wagent;

    public HandleRequest(WorkerAgent wagent) {
        this.wagent = wagent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST));
        ACLMessage msg = wagent.receive(mt);
        if (msg != null) {
            try {
                WorkpieceInfo wpInfo = (WorkpieceInfo) msg.getContentObject();
                wagent.getWpInfoQueue().offer(wpInfo);
                LoggerUtil.agent.info("Receive Request From: " + msg.getSender().getName());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        } else {
            block();
        }
    }
}
