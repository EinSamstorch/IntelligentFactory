package machines.agent;

import machines.real.commons.MachineState;
import machines.real.commons.buffer.BufferManger;
import machines.real.commons.hal.MiddleHal;

/**
 * 机床agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class RealMachineAgent extends RealAgentTemplate {

  private MiddleHal hal;
  private String armPwd;
  private BufferManger bufferManger;
  private MachineState machineState;

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

  public MiddleHal getHal() {
    return hal;
  }

  public void setHal(MiddleHal hal) {
    this.hal = hal;
  }
}
