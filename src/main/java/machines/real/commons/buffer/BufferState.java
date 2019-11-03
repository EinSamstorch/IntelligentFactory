package machines.real.commons.buffer;

/**
 * 工位台状态.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BufferState {

  public static final int STATE_EMPTY = 1;
  public static final int STATE_ARRIVING = 2;
  public static final int STATE_WAITING = 3;
  public static final int STATE_PROCESSING = 4;
  public static final int STATE_PROCESSED = 5;

  private Integer state = STATE_EMPTY;

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  /**
   * 转移到下个状态.
   */
  public void nextState() {
    if (state != STATE_PROCESSED) {
      state += 1;
    }
  }

  public void resetState() {
    state = STATE_EMPTY;
  }
}
