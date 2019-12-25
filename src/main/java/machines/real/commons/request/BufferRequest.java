package machines.real.commons.request;

import java.io.Serializable;

public class BufferRequest implements Serializable {

  private final int op;
  private final int bufferNo;

  public BufferRequest(int op, int bufferNo) {
    this.op = op;
    this.bufferNo = bufferNo;
  }

  public int getOp() {
    return op;
  }

  public int getBufferNo() {
    return bufferNo;
  }
}
