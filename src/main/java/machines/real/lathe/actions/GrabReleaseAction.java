package machines.real.lathe.actions;

import machines.real.commons.actions.AbstractMachineAction;
import machines.real.lathe.LatheHal2;

/**
 * 抓取/释放工件动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class GrabReleaseAction extends AbstractMachineAction {

  private boolean grab;

  public GrabReleaseAction(boolean grab) {
    this.grab = grab;
  }

  @Override
  public String getCmd() {
    return grab ? LatheHal2.CMD_GRAB_ITEM : LatheHal2.CMD_RELEASE_ITEM;
  }

  @Override
  public Object getExtra() {
    return "";
  }
}
