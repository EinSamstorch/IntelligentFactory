package machines.real.mill.behaviours.cycle;

import commons.Buffer;
import jade.core.behaviours.CyclicBehaviour;
import machines.real.commons.ArmrobotMoveItemRequest;
import machines.real.mill.MillAgent;
import machines.real.mill.MillHal;
import machines.real.mill.behaviours.simple.CallForArm;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class LoadItemBehaviour extends CyclicBehaviour {
    private MillAgent magent;
    private MillHal hal;
    private Buffer[] buffers;

    public LoadItemBehaviour(MillAgent magent) {
        super(magent);
        this.magent = magent;
        this.hal = magent.getHal();
        this.buffers = magent.getBuffers();
    }

    @Override
    public void action() {
        if(!magent.isBusy()) {
            for (int i = 0; i < buffers.length; i++) {
                // 已到达 且 未加工 且 机床空闲
                boolean ready = buffers[i].isArrived() && !buffers[i].isProcessed() && !magent.isBusy();
                if(ready) {
                    // 向机械手发起搬运请求
                    magent.setBusy(true);
                    String from = String.valueOf(buffers[i].getIndex());
                    String to = magent.getLocalName();
                    String goodsId = buffers[i].getWpInfo().getGoodsId();
                    ArmrobotMoveItemRequest request = new ArmrobotMoveItemRequest(from, to, goodsId);
                    magent.addBehaviour(new CallForArm(magent, request, buffers[i], magent.getArmPwd(), CallForArm.LOAD));
                }
            }
        } else {
            block(1000);
        }
    }
}
