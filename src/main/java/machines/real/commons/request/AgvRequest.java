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
   */
  private int from;
  private int to;
  private WorkpieceStatus wpInfo;

  /**
   * AGV搬运请求.
   *
   * @param from   工件搬运起始位置
   * @param to     工件搬运终点位置
   * @param wpInfo 工件具体信息
   */
  public AgvRequest(int from, int to, WorkpieceStatus wpInfo) {
    this.from = from;
    this.to = to;
    this.wpInfo = wpInfo;
  }

  public int getFrom() {
    return from;
  }

  public int getTo() {
    return to;
  }

  public WorkpieceStatus getWpInfo() {
    return wpInfo;
  }
}
