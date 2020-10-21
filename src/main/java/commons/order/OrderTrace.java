package commons.order;

import java.io.Serializable;

/**
 * 订单追溯类，储存订单信息.
 *
 * @author Ein
 */
public class OrderTrace implements Serializable {
  /** 订单id. */
  private String orderId;
  /** 工件id. */
  private String workpieceId;
  /** 工位台号：1-24，0表示不存在. */
  private int bufferId;
  /** 工件状态： 仓库/agv/车/铣. */
  private String owner;
  /** 责任人. */
  private String duty;

  /** 额外信息： 仓库:工件位置 AGV：path. */
  private String extra;

  /**
   * 构造函数.
   *
   * @param orderId 订单id
   * @param workpieceId 工件id
   * @param bufferId 工位台号
   * @param owner 拥有者
   * @param extra 额外信息
   */
  public OrderTrace(String orderId, String workpieceId, int bufferId, String owner, String extra) {
    this.orderId = orderId;
    this.workpieceId = workpieceId;
    this.bufferId = bufferId;
    this.owner = owner;
    this.extra = extra;
  }

  public String getWorkpieceId() {
    return workpieceId;
  }

  public int getBufferId() {
    return bufferId;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getOwner() {
    return owner;
  }

  public String getDuty() {
    return duty;
  }

  public String getExtra() {
    return extra;
  }
}
