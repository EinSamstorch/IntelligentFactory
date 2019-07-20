package machines.virtual.worker.behaviours.cycle;

import commons.order.WorkpieceInfo;
import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.tools.LoggerUtil;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import machines.virtual.worker.WorkerAgent;
import machines.virtual.worker.behaviours.simple.MyContractNetInitiator;

import java.util.List;

/**
 * 处理工件招投标.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HandleWorkpiece extends CyclicBehaviour {
    private WorkerAgent workerAgent;
    public HandleWorkpiece(WorkerAgent workerAgent) {
        this.workerAgent = workerAgent;
    }


    @Override
    public void action() {
        WorkpieceInfo wpInfo = workerAgent.getWpInfoQueue().poll();
        if(wpInfo == null) return;
        List<String> processPlan = wpInfo.getProcessPlan();
        int processIndex = wpInfo.nextProcessIndex();
        if(processIndex >= processPlan.size()){
            // 所有工艺完成，回成品库
            LoggerUtil.agent.info(String.format("OrderId: %s, wpId: %s done.",
                    wpInfo.getOrderId(), wpInfo.getWorkpieceId()));
            processOn(wpInfo, DFServiceType.PRODUCT);
            return ;
        }

        String process = processPlan.get(processIndex);
        processOn(wpInfo, process);
    }

    private void processOn(WorkpieceInfo wpInfo, String serviceType){
        try{
            ACLMessage msg = DFUtils.createCFPMsg(wpInfo);
            if(serviceType.equals(DFServiceType.PRODUCT)) {
                DFUtils.searchDF(workerAgent, msg, DFServiceType.WAREHOUSE);
                msg.setLanguage("PRODUCT");
            } else if(serviceType.equals(DFServiceType.WAREHOUSE)) {
                DFUtils.searchDF(workerAgent, msg, serviceType);
                msg.setLanguage("RAW");
            } else {
                DFUtils.searchDF(workerAgent, msg, serviceType);
            }

            Behaviour b = new MyContractNetInitiator(workerAgent, msg, serviceType);
            workerAgent.addBehaviour(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
