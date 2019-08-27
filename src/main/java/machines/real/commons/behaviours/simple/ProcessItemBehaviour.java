package machines.real.commons.behaviours.simple;

import commons.order.WorkpieceStatus;
import jade.core.behaviours.Behaviour;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MachineHal;

import java.lang.reflect.Method;

/**
 * 执行加工动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessItemBehaviour {
    public static Behaviour getBehaviour(MachineHal hal, Buffer buffer) {
        Method processMethod = null;
        try {
            processMethod = hal.getClass().getMethod("process", WorkpieceStatus.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new ExecuteActionBehaviour(hal, processMethod, buffer, "Process item.");
    }
}
