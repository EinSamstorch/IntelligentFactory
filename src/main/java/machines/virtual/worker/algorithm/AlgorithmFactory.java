package machines.virtual.worker.algorithm;

import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

/**
 * 决策工厂.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AlgorithmFactory {

  public static ACLMessage decision(ArrayList<ACLMessage> msgArray, int strategy) {
    return BestPrice.getBestOffer(msgArray, strategy);
  }
}
