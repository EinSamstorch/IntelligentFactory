package machines.real.buffer.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import machines.real.buffer.BufferHal;
import machines.real.commons.request.BufferOperation;
import machines.real.commons.request.BufferRequest;

public class InExportItemBehaviour extends SimpleBehaviour {

  private final int op;
  private final int bufferNo;
  private final BufferHal hal;
  private final ACLMessage msg;
  private boolean done = false;

  /**
   * 构造函数， 生成简单任务操控工位的进出料.
   *
   * @param request 进出货请求
   * @param hal     硬件层
   * @param msg     请求来源消息
   */
  public InExportItemBehaviour(BufferRequest request, BufferHal hal, ACLMessage msg) {
    this(request.getOp(), request.getBufferNo(), hal, msg);
  }

  /**
   * 构造函数， 生成简单任务操控工位的进出料.
   *
   * @param op       操作码, 见{@link BufferOperation}
   * @param bufferNo 工位台编号
   * @param hal      硬件层
   * @param msg      请求来源消息
   */
  public InExportItemBehaviour(int op, int bufferNo, BufferHal hal, ACLMessage msg) {
    this.op = op;
    this.bufferNo = bufferNo;
    this.hal = hal;
    this.msg = msg;
  }

  @Override
  public void action() {
    switch (op) {
      case BufferOperation.OP_EXPORT:
        done = hal.exportItem(bufferNo);
        break;
      case BufferOperation.OP_IMPORT:
        done = hal.importItem(bufferNo);
        break;
      default:
        throw new IllegalArgumentException("Wrong buffer operation code.");
    }
    if (done) {
      LoggerUtil.hal.debug("Export Buffer: " + bufferNo);
      ACLMessage reply = msg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
    } else {
      LoggerUtil.hal.error(String.format("Failed! Export Buffer: %s, extra: %s.",
          bufferNo, hal.getExtraInfo()));
      // 10秒后重试
      block(10000);
    }
  }

  @Override
  public boolean done() {
    return done;
  }
}
