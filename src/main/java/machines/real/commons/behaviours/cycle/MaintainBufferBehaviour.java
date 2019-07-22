package machines.real.commons.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.RealMachineAgent;

/**
 * 维护buffer状态
 * 取走工件后, 清空buffer
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MaintainBufferBehaviour extends CyclicBehaviour {
    private RealMachineAgent machineAgent;


    public MaintainBufferBehaviour(RealMachineAgent machineAgent) {
        super(machineAgent);
        this.machineAgent = machineAgent;

    }

    @Override
    public void action() {
        final String MSG_LANGUAGE = "BUFFER_INDEX";
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                MessageTemplate.MatchLanguage(MSG_LANGUAGE)
        );
        ACLMessage receive = myAgent.receive(mt);
        if (receive != null) {
            int bufferIndex = new Integer(receive.getContent());
            if (machineAgent.getBufferManger().resetBufferByIndex(bufferIndex)) {
                LoggerUtil.agent.info(String.format("Item removed from buffer %d", bufferIndex));
            } else {
                LoggerUtil.agent.error(String.format("Buffer %d not found!", bufferIndex));
            }
        } else {
            block();
        }
    }
}
