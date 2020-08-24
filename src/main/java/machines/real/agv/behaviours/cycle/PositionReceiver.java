package machines.real.agv.behaviours.cycle;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Map;
import machines.real.agv.algorithm.AgvMapUtils2;
import machines.real.agv.algorithm.AgvMapUtils2.AgvState;

/**
 * 位置信息接收者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class PositionReceiver extends CyclicBehaviour {

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
    int pos = Integer.parseInt(receive.getContent());
    AID agv = receive.getSender();
    AgvMapUtils2.updateAgvLoc(agv, pos);
    AgvMapUtils2.initAgvLocLogic(agv, pos);
    Map<AID, AgvState> map = AgvMapUtils2.getAgvStateMap();
    if (!map.containsKey(agv)) {
      AgvMapUtils2.updateAgvState(agv, AgvState.FREE);
    }

    checkInMap.put(agv, System.currentTimeMillis());
  }
}
