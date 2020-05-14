package machines.real.buffer.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.buffer.BufferHal;
import machines.real.commons.actions.AbstractMachineAction;

/**
 * 进出货动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class InExportAction extends AbstractMachineAction {

  private boolean in;
  private int bufferNo;

  public InExportAction(boolean in, int bufferNo) {
    this.in = in;
    this.bufferNo = bufferNo;
  }

  @Override
  public String getCmd() {
    return in ? BufferHal.CMD_IMPORT_ITEM : BufferHal.CMD_EXPORT_ITEM;
  }

  @Override
  public Object getExtra() {
    JSONObject json = new JSONObject();
    json.put(BufferHal.FIELD_BUFFER_NO, bufferNo);
    return json;
  }
}
