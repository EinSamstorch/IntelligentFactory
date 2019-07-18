package machines.real.agv.behaviours.cycle;

import commons.NotifyFinish;
import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;
import machines.real.agv.AgvAgent;
import machines.real.agv.AgvHal;
import machines.real.agv.behaviours.simple.CallForWarehouse;
import machines.real.commons.ItemMoveRequest;
import machines.real.commons.TransportRequest;

/**
 * 执行运输任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class TransportItemBehaviour extends CyclicBehaviour {
    private AgvAgent aagent;
    private AgvHal hal;
    private static final Integer WAREHOUSE_EXPORT = 26;
    private static final int STATE_READY = 1;
    private static final int STATE_CALL_WH = 2;
    private static final int STATE_DO_TASK = 3;
    private static final int STATE_WAIT_WH = 4;
    private int state;
    private TransportRequest request;
    private ACLMessage requestMsg;
    private volatile NotifyFinish notifyFinish;

    public TransportItemBehaviour(AgvAgent aagent) {
        super(aagent);
        this.aagent = aagent;
        this.hal = aagent.getHal();
        this.state = STATE_READY;
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
        }
//        ACLMessage msg = aagent.getTransportRequestQueue().poll();
//        if (msg != null) {
//            TransportRequest request = null;
//            try {
//                request = (TransportRequest) msg.getContentObject();
//            } catch (UnreadableException e) {
//                e.printStackTrace();
//            }
//            if (request != null) {
//                int from = request.getFrom();
//                int to = request.getTo();
//                if (from == WAREHOUSE_EXPORT) {
//                    // CALL FOR WAREHOUSE
//                    ItemMoveRequest exportRequest = new ItemMoveRequest(request.getWpInfo().getWarehousePosition());
//                    myAgent.addBehaviour(new CallForWarehouse(myAgent, exportRequest));
//                } else {
//                    doTask();
//                }
//            } else {
//                LoggerUtil.hal.error("Request NPE Error.");
//            }
//        } else {
//            block(1000);
//        }
    }

    private void waitNotify() {
        if (notifyFinish.getDone()) {
            state = STATE_DO_TASK;
        } else {
            block(1000);
        }
    }

    private void callForWarehouse() {
        ItemMoveRequest exportRequest = new ItemMoveRequest(request.getWpInfo().getWarehousePosition());
        notifyFinish = new NotifyFinish();
        myAgent.addBehaviour(new CallForWarehouse(myAgent, exportRequest, notifyFinish));
        state = STATE_WAIT_WH;
    }

    private void getRequest() {
        ACLMessage msg = aagent.getTransportRequestQueue().poll();
        if (msg != null) {
            this.requestMsg = msg;
            TransportRequest request = null;
            try {
                request = (TransportRequest) msg.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            if (request != null) {
                int from = request.getFrom();
                int to = request.getTo();
                if (from == WAREHOUSE_EXPORT) {
                    state = STATE_CALL_WH;
                    this.request = request;
                } else {
                    state = STATE_DO_TASK;
                    this.request = request;
                }
            } else {
                LoggerUtil.hal.error("Request NPE Error.");
            }
        } else {
            block(1000);
        }
    }

    private void doTask() {
        int from = this.request.getFrom();
        int to = this.request.getTo();
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
            aagent.send(inform);

//                    AID receiver = myAgent.getAID();
//                    receiver.setLocalName(request.getWpInfo().getPreOwnerId());
//
//                    ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
//                    inform.addReceiver(receiver);
//                    inform.setLanguage("BUFFER_INDEX");
//                    inform.setContent(Integer.toString(from));

            // 通知到货
            ACLMessage reply = requestMsg.createReply();
            reply.setPerformative(ACLMessage.INFORM);
            aagent.send(reply);
            state = STATE_READY;
        } else {
            LoggerUtil.hal.error(String.format("Failed! Request from %d to %d.", from, to));
        }
    }
}
