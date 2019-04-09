package Worker.Behaviours.MainBehaviours;

import CommonTools.DFServiceType;
import CommonTools.DFUtils;
import Commons.MyExceptions.MsgCreateFailedException;
import Commons.WorkpieceInfo;
import Worker.Behaviours.SimpleBehaviours.RAWCNInitiator;
import Worker.WorkerAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * 处理工件招投标.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HandleWorkpiece extends CyclicBehaviour {
    private WorkerAgent wagent;
    public HandleWorkpiece(WorkerAgent wagent) {
        this.wagent = wagent;
    }


    @Override
    public void action() {
        WorkpieceInfo wpInfo = wagent.getWpInfoQueue().poll();
        if(wpInfo == null) return;
        if(wpInfo.getProviderAID() == null){
            try {
                ACLMessage msg = DFUtils.createCFPMsg(wpInfo);
                msg = DFUtils.searchDF(wagent, msg, DFServiceType.WAREHOUSE);
                Behaviour b = new RAWCNInitiator(wagent, msg);
                wagent.addBehaviour(b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
