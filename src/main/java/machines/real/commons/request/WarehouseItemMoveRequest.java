package machines.real.commons.request;

import commons.order.WorkpieceStatus;
import java.io.Serializable;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable.BinaryOp.Or;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class WarehouseItemMoveRequest implements Serializable {

  public static final String LANGUAGE = "MOVE";
  private int itemPosition;
  private boolean in;
  private final String orderId;
  private final String workpieceId;

  /**
   * 仓库移动工件行为.
   *
   * @param itemPosition 工件位置
   * @param in 为false时，仓库出货，agv进货； 为true时，仓库入货，agv出货
   * @param wpInfo 工件信息
   */
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
