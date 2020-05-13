package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Map;
import machines.real.agv.algorithm.AgvMapUtils;

/**
 * 位置信息接收者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class PositionReceiver extends CyclicBehaviour {

  private Map<AID, Long> checkInMap;

  public void setCheckInMap(Map<AID, Long> checkInMap) {
    this.checkInMap = checkInMap;
  }

  @Override
  public void action() {
    MessageTemplate template = MessageTemplate
        .and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchLanguage("HB"));
    ACLMessage receive = myAgent.receive(template);
    if (receive == null) {
      return;
    }
    AID sender = receive.getSender();
    checkInMap.put(sender, System.currentTimeMillis());
    int pos = Integer.parseInt(receive.getContent());
    LoggerUtil.agent.debug("HB: " + sender.getLocalName() + ", pos: " + pos);
    AgvMapUtils.setNodeMap(receive.getSender(), pos);
  }
}
