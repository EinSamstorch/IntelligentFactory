package machines.real.commons.behaviours.simple;


import commons.order.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.behaviours.OneShotBehaviour;
import machines.real.commons.RealMachineAgent;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.hal.MachineHal;
import machines.real.commons.request.ArmrobotRequest;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class SimpleProcessItemBehaviour extends OneShotBehaviour {
    private Buffer buffer;
    private MachineHal hal;
    private RealMachineAgent machineAgent;

    public SimpleProcessItemBehaviour(RealMachineAgent machineAgent, Buffer buffer) {
        super(machineAgent);
        this.machineAgent = machineAgent;
        this.buffer = buffer;
        this.hal = machineAgent.getHal();
    }

    @Override
    public void action() {
        LoggerUtil.hal.info(String.format("Start processing item from buffer %d", buffer.getIndex()));
        WorkpieceInfo wpInfo = buffer.getWpInfo();
        buffer.setProcessTimestamp(System.currentTimeMillis());
        if(hal.process(wpInfo)) {
            LoggerUtil.hal.info(String.format("Item orderid:%s, workpieceid:%s processed.",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId()));
            String from = myAgent.getLocalName();
            String to = String.valueOf(buffer.getIndex());
            String goodsid = wpInfo.getGoodsId();
            ArmrobotRequest request = new ArmrobotRequest(from, to, goodsid);
            myAgent.addBehaviour(new SimpleCallForArm(machineAgent, request, buffer,
                    machineAgent.getArmPwd(), SimpleCallForArm.UNLOAD));
        } else {
            LoggerUtil.hal.error(String.format("Failed! Item orderid:%s, workpieceid:%s processed.",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId()));
        }
    }

}
