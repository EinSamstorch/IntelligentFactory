package machines.real.commons.request;

import commons.order.WorkpieceStatus;
import java.io.Serializable;

/**
 * 运输任务请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvRequest implements Serializable {

  /**
   * 从from地点取货 放置到 to 地点.
   *
   * <p>定义：工位台buffer 从1~24, 具体地图位置需要通过{@link machines.real.agv.algorithm.AgvMapUtils}
   * 若工位台编号为负数，则代表它是仓库的入/出口
   */
  private int fromBuffer;

  private int toBuffer;
  private WorkpieceStatus wpInfo;
  private final String orderId;
  private final String workpieceId;

  /**
   * AGV搬运请求.
   *
   * @param fromBuffer 工件搬运起始位置
   * @param toBuffer 工件搬运终点位置
   * @param wpInfo 工件具体信息
   */
  public AgvRequest(int fromBuffer, int toBuffer, WorkpieceStatus wpInfo) {
    this.fromBuffer = fromBuffer;
    this.toBuffer = toBuffer;
    this.wpInfo = wpInfo;
    this.orderId = wpInfo.getOrderId();
    this.workpieceId = wpInfo.getWorkpieceId();
  }

  public int getFromBuffer() {
    return fromBuffer;
  }

  public int getToBuffer() {
    return toBuffer;
  }

  public WorkpieceStatus getWpInfo() {
    return wpInfo;
  }
}
