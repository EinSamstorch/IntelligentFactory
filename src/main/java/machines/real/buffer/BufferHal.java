package machines.real.buffer;

import machines.real.commons.hal.BaseHal;

public interface BufferHal extends BaseHal {

  String CMD_EXPORT_ITEM = "export_item";
  String CMD_IMPORT_ITEM = "import_item";
  String FIELD_BUFFER_NO = "buffer_no";
}
