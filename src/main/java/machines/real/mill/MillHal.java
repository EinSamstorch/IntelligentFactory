package machines.real.mill;

import commons.WorkpieceInfo;
import machines.real.commons.hal.BaseHal;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MillHal extends BaseHal {
    private static final String CMD_PROCESS = "process";
    private static final String CMD_EVALUATE = "evalute";

    public MillHal() {
        super();
    }

    public MillHal(int port) {
        super(port);
    }
    public boolean process(WorkpieceInfo wpInfo) {
        return executeCmd(CMD_PROCESS, wpInfo);
    }

    public int evaluate(WorkpieceInfo wpInfo) {
        if(executeCmd(CMD_EVALUATE, wpInfo)){
            return (int) Float.parseFloat((String)getExtraInfo());
        } else {
            return 0;
        }
    }
}
