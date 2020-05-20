package machines.real.commons.behaviours.sequantial;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;
import machines.real.commons.request.AgvRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class CallForAgv extends SequentialBehaviour {


  /**
   * 请求agv, 仓库用.
   *
   * @param request agv搬运请求
   */
  public CallForAgv(AgvRequest request) {
    this(request, null);
  }

  /**
   * 请求agv,机床用.
   *
   * @param request agv搬运请求
   * @param buffer  工位台
   */
  public CallForAgv(AgvRequest request, Buffer buffer) {
    String conversationId = String.format("CALL_FOR_AGV_%d", new Random().nextInt());
    addSubBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        // 发送运输请求
        ACLMessage msg = null;
        try {
          msg = DfUtils.createRequestMsg(request);
          DfUtils.searchDf(myAgent, msg, DfServiceType.AGV);
          msg.setConversationId(conversationId);
          myAgent.send(msg);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    addSubBehaviour(new SimpleBehaviour() {
      private boolean done = false;

      @Override
      public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
          if (receive.getPerformative() == ACLMessage.INFORM) {
            // 已完成
            done = true;
            if (buffer != null) {
              buffer.getBufferState().setState(BufferState.STATE_WAITING);
            }
            LoggerUtil.agent.info(String
                .format("AgvRequest: from buffer %d to buffer %d, done.",
                    request.getFromBuffer(),
                    request.getToBuffer()));
          } else {
            LoggerUtil.agent.error("Performative error.");
          }
        } else {
          block();
        }
      }

      @Override
      public boolean done() {
        return done;
      }
    });
  }
}
