package machines.real.agv.behaviours.simple;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import java.util.Random;
import machines.real.commons.request.BufferRequest;

/**
 * 向buffer发起请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class InteractBuffer extends SimpleBehaviour {

  private final BufferRequest request;
  private boolean done = false;
  private boolean sendMsg = false;
  private final String conversationId = "BUFFER_REQUEST_" + new Random().nextInt();

  public InteractBuffer(BufferRequest request) {
    this.request = request;
  }

  @Override
  public void action() {
    if (!sendMsg) {
      sendMessage();
    } else {
      waitResponse();
    }
  }

  private void sendMessage() {
    ACLMessage msg;

    try {
      msg = DfUtils.createRequestMsg(request);
      msg.setConversationId(conversationId);
      DfUtils.searchDf(myAgent, msg, DfServiceType.BUFFER);
    } catch (IOException e) {
      // setContentObject
      e.printStackTrace();
      LoggerUtil.agent.error("Serialization failed! " + e.getLocalizedMessage());
      return;
    } catch (Exception e) {
      // search df
      e.printStackTrace();
      LoggerUtil.agent.warn("Reach buffer agent failed! Retry in 3s.");
      block(3000);
      return;
    }

    myAgent.send(msg);
    sendMsg = true;
  }

  private void waitResponse() {
    MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
        MessageTemplate.MatchConversationId(conversationId));
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      return;
    }
    done = true;
  }

  @Override
  public boolean done() {
    return done;
  }
}
