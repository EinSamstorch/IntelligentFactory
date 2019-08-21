package machines.real.commons;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import machines.real.commons.behaviours.cycle.ProcessContractNetResponder;
import machines.real.commons.buffer.BufferManger;
import machines.real.commons.hal.MachineHal;
import machines.real.commons.hal.MachineHalImpl;

/**
 * template for real machine agents.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RealMachineAgent extends BaseAgent {

    private MachineHal hal;
    private String armPwd;
    private BufferManger bufferManger;
    private MachineState machineState;
    private Behaviour[] behaviours;
    private String serviceType;

    public void setHal(MachineHal hal) {
        this.hal = hal;
    }

    public void setArmPwd(String armPwd) {
        this.armPwd = armPwd;
    }

    public void setBufferManger(BufferManger bufferManger) {
        this.bufferManger = bufferManger;
    }

    public void setMachineState(MachineState machineState) {
        this.machineState = machineState;
    }

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    protected void setup() {
        super.setup();
        registerDf(serviceType);

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
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
