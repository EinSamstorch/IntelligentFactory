package machines.real.mill;

import machines.real.commons.hal.MachineHalImpl;

/**
 * 铣床hal 仅 加工和时间预估两个功能
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillHalImpl extends MachineHalImpl {
    public MillHalImpl() {
        super();
    }

    public MillHalImpl(int port) {
        super(port);
    }
}
