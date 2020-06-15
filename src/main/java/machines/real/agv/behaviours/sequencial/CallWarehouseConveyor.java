package machines.real.agv.behaviours.sequencial;

import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;
import machines.real.commons.behaviours.simple.WaitResponse;
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
  public CallWarehouseConveyor(WarehouseConveyorRequest request, AID warehouse) {
    String conversationId = "CALL_FOR_WAREHOUSE_CONVEYOR_" + new Random().nextInt();
    addSubBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        ACLMessage msg;
        try {
          msg = DfUtils.createRequestMsg(request);
        } catch (Exception e) {
          e.printStackTrace();
          return;
        }
        msg.addReceiver(warehouse);
        msg.setConversationId(conversationId);
        msg.setLanguage(WarehouseConveyorRequest.LANGUAGE);
        myAgent.send(msg);
      }
    });

    addSubBehaviour(new WaitResponse(conversationId) {
      @Override
      public void action() {
        super.action();
        if (done) {
          LoggerUtil.agent
              .info("Workpiece is ready to " + (request.isImportMode() ? "import." : "export."));
        }
      }
    });
  }
}
