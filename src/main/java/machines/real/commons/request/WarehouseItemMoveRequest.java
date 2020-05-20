package machines.real.commons.request;

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
  private int itemPosition;
  private boolean in;

  public WarehouseItemMoveRequest(int itemPosition, boolean in) {
    this.in = in;
    this.itemPosition = itemPosition;
  }

  public int getItemPosition() {
    return itemPosition;
  }

  public boolean isIn() {
    return in;
  }
}
