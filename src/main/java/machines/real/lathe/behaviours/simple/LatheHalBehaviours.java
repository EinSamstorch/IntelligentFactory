package machines.real.lathe.behaviours.simple;

import jade.core.behaviours.Behaviour;
import machines.real.commons.behaviours.simple.ExecuteActionBehaviour;
import machines.real.lathe.LatheHal;
import machines.real.lathe.LatheHalImpl;

import java.lang.reflect.Method;

/**
 * 车床夹具动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheHalBehaviours {
    public static Behaviour grabItem(LatheHal hal) {
        return getBehaviour(hal, "grabItem", "Grab item.");
    }

    public static Behaviour releaseItem(LatheHal hal) {
        return getBehaviour(hal, "releaseItem", "Release item.");
    }

    private static Behaviour getBehaviour(LatheHal hal, String methodName, String infoStr) {
        Method grabItemMethod = null;
        try {
            grabItemMethod = LatheHalImpl.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (grabItemMethod != null) {
            return new ExecuteActionBehaviour(hal, grabItemMethod, null, infoStr);
        }
        return null;
    }

}
