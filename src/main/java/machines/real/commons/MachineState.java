package machines.real.commons;

/**
 * 机床状态.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class MachineState {

  public static int STATE_STAND_BY = 1;
  public static int STATE_BUSY = 2;

  private Integer state = STATE_STAND_BY;

  public Boolean isBusy() {
    return state == STATE_BUSY;
  }

  public void resetBusy() {
    state = STATE_STAND_BY;
  }

  public void setBusy() {
    state = STATE_BUSY;
  }
}
