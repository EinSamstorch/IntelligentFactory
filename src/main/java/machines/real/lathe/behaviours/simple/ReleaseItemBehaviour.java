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

public class ReleaseItemBehaviour extends SimpleBehaviour {
    private boolean isDone = false;
    private LatheHal hal;

    public ReleaseItemBehaviour(LatheHal hal) {
        this.hal = hal;
    }

    @Override
    public void action() {
        if (hal.releaseItem()) {
            LoggerUtil.hal.info("Release Item Success!");
            isDone = true;
        } else {
            LoggerUtil.hal.error("Release Item Failed!");
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
