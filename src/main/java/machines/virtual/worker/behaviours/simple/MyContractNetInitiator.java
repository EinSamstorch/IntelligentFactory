package machines.virtual.worker.behaviours.simple;

import commons.order.WorkpieceStatus;
import commons.tools.DfServiceType;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetInitiator;
import jade.util.leap.Iterator;
import machines.real.commons.ContractNetContent;
import machines.virtual.worker.algorithm.AlgorithmFactory;
import machines.virtual.worker.algorithm.BestPrice;
import machines.virtual.worker.behaviours.cycle.RetryMessage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Queue;
import java.util.Vector;

/**
 * 招投标发起人.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MyContractNetInitiator extends ContractNetInitiator {

    private final String serviceType;
    private final int nResponders;
    private final Queue<RetryMessage> retryQueue;
    private final ACLMessage cfp;

    public MyContractNetInitiator(Agent a, ACLMessage cfp, String serviceType, Queue<RetryMessage> retryQueue) {
        super(a, cfp);
        this.serviceType = serviceType;
        this.cfp = cfp;
        this.retryQueue = retryQueue;
        int numResponder = 0;
        Iterator it = cfp.getAllReceiver();
        while (it.hasNext()) {
            numResponder++;
            it.next();
        }
        nResponders = numResponder;
    }

    /**
     * 竞价规则需要修改，对于仓库而言，原料越多越好；对于加工而言，用时越短越好。
     *
     * @param responses
     * @param acceptances
     */
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        if (responses.size() < nResponders) {
            LoggerUtil.agent.info(String.format("Timeout expired: missing %d responders.",
                    (nResponders - responses.size())));
        }
        int bestProposal = Integer.MAX_VALUE;
        Enumeration e = responses.elements();
        ArrayList<ACLMessage> msgArray = new ArrayList<>();
        while (e.hasMoreElements()) {
            ACLMessage msg = (ACLMessage) e.nextElement();
            if (msg.getPerformative() == ACLMessage.PROPOSE) {
                msgArray.add(msg);
            }
        }
        int strategy;
        if (serviceType.equals(DfServiceType.WAREHOUSE) || serviceType.equals(DfServiceType.PRODUCT)) {
            strategy = BestPrice.HIGHEST;
        } else {
            strategy = BestPrice.LOWEST;
        }
        ACLMessage bestOffer = AlgorithmFactory.decision(msgArray, strategy);

        if (bestOffer != null) {
            ACLMessage accept;
            for (ACLMessage msg : msgArray) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.addElement(reply);
                if (msg.equals(bestOffer)) {
                    accept = reply;
                    accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    accept.setContent(String.valueOf(bestProposal));
                }
            }
            try {
                int bestPrice = ((ContractNetContent) bestOffer.getContentObject()).getOfferPrice();
                LoggerUtil.agent.info(String.format("Accepting proposal %d from responder %s",
                        bestPrice, bestOffer.getSender().getName()));
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }

        } else {
            LoggerUtil.agent.error(String.format("No %s service is available.", serviceType));
            // 放入重试队列
            try {
                retryQueue.offer(new RetryMessage((WorkpieceStatus) cfp.getContentObject(), serviceType));
            } catch (UnreadableException ex) {
                ex.printStackTrace();
            }
        }
    }
}
