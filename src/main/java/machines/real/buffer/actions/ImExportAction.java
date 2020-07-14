package machines.real.buffer.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.buffer.BufferHal;
import machines.real.commons.actions.AbstractMachineAction;
import machines.real.commons.request.BufferRequest;

/**
 * 进出货动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ImExportAction extends AbstractMachineAction {

  private final BufferRequest request;

  /**
   * 工位台进出货动作.
   *
   * @param request 进出货请求
   */
  public ImExportAction(BufferRequest request) {
    this.orderId = request.getOrderId();
    this.workpieceId = request.getWorkpieceId();
    this.request = request;
  }

  @Override
  public String getCmd() {
    return request.isImportMode() ? BufferHal.CMD_IMPORT_ITEM : BufferHal.CMD_EXPORT_ITEM;
  }

  @Override
  public Object getExtra() {
    JSONObject json = new JSONObject();
    json.put(BufferHal.FIELD_BUFFER_NO, request.getBufferNo());
    return json;
  }
}
