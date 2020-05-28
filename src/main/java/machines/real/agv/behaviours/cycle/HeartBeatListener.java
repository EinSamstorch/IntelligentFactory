package machines.real.agv.behaviours.cycle;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Map;

/**
 * 心跳包接收者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HeartBeatListener extends CyclicBehaviour {

  private static MessageTemplate mt = MessageTemplate.and(
      MessageTemplate.MatchPerformative(ACLMessage.INFORM),
      MessageTemplate.MatchLanguage("HB"));
  private Map<AID, Long> checkInMap;

  public void setCheckInMap(Map<AID, Long> checkInMap) {
    this.checkInMap = checkInMap;
  }

  @Override
  public void action() {
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      block();
      return;
    }
    AID sender = receive.getSender();
    checkInMap.put(sender, System.currentTimeMillis());
  }
}
