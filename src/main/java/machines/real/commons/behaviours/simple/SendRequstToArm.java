package machines.real.commons.behaviours.simple;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.commons.request.ArmrobotRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SendRequstToArm extends OneShotBehaviour {
    private ArmrobotRequest request;
    private String password;
    private String conversationId;
    private String infoString;

    public SendRequstToArm(Agent a, ArmrobotRequest request, String password, String conversationId, String infoString) {
        super(a);
        this.request = request;
        this.password = password;
        this.conversationId = conversationId;
        this.infoString = infoString;
    }

    public SendRequstToArm(Agent a, ArmrobotRequest request, String password, String conversationId) {
        this(a, request, password, conversationId, "");
    }

    @Override
    public void action() {
        // 发送移动货物请求
        ACLMessage msg = null;
        try {
            msg = DfUtils.createRequestMsg(request);
            DfUtils.searchDf(myAgent, msg, DfServiceType.ARMROBOT, password);

            msg.setConversationId(conversationId);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LoggerUtil.hal.debug("Call for arm. " + infoString);
    }
}
