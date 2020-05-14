package machines.real.commons.request;

import java.io.Serializable;

public class BufferRequest implements Serializable {

  private final boolean importMode;
  private final int bufferNo;

  public BufferRequest(int bufferNo, boolean importMode) {
    this.bufferNo = bufferNo;
    this.importMode = importMode;
  }

  public boolean isImportMode() {
    return importMode;
  }

  public int getBufferNo() {
    return bufferNo;
  }
}
