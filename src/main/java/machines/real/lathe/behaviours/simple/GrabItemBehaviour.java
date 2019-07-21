package machines.real.lathe.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.SimpleBehaviour;
import machines.real.lathe.LatheHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class GrabItemBehaviour extends SimpleBehaviour {
    private boolean isDone = false;
    private LatheHal hal;

    public GrabItemBehaviour(LatheHal hal) {
        this.hal = hal;
    }

    @Override
    public void action() {
        if (hal.grabItem()) {
            LoggerUtil.hal.info("Grab Item Success!");
            isDone = true;
        } else {
            LoggerUtil.hal.error("Grab Item Failed!");
            myAgent.clean(false);
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
