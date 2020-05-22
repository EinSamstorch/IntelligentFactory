package machines.real.commons.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.io.Serializable;

/**
 * 发送请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class SendRequest extends OneShotBehaviour {

  private String conversationId;
  private ACLMessage msg = null;

  /**
   * 向其他agent发送任务请求.
   *
   * @param receiver       接收者
   * @param request        任务
   * @param conversationId 会话id
   */
  public SendRequest(AID receiver, Serializable request, String conversationId) {
    this.conversationId = conversationId;

    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
    msg.setProtocol(InteractionProtocol.FIPA_REQUEST);
    msg.addReceiver(receiver);
    try {
      msg.setContentObject(request);
    } catch (IOException e) {
      e.printStackTrace();
      LoggerUtil.agent.error("Serialization request failed! " + request.getClass());
      msg = null;
    }
    this.msg = msg;
  }

  /**
   * 向其他agent发送消息.
   *
   * @param msg            消息
   * @param conversationId 会话id
   */
  public SendRequest(ACLMessage msg, String conversationId) {
    this.msg = msg;
    this.conversationId = conversationId;
  }


  @Override
  public void action() {
    if (msg == null) {
      LoggerUtil.agent.error("Message NPE!");
      return;
    }
    msg.setConversationId(conversationId);
    myAgent.send(msg);
  }
}
