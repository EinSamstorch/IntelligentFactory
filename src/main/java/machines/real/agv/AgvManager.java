package machines.real.agv;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

/**
 * Agv管理者，接受运输任务，并调度下发给AgvInstance完成.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvManager extends BaseAgent {

  @Override
  protected void setup() {
    super.setup();
    registerDf(serviceType);

    ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    for (Behaviour behaviour : behaviours) {
      addBehaviour(tbf.wrap(behaviour));
    }
  }
}
