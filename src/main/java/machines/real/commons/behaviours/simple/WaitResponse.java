package machines.real.commons.behaviours.simple;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * 等待消息回复.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class WaitResponse extends SimpleBehaviour {

  protected boolean done = false;
  private String conversationId;

  public WaitResponse(String conversationId) {
    this.conversationId = conversationId;
  }

  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchConversationId(conversationId),
        MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );
    ACLMessage receive = myAgent.receive(mt);
    if (receive == null) {
      block();
      return;
    }
    done = true;
  }

  @Override
  public boolean done() {
    return done;
  }
}
