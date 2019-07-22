package machines.real.commons;

import commons.AgentTemplate;
import commons.tools.IniLoader;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.behaviours.cycle.ProcessContractNetResponder;
import machines.real.commons.buffer.BufferManger;
import machines.real.commons.hal.MachineHal;

/**
 * template for real machine agents.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RealMachineAgent extends AgentTemplate {
    protected Integer halPort;
    protected MachineHal hal;
    private String armPwd;
    private BufferManger bufferManger;
    private MachineState machineState = new MachineState();

    @Override
    protected void setup() {
        super.setup();
        halPort = IniLoader.loadHalPort(getLocalName());
        armPwd = IniLoader.loadArmPassword(getLocalName());
        int[] bufferIndexes = IniLoader.loadBuffers(getLocalName());
        if (bufferIndexes != null) {
            bufferManger = new BufferManger(bufferIndexes);
        } else {
            bufferManger = null;
        }
    }

    protected void addContractNetResponder(ThreadedBehaviourFactory tbf) {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
                MessageTemplate.MatchPerformative(ACLMessage.CFP)
        );
        Behaviour b = new ProcessContractNetResponder(this, mt);
        addBehaviour(tbf.wrap(b));
    }


    public BufferManger getBufferManger() {
        return bufferManger;
    }

    public String getArmPwd() {
        return armPwd;
    }

    public MachineState getMachineState() {
        return machineState;
    }

    public MachineHal getHal() {
        return hal;
    }
}
