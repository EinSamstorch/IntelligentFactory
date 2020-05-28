package machines.real.agv.behaviours.cycle;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 心跳包.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HeartBeatSender extends TickerBehaviour {

  /**
   * Construct a <code>TickerBehaviour</code> that call its
   * <code>onTick()</code> method every <code>period</code> ms.
   *
   * @param a is the pointer to the agent
   * @param period the tick period in ms
   */
  public HeartBeatSender(Agent a, long period) {
    super(a, period);
  }

  @Override
  protected void onTick() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setLanguage("HB");
    try {
      DfUtils.searchDf(myAgent, msg, DfServiceType.AGV);
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtil.agent.warn(e.getLocalizedMessage());
      return;
    }
    myAgent.send(msg);
  }
}
