package machines.real.agv.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.Random;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.behaviours.simple.SendRequest;

/**
 * AgvManager向Instance发送命令请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ActionCaller extends SequentialBehaviour {

  /**
   * 调用agv instance 执行动作.
   *
   * @param receiver agv
   * @param action   动作
   */
  public ActionCaller(AID receiver, MachineAction action) {

    final String conversationId = "ActionCaller" + new Random().nextInt();
    addSubBehaviour(new SendRequest(receiver, action, conversationId));
    addSubBehaviour(new SimpleBehaviour() {
      private boolean done = false;

      @Override
      public void action() {
        MessageTemplate mt = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchConversationId(conversationId)
        );
        ACLMessage receive = myAgent.receive(mt);
        if (receive == null) {
          return;
        }
        done = true;
        Object extra = "";
        try {
          extra = receive.getContentObject();
        } catch (UnreadableException e) {
          LoggerUtil.agent.error("Deserialization result extra failed! From: "
              + receive.getSender().getLocalName() + ", "
              + e.getLocalizedMessage());
        }
        action.setResultExtra(extra);
      }

      @Override
      public boolean done() {
        return done;
      }
    });
  }
}
