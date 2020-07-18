package machines.real.commons.request;

import commons.order.WorkpieceStatus;

import java.io.Serializable;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseItemMoveRequest implements Serializable {

  public static final String LANGUAGE = "MOVE";
  private final int itemPosition;
  private final boolean in;
  private final String orderId;
  private final String workpieceId;


  public WarehouseItemMoveRequest(int itemPosition, boolean in, WorkpieceStatus wpInfo) {
    this.in = in;
    this.itemPosition = itemPosition;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  public int getItemPosition() {
    return itemPosition;
  }

  public boolean isIn() {
    return in;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getWorkpieceId() {
    return workpieceId;
  }
}
