package Cloud.Behaviours;

import CommonTools.Workpiece;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Vector;

/**
 * 原料招标发起人.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RawCNInitiator extends ContractNetInitiator {
    private Workpiece workpiece;

    public RawCNInitiator(Agent a, ACLMessage cfp, Workpiece workpiece) {
        super(a, cfp);
        this.workpiece = workpiece;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

    }
}
