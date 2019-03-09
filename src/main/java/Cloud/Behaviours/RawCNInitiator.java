package Cloud.Behaviours;

import CommonTools.LoggerUtil;
import CommonTools.Workpiece;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.util.leap.Iterator;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 原料招标发起人.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RawCNInitiator extends ContractNetInitiator {
    private Workpiece workpiece;
    private int nResponders = 0;

    public RawCNInitiator(Agent a, ACLMessage cfp, Workpiece workpiece) {
        super(a, cfp);
        this.workpiece = workpiece;
        Iterator it = cfp.getAllReceiver();
        // 计算招标数量
        while(it.hasNext()){
            nResponders++;
            it.next();
        }
        if(nResponders == 0){
            // 没有招标商，移除招标行为
            LoggerUtil.agent.error("Raw Responder Not Found" );
        }

    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        if(responses.size() < nResponders){
            LoggerUtil.agent.info(String.format("Timeout expired: missing %d responders.", (nResponders-responses.size())));
        }
        int bestProposal = -1;
        AID bestProposer = null;
        ACLMessage accept = null;
        Enumeration e = responses.elements();
        while(e.hasMoreElements()){
            ACLMessage msg = (ACLMessage) e.nextElement();
            if(msg.getPerformative() == ACLMessage.PROPOSE){
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                int proposal = Integer.parseInt(msg.getContent());
                if(proposal > bestProposal){
                    bestProposal = proposal;
                    bestProposer = msg.getSender();
                    accept = reply;
                }
            }
        }
        if(accept != null){
            LoggerUtil.agent.info(String.format("Accepting proposal %d from responder %s", bestProposal, bestProposer.getName()));
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            accept.setContent(String.valueOf(bestProposal));
        }else{
            LoggerUtil.agent.error("Goodsid: " + workpiece.getGoodsId() + " is out of stock.");
        }
    }

    @Override
    protected void handleInform(ACLMessage inform) {
        LoggerUtil.agent.info("Get type: " + workpiece.getGoodsId() + ", position: " + inform.getContent());
    }
}
