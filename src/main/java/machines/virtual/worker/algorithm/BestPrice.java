package machines.virtual.worker.algorithm;

import commons.tools.LoggerUtil;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import machines.real.commons.ContractNetContent;

import java.util.ArrayList;

/**
 * 竞价算法.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BestPrice {
    public static final int HIGHEST = 1;
    public static final int LOWEST = 2;


    /**
     * 获得最优offer
     * @param msgArray offers
     * @param strategy 策略, 最高价 / 最低价
     * @return 最佳offer
     */
    public static ACLMessage getBestOffer(ArrayList<ACLMessage> msgArray, int strategy) {
        int bestPrice = 0;
        switch (strategy) {
            case HIGHEST:
                bestPrice = Integer.MIN_VALUE;
                break;
            case LOWEST:
                bestPrice = Integer.MAX_VALUE;
                break;
        }
        ACLMessage bestOffer = null;
        for (ACLMessage offerMsg : msgArray) {
            ContractNetContent content = null;
            try {
                content = (ContractNetContent) offerMsg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            if (content != null) {
                // 最高价策略和最低价策略
                int offerPrice = content.getOfferPrice();
                if(offerPrice <= 0) {
                    // 价格非正数 跳过
                    continue;
                }
                boolean better = strategy == HIGHEST
                        ? offerPrice > bestPrice
                        : offerPrice < bestPrice;
                if (better) {
                    bestPrice = offerPrice;
                    bestOffer = offerMsg;
                }
            } else {
                LoggerUtil.agent.error("ContractNetContent Read Error!");
            }
        }
        return bestOffer;

    }
}
