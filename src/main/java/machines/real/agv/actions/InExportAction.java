package machines.real.agv.actions;

import machines.real.agv.MultiAgvHal;

/**
 * Agv进出货物.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class InExportAction extends AbstractMachineAction {

  private boolean in;

  public InExportAction(boolean in) {
    this.in = in;
  }

  @Override
  public String getCmd() {
    return in ? MultiAgvHal.CMD_IMPORT_ITEM : MultiAgvHal.CMD_EXPORT_ITEM;
  }

  @Override
  public Object getExtra() {
    return "";
  }
}
