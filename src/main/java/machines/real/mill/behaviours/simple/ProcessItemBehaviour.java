package machines.real.mill.behaviours.simple;

import com.sun.org.apache.xpath.internal.operations.Bool;
import commons.Buffer;
import commons.WorkpieceInfo;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import machines.real.commons.ArmrobotMoveItemRequest;
import machines.real.mill.MillAgent;
import machines.real.mill.MillHal;
import org.omg.CORBA.MARSHAL;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProcessItemBehaviour extends OneShotBehaviour {
    private Buffer buffer;
    private MillHal hal;
    private MillAgent magent;

    public ProcessItemBehaviour(MillAgent magent, Buffer buffer) {
        super(magent);
        this.magent = magent;
        this.buffer = buffer;
        this.hal = magent.getHal();
    }

    @Override
    public void action() {
        LoggerUtil.hal.info(String.format("Start processing item from buffer %d", buffer.getIndex()));
        WorkpieceInfo wpInfo = buffer.getWpInfo();
        if(hal.process(wpInfo)) {
            LoggerUtil.hal.info(String.format("Item orderid:%s, workpieceid:%s processed.", wpInfo.getOrderId(), wpInfo.getWorkpieceId()));
            String from = magent.getLocalName();
            String to = String.valueOf(buffer.getIndex());
            String goodsid = wpInfo.getGoodsId();
            ArmrobotMoveItemRequest request = new ArmrobotMoveItemRequest(from, to, goodsid);
            magent.addBehaviour(new CallForArm(magent, request, buffer, magent.getArmPwd(), CallForArm.UNLOAD));
        } else {
            LoggerUtil.hal.error(String.format("Failed! Item orderid:%s, workpieceid:%s processed.", wpInfo.getOrderId(), wpInfo.getWorkpieceId()));

        }
    }

}
