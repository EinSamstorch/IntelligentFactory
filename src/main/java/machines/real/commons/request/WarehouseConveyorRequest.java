package machines.real.commons.request;

import java.io.Serializable;

/**
 * 仓库传送带运动请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class WarehouseConveyorRequest implements Serializable {

  public static final String LANGUAGE = "CONVEYOR";
  private boolean importMode;

  public WarehouseConveyorRequest(boolean importMode) {
    this.importMode = importMode;
  }

  public boolean isImportMode() {
    return importMode;
  }
}
