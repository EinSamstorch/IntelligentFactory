package machines.real.agv.behaviours.sequencial;

import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import machines.real.commons.request.WarehouseConveyorRequest;

/**
 * 请求仓库控制传送带.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class CallWarehouseConveyor extends SequentialBehaviour {

  /**
   * 仓库传送带控制请求.
   *
   * @param request 传送带控制请求
   */
  public CallWarehouseConveyor(WarehouseConveyorRequest request) {
    String conversationId = "CALL_FOR_WAREHOUSE_CONVEYOR_" + new Random().nextInt();
    addSubBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        try {
          ACLMessage msg = DfUtils.createRequestMsg(request);
          DfUtils.searchDf(myAgent, msg, DfServiceType.WAREHOUSE);
          msg.setConversationId(conversationId);
          msg.setLanguage(WarehouseConveyorRequest.LANGUAGE);
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
            done = true;
            LoggerUtil.agent
                .info("Workpiece is ready to " + (request.isImportMode() ? "import." : "export."));
          } else {
            LoggerUtil.agent.error("Response code error, code: " + receive.getPerformative());
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
