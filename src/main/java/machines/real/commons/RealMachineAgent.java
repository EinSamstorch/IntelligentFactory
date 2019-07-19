package machines.real.commons;

import commons.AgentTemplate;
import commons.tools.IniLoader;
import machines.real.commons.buffer.BufferManger;
import machines.real.commons.hal.MachineHal;

import java.util.Map;

/**
 * template for real machine agents.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RealMachineAgent extends AgentTemplate {
    protected Integer halPort;
    private String armPwd;
    private BufferManger bufferManger;
    private MachineState machineState = new MachineState();
    protected MachineHal hal;

    @Override
    protected void setup() {
        super.setup();
        halPort = IniLoader.loadHalPort(getLocalName());
        armPwd = IniLoader.loadArmPassword(getLocalName());
        int[] bufferIndexes = IniLoader.loadBuffers(getLocalName());
        if(bufferIndexes != null) {
            bufferManger = new BufferManger(bufferIndexes);
        } else {
            bufferManger = null;
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
