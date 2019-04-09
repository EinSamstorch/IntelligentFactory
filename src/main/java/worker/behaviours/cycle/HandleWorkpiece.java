package worker.behaviours.cycle;

import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.WorkpieceInfo;
import worker.behaviours.simple.RAWCNInitiator;
import worker.WorkerAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
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
