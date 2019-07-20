package machines.real.lathe;

import commons.order.WorkpieceInfo;
import machines.real.commons.hal.BaseHal;

/**
 * 车床硬件.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LatheHal extends BaseHal {
    private static final String CMD_GRAB_ITEM = "grab_item";
    private static final String CMD_RELEASE_ITEM = "release_item";
    private static final String CMD_PROCESS = "process";
    private static final String CMD_EVALUATE = "evaluate";

    public LatheHal() {
        super();
    }

    public LatheHal(int port) {
        super(port);
    }

    public boolean grabItem() {
        return executeCmd(CMD_GRAB_ITEM, "");
    }

    public boolean releaseItem() {
        return executeCmd(CMD_RELEASE_ITEM, "");
    }

    public int evaluate(WorkpieceInfo wpInfo) {
        if(executeCmd(CMD_EVALUATE, wpInfo)){
            return (int) Float.parseFloat((String)getExtraInfo());
        } else {
            return 0;
        }
    }

    public boolean process(WorkpieceInfo wpInfo) {
        return executeCmd(CMD_PROCESS, wpInfo);
    }
}
