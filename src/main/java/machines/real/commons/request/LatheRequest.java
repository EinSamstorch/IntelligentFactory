package machines.real.commons.request;

import commons.order.WorkpieceStatus;
import java.io.Serializable;
import org.springframework.cglib.beans.BulkBean;

/**
 * 车床夹紧工件请求.
 *
 * @author <a href="mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @date: 2020/9/29 18:53
 */
public class LatheRequest implements Serializable {

  private final boolean grab;
  private final String orderId;
  private final String workpieceId;

  /**
   * 车床夹紧请求.
   *
   * @param grab 是否夹紧
   * @param wpInfo 工件信息
   */
  public LatheRequest(boolean grab, WorkpieceStatus wpInfo) {
    this.grab = grab;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  public String getOrderId() {
    return orderId;
  }

  public String getWorkpieceId() {
    return workpieceId;
  }

  public boolean isGrab() {
    return grab;
  }
}
