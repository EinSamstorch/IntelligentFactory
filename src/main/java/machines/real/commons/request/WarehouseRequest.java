package machines.real.commons.request;

import java.io.Serializable;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseRequest implements Serializable {

  private int itemPosition;
  private boolean importMode;

  public WarehouseRequest(int itemPosition, boolean importMode) {
    this.itemPosition = itemPosition;
    this.importMode = importMode;
  }

  public int getItemPosition() {
    return itemPosition;
  }

  public boolean isImportMode() {
    return importMode;
  }
}
