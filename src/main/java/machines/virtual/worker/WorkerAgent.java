package machines.virtual.worker;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.BaseAgent;

/**
 * 专门负责招投标任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WorkerAgent extends BaseAgent {

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
