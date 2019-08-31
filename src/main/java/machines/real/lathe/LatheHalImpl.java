package machines.real.lathe;

import machines.real.commons.hal.MachineHalImpl;

/**
 * 车床硬件.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheHalImpl extends MachineHalImpl implements LatheHal {
    private static final String CMD_GRAB_ITEM = "grab_item";
    private static final String CMD_RELEASE_ITEM = "release_item";

    public LatheHalImpl() {
        super();
    }

    public LatheHalImpl(int port) {
        super(port);
    }

    @Override
    public boolean grabItem() {
        return executeCmd(CMD_GRAB_ITEM, "");
    }

    @Override
    public boolean releaseItem() {
        return executeCmd(CMD_RELEASE_ITEM, "");
    }

}
