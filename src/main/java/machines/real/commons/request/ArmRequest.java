package machines.real.commons.request;

import java.io.Serializable;

/**
 * move item.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmRequest implements Serializable {

  private final String from;
  private final String to;
  private final String goodsId;
  private final Integer step;

  /**
   * 机械手搬运请求.
   *
   * @param from    起始位置
   * @param to      终点位置
   * @param goodsId 货物类型
   * @param step    搬运步骤, 铣床与检测仪该参数均为0, 车床该参数与具体装夹过程相关, 为-1时代表一组装夹动作完成
   */
  public ArmRequest(String from, String to, String goodsId, Integer step) {
    this.from = from;
    this.to = to;
    this.goodsId = goodsId;
    this.step = step;
  }

  public ArmRequest(String from, String to, String goodsId) {
    this(from, to, goodsId, 0);
  }

  /**
   * 根据上料请求,生成对应的下料请求, 即交换from与to.
   *
   * @param request 上料请求
   * @return 下料请求
   */
  public static ArmRequest unloadRequest(ArmRequest request) {
    return new ArmRequest(
        request.to,
        request.from,
        request.goodsId,
        0);
  }

  /**
   * 根据上料请求,生成对应的掉转装夹请求, 即从to到to.
   *
   * @param request 上料请求
   * @return 掉转装夹请求
   */
  public static ArmRequest reverseRequest(ArmRequest request) {
    return new ArmRequest(
        request.to,
        request.to,
        request.goodsId,
        0);
  }

  /**
   * 根据上一步请求,生成对应的下一步, 即对step+1.
   *
   * @param request 上一步请求
   * @return 下一步请求
   */
  public static ArmRequest nextStep(ArmRequest request) {
    return new ArmRequest(
        request.from,
        request.to,
        request.goodsId,
        request.step + 1);
  }

  /**
   * 根据动作请求,生成该组的动作的终结请求,即step = -1.
   *
   * @param request 该组动作请求
   * @return 终结动作请求
   */
  public static ArmRequest endStep(ArmRequest request) {
    return new ArmRequest(
        request.from,
        request.to,
        request.goodsId,
        -1
    );
  }

  /**
   * 复制一个新的请求.
   *
   * @param request 待复制请求
   */
  public ArmRequest copyOf(ArmRequest request) {
    return new ArmRequest(request.from, request.to, request.goodsId, request.step);
  }

  public String getFrom() {
    return from;
  }

  public String getTo() {
    return to;
  }

  public String getGoodsId() {
    return goodsId;
  }

  public Integer getStep() {
    return step;
  }

  @Override
  public String toString() {
    return String.format("FROM:%s;TO:%s", from, to);
  }
}
