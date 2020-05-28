package machines.real.agv.behaviours.simple;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 初始化agv时，登记地址.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class InitPositionSender extends SimpleBehaviour {
  private int initPos;
  private boolean done = false;

  public InitPositionSender(int initPos) {
    this.initPos = initPos;
  }

  @Override
  public void action() {
    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setLanguage("init");
    msg.setContent(String.valueOf(initPos));
    try {
      DfUtils.searchDf(myAgent, msg, DfServiceType.AGV);
    } catch (Exception e) {
      e.printStackTrace();
      block(5000);
      return;
    }
    myAgent.send(msg);
    done = true;
  }

  @Override
  public boolean done() {
    return done;
  }
}
