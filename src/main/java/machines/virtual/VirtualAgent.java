package machines.virtual;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.BaseAgent;

/**
 * 虚拟agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class VirtualAgent extends BaseAgent {

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
