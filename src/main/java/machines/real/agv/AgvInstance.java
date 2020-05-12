package machines.real.agv;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

/**
 * 多Agv系统中，单个Agv实例.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvInstance extends BaseAgent {

  @Override
  protected void setup() {
    super.setup();

    ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    for (Behaviour behaviour : behaviours) {
      addBehaviour(tbf.wrap(behaviour));
    }
  }
}
