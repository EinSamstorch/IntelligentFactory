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

  private int itemPosition;

  public WarehouseItemMoveRequest(int itemPosition) {
    this.itemPosition = itemPosition;
  }

  public int getItemPosition() {
    return itemPosition;
  }

}
