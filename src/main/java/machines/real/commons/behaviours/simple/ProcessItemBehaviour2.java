package machines.real.commons.behaviours.simple;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.actions.ProcessAction;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MiddleHal;

/**
 * 加工工件行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ProcessItemBehaviour2 extends OneShotBehaviour {

  private static ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
  private MiddleHal hal;
  private Buffer buffer;

  public ProcessItemBehaviour2(MiddleHal hal, Buffer buffer) {
    this.hal = hal;
    this.buffer = buffer;
  }

  @Override
  public void action() {
    MachineAction action = new ProcessAction(buffer.getWpInfo());
    // 设置Buffer 加工中
    buffer.startProcess();
    Behaviour b = tbf.wrap(new ActionExecutor(action, hal));
    myAgent.addBehaviour(b);
    while (!b.done()) {
      block(1000);
    }
    // 下一个加工工步
    buffer.getWpInfo().setNextProcessStep();
  }
}
