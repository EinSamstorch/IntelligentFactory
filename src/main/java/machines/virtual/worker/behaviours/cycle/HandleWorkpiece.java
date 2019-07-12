package machines.virtual.worker.behaviours.cycle;

import commons.tools.DFServiceType;
import commons.tools.DFUtils;
import commons.WorkpieceInfo;
import commons.tools.LoggerUtil;
import machines.virtual.worker.behaviours.simple.MyContractNetInitiator;
import machines.virtual.worker.WorkerAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import commons.MachineType;
import java.util.List;

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
        List<String> processPlan = wpInfo.getProcessPlan();
        int processIndex = wpInfo.nextProcessIndex();
        if(processIndex >= processPlan.size()){
            // 所有工艺完成，回成品库

            // action here
            return ;
        }

        // 未来修改 将 MachineType 去除 改用 DFServiceType
        String process = processPlan.get(processIndex);
        switch (process){
            case MachineType.WAREHOUSE:
                processOn(wpInfo, DFServiceType.WAREHOUSE);
                break;
            case MachineType.LATHE:
                processOn(wpInfo, DFServiceType.LATHE);
                break;
            case MachineType.MILLER:
                processOn(wpInfo, DFServiceType.MILLER);
                break;
            case MachineType.DETECTOR:
                processOn(wpInfo, DFServiceType.DETECTOR);
                break;
            default:
                LoggerUtil.agent.error("Unknown process: " + process);
        }
    }

    private void processOn(WorkpieceInfo wpInfo, String serviceType){
        try{
            ACLMessage msg = DFUtils.createCFPMsg(wpInfo);
            msg = DFUtils.searchDF(wagent, msg, serviceType);
            Behaviour b = new MyContractNetInitiator(wagent, msg);
            wagent.addBehaviour(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
