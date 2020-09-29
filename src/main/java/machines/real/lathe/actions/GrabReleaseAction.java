package machines.real.lathe.actions;

import commons.order.WorkpieceStatus;
import machines.real.commons.actions.AbstractMachineAction;
import machines.real.commons.request.LatheRequest;
import machines.real.lathe.LatheHal;

/**
 * 抓取/释放工件动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class GrabReleaseAction extends AbstractMachineAction {

  private boolean grab;

  /**
   * 车床夹紧动作.
   *
   * @param request 车床夹紧请求
   */
  public GrabReleaseAction(LatheRequest request) {
    this.grab = request.isGrab();
    this.orderId = request.getOrderId();
    this.workpieceId = request.getWorkpieceId();
  }

  @Override
  public String getCmd() {
    return grab ? LatheHal.CMD_GRAB_ITEM : LatheHal.CMD_RELEASE_ITEM;
  }

  @Override
  public Object getExtra() {
    return "";
  }
}
