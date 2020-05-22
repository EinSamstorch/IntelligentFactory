package machines.real.commons.behaviours.simple;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.commons.request.ArmRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SendRequstToArm extends OneShotBehaviour {

  private ArmRequest request;
  private String password;
  private String conversationId;
  private String infoString;

  /**
   * 请求机械手搬运.
   *
   * @param request        机械手搬运任务
   * @param password       机械手密码
   * @param conversationId 会话id,用于匹配回信
   * @param infoString     消息字符串,用于日志输出
   */
  public SendRequstToArm(ArmRequest request, String password, String conversationId,
      String infoString) {
    this.request = request;
    this.password = password;
    this.conversationId = conversationId;
    this.infoString = infoString;
  }

  @Override
  public void action() {
    // 发送移动货物请求
    ACLMessage msg;
    try {
      msg = DfUtils.createRequestMsg(request);
      DfUtils.searchDf(myAgent, msg, DfServiceType.ARM, password);

      msg.setConversationId(conversationId);
      msg.setLanguage(request.toString());
      myAgent.send(msg);
    } catch (Exception e) {
      e.printStackTrace();
    }
    LoggerUtil.agent.info("Call for arm. " + infoString);
  }
}
