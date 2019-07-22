package machines.real.commons.behaviours.simple;


import commons.order.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;
import machines.real.commons.request.ArmrobotRequest;

import java.util.Random;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SimpleCallForArm extends SimpleBehaviour {
    public static final String LOAD = "load";
    public static final String UNLOAD = "unload";
    private ArmrobotRequest request;
    private Boolean init = true;
    private Boolean isDone = false;
    private String conversationId = String.format("CALL_FOR_ARM_%d", new Random().nextInt());
    private String password;
    private Buffer buffer;
    private String operation;
    private RealMachineAgent machineAgent;

    public SimpleCallForArm(RealMachineAgent machineAgent, ArmrobotRequest request, Buffer buffer, String password, String operation) {
        super(machineAgent);
        this.machineAgent = machineAgent;
        this.request = request;
        this.password = password;
        this.buffer = buffer;
        this.operation = operation;
    }

    @Override
    public void action() {
        if (init) {
            // 发出搬运请求
            initSendRequest();
        } else {
            // 等待完成通知
            waitForInform();
        }
    }

    private void sendToWorker(WorkpieceInfo wpInfo) {
        try {
            ACLMessage msg = DFUtils.createRequestMsg(wpInfo);
            DFUtils.searchDF(myAgent, msg, DFServiceType.WORKER);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void waitForInform() {
        MessageTemplate mt = MessageTemplate.MatchConversationId(conversationId);
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
            if (receive.getPerformative() == ACLMessage.INFORM) {
                isDone = true;
                switch (operation) {
                    case LOAD:
                        loadAction();
                        break;
                    case UNLOAD:
                        unloadAction();
                        break;
                }
            }
        } else {
            block();
        }
    }

    private void loadAction() {
        ArmrobotRequest unloadRequet = new ArmrobotRequest(this.request.getTo(), this.request.getFrom(),
                this.request.getGoodsid(), this.request.getStep());
        Behaviour afterProcess = new SimpleCallForArm(machineAgent, unloadRequet, buffer,
                machineAgent.getArmPwd(), SimpleCallForArm.UNLOAD);
        myAgent.addBehaviour(new SimpleProcessItemBehaviour(machineAgent, buffer, afterProcess));
        LoggerUtil.hal.info(String.format("Buffer %d load on machine.", buffer.getIndex()));
    }

    private void unloadAction() {
        machineAgent.getMachineState().resetBusy();
        buffer.getBufferState().setState(BufferState.STATE_PROCESSED);
        // 提交给 worker 进行 下一步招标以及状态更新
        sendToWorker(buffer.getWpInfo());
        LoggerUtil.hal.info(String.format("Buffer %d unload from machine", buffer.getIndex()));
    }

    private void initSendRequest() {
        // 发送移动货物请求
        ACLMessage msg = null;
        try {
            msg = DFUtils.createRequestMsg(request);
            DFUtils.searchDF(myAgent, msg, DFServiceType.ARMROBOT, password);

            msg.setConversationId(conversationId);
            myAgent.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        init = false;
        LoggerUtil.hal.debug("Call for arm.");
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
