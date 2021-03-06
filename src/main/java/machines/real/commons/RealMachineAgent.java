package machines.real.commons;

import commons.BaseAgent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.commons.buffer.BufferManger;
import machines.real.commons.hal.MachineHal;

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

    @Override
    protected void setup() {
        super.setup();

        ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
        for (Behaviour behaviour : behaviours) {
            addBehaviour(tbf.wrap(behaviour));
        }
    }

    public BufferManger getBufferManger() {
        return bufferManger;
    }

    public void setBufferManger(BufferManger bufferManger) {
        this.bufferManger = bufferManger;
    }

    public String getArmPwd() {
        return armPwd;
    }

    public void setArmPwd(String armPwd) {
        this.armPwd = armPwd;
    }

    public MachineState getMachineState() {
        return machineState;
    }

    public void setMachineState(MachineState machineState) {
        this.machineState = machineState;
    }

    public MachineHal getHal() {
        return hal;
    }

    public void setHal(MachineHal hal) {
        this.hal = hal;
    }
}
