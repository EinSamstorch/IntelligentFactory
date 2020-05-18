package machines.real.agv.behaviours.cycle;

import commons.NotifyFinish;
import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;
import machines.real.agv.SingleAgvHal;
import machines.real.agv.behaviours.simple.CallForWarehouse;
import machines.real.commons.request.AgvRequest;
import machines.real.commons.request.WarehouseRequest;

/**
 * 执行运输任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class TransportItemBehaviour extends CyclicBehaviour {

  private static final Integer WAREHOUSE_EXPORT = 26;
  private static final int STATE_READY = 1;
  private static final int STATE_CALL_WH = 2;
  private static final int STATE_DO_TASK = 3;
  private static final int STATE_WAIT_WH = 4;
  private SingleAgvHal hal;
  private int state;
  private AgvRequest request;
  private ACLMessage requestMsg;
  private volatile NotifyFinish notifyFinish;

  public TransportItemBehaviour() {
    super();
    this.state = STATE_READY;
  }

  public void setHal(SingleAgvHal hal) {
    this.hal = hal;
  }

  @Override
  public void action() {
    switch (state) {
      case STATE_READY:
        getRequest();
        break;
      case STATE_CALL_WH:
        callForWarehouse();
        break;
      case STATE_WAIT_WH:
        waitNotify();
        break;
      case STATE_DO_TASK:
        doTask();
        break;
      default:
    }
  }

  private void waitNotify() {
    if (notifyFinish.getDone()) {
      state = STATE_DO_TASK;
    } else {
      block(1000);
    }
  }

  private void callForWarehouse() {
    WarehouseRequest exportRequest = new WarehouseRequest(
        request.getWpInfo().getWarehousePosition());
    notifyFinish = new NotifyFinish();
    myAgent.addBehaviour(new CallForWarehouse(myAgent, exportRequest, notifyFinish));
    state = STATE_WAIT_WH;
  }

  private void getRequest() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );
    ACLMessage msg = myAgent.receive(mt);
    if (msg != null) {
      this.requestMsg = msg;
      AgvRequest request = null;
      try {
        request = (AgvRequest) msg.getContentObject();
      } catch (UnreadableException e) {
        e.printStackTrace();
      }
      if (request != null) {
        int from = request.getFromBuffer();
        this.request = request;
        if (from == WAREHOUSE_EXPORT) {
          state = STATE_CALL_WH;
        } else {
          state = STATE_DO_TASK;
        }
      } else {
        LoggerUtil.hal.error("Agv request NPE error.");
      }
    } else {
      block(1000);
    }
  }

  private void doTask() {
    int from = this.request.getFromBuffer();
    int to = this.request.getToBuffer();
    if (hal.move(from, to)) {
      LoggerUtil.hal.info(String.format("Success! Request from %d to %d.", from, to));
      // 通知取货
      // TODO 修改aid 向 上一道工序的机床通知取货
      // 不可以修改myAgent AID
      AID me = myAgent.getAID();
      AID receiver = new AID(request.getWpInfo().getPreOwnerId(), false);
      Iterator allAddresses = me.getAllAddresses();
      while (allAddresses.hasNext()) {
        receiver.addAddresses((String) allAddresses.next());
      }
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      inform.addReceiver(receiver);
      inform.setLanguage("BUFFER_INDEX");
      inform.setContent(Integer.toString(from));
      myAgent.send(inform);

      // 通知到货
      ACLMessage reply = requestMsg.createReply();
      reply.setPerformative(ACLMessage.INFORM);
      myAgent.send(reply);
      state = STATE_READY;
    } else {
      LoggerUtil.hal.error(String.format("Failed! Request from %d to %d.", from, to));
    }
  }
}
