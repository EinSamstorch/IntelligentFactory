package worker.behaviours.simple;

import commons.tools.LoggerUtil;
import worker.WorkerAgent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.util.leap.Iterator;

import java.util.Enumeration;
import java.util.Vector;

/**
 * 原材料招标发起人.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RAWCNInitiator extends ContractNetInitiator {
    private WorkerAgent wagent;
    private int nResponders = 0;
    public RAWCNInitiator(WorkerAgent wagent, ACLMessage cfp) {
        super(wagent, cfp);
        this.wagent = wagent;
        Iterator it = cfp.getAllReceiver();
        while (it.hasNext()) {
            nResponders++;
            it.next();
        }
        if (nResponders == 0) {
            // future to do
            // 没有招标商，移除招标行为
            LoggerUtil.agent.error("Raw Responder Not Found");
        }
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        if (responses.size() < nResponders) {
            LoggerUtil.agent.info(String.format("Timeout expired: missing %d responders.", (nResponders - responses.size())));
        }
        int bestProposal = -1;
        AID bestProposer = null;
        ACLMessage accept = null;
        Enumeration e = responses.elements();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                int proposal = Integer.parseInt(msg.getContent());
                if (proposal > bestProposal) {
                    bestProposal = proposal;
                    bestProposer = msg.getSender();
                    accept = reply;
                }
            }
        }
        if (accept != null) {
            LoggerUtil.agent.info(String.format("Accepting proposal %d from responder %s", bestProposal, bestProposer.getName()));
            accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            accept.setContent(String.valueOf(bestProposal));
        } else {
            LoggerUtil.agent.error("All warehouses is out of stock.");
        }
    }
}
