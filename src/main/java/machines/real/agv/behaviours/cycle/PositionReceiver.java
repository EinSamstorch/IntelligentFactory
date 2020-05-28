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

  @Override
  public void action() {
    MessageTemplate template = MessageTemplate
        .and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchLanguage("init"));
    ACLMessage receive = myAgent.receive(template);
    if (receive == null) {
      return;
    }
    AID sender = receive.getSender();
    int pos = Integer.parseInt(receive.getContent());
    LoggerUtil.agent.info("Agv init: " + sender.getLocalName() + ", pos: " + pos);
    AgvMapUtils.setAgvLoc(receive.getSender(), pos);
  }
}
