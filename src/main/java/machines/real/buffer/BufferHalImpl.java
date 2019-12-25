package machines.real.buffer;

import com.alibaba.fastjson.JSONObject;
import machines.real.commons.hal.BaseHalImpl;

public class BufferHalImpl extends BaseHalImpl implements BufferHal {

  private static final String CMD_EXPORT_ITEM = "export_item";
  private static final String CMD_IMPORT_ITEM = "import_item";
  private static final String FIELD_BUFFER_NO = "buffer_no";

  @Override
  public boolean importItem(int bufferNo) {
    JSONObject extra = new JSONObject();
    extra.put(FIELD_BUFFER_NO, bufferNo);
    return executeCmd(CMD_IMPORT_ITEM, extra);
  }

  @Override
  public boolean exportItem(int bufferNo) {
    JSONObject extra = new JSONObject();
    extra.put(FIELD_BUFFER_NO, bufferNo);
    return executeCmd(CMD_EXPORT_ITEM, extra);
  }
}
