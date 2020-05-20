package machines.real.buffer.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import machines.real.buffer.actions.ImExportAction;
import machines.real.commons.hal.MiddleHal;
import machines.real.commons.request.BufferRequest;

public class ImExportItemBehaviour extends OneShotBehaviour {

  private final MiddleHal hal;
  private final ACLMessage msg;

  public ImExportItemBehaviour(ACLMessage taskMsg, MiddleHal hal) {
    this.msg = taskMsg;
    this.hal = hal;
  }

  @Override
  public void action() {
    BufferRequest request = null;
    try {
      request = (BufferRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      return;
    }
    ImExportAction action = new ImExportAction(request.isImportMode(),
        request.getBufferNo());
    while (!hal.executeAction(action)) {
      LoggerUtil.hal.error(String.format("%s Failed! Buffer: %s, extra: %s.",
          request.isImportMode() ? "Import" : "Export",
          request.getBufferNo(),
          hal.getExtraInfo()));
      // 3秒后重试
      block(3000);
    }
    LoggerUtil.hal.debug(String.format("%s Buffer: %d",
        request.isImportMode() ? "Import" : "Export",
        request.getBufferNo()));
    ACLMessage reply = msg.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    reply.setContent("done");
    myAgent.send(reply);
  }
}
