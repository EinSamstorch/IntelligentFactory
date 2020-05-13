package machines.real.agv.behaviours.cycle;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.agv.MultiAgvHal;
import machines.real.agv.actions.GetPositionAction;
import machines.real.agv.actions.MachineAction;

/**
 * 定时发送位置信息，作为心跳系统.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class PositionSender extends TickerBehaviour {

  private MultiAgvHal hal;

  public void setHal(MultiAgvHal hal) {
    this.hal = hal;
  }

  public PositionSender(Agent a, long period) {
    super(a, period);
  }

  @Override
  protected void onTick() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setLanguage("HB");
    MachineAction action = new GetPositionAction();
    boolean result = hal.executeAction(action);
    if (!result) {
      return;
    }
    int pos = (int) hal.getExtra();
    msg.setContent(String.valueOf(pos));
    try {
      DfUtils.searchDf(myAgent, msg, DfServiceType.AGV);
    } catch (Exception e) {
      LoggerUtil.agent.error(e.getLocalizedMessage());
      e.printStackTrace();
      return;
    }
    myAgent.send(msg);
  }
}
