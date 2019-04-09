package lathe;

import commons.AgentTemplate;
import commons.tools.DFServiceType;

/**
 * 车床Agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class LatheAgent extends AgentTemplate {
    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.LATHE);
    }

    @Override
    protected void loadINI() {
        // to do
    }

}
