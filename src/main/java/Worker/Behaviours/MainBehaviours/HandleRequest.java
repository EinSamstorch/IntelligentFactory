package Worker.Behaviours.MainBehaviours;

import CommonTools.LoggerUtil;
import Commons.WorkpieceInfo;
import Worker.WorkerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * 处理REQUEST请求.
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
        if(msg != null){
            try {
                WorkpieceInfo wpInfo = (WorkpieceInfo) msg.getContentObject();
                wagent.getWpInfoQueue().offer(wpInfo);
                LoggerUtil.agent.info("Receive Request From: " + msg.getSender().getName());
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
        }else{
            block();
        }
    }
}
