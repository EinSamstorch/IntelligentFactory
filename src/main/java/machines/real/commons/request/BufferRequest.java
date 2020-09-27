package machines.real.commons.request;

import commons.order.WorkpieceStatus;
import java.io.Serializable;

public class BufferRequest implements Serializable {

  private final boolean importMode;
  private final int bufferNo;
  private final String orderId;
  private final String workpieceId;

  /**
   * 工位台进出货请求.
   *
   * @param bufferNo 工位台编号
   * @param importMode true，工位台入货
   * @param wpInfo 工件信息
   */
  public BufferRequest(int bufferNo, boolean importMode, WorkpieceStatus wpInfo) {
    this.bufferNo = bufferNo;
    this.importMode = importMode;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  public boolean isImportMode() {
    return importMode;
  }

  public int getBufferNo() {
    return bufferNo;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getWorkpieceId() {
    return workpieceId;
  }
}
