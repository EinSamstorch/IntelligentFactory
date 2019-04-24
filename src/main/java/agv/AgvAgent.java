package agv;

import commons.AgentTemplate;
import commons.tools.DFServiceType;

/**
 * AGV Agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvAgent extends AgentTemplate {

    @Override
    protected void setup() {
        super.setup();
        registerDF(DFServiceType.AGV);
    }

    @Override
    protected void loadINI() {

    }
}
