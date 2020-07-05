package machines.real.agv.algorithm.algs4;

/**
 * 加权有向边.
 *
 * @author <a href = "mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @since 1.8
 */
public class DirectedEdge {

  /**
   * 边的起点.
   */
  private final int from;
  /**
   * 边的终点.
   */
  private final int to;
  /**
   * 边的权重.
   */
  private final double weight;

  /**
   * 生成权重边.
   *
   * @param from   起点
   * @param to     终点
   * @param weight 权重
   */
  public DirectedEdge(int from, int to, double weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  /**
   * 返回边的权重.
   *
   * @return 边的权重
   */
  public double weight() {
    return weight;
  }

  /**
   * 返回边的起点.
   *
   * @return 边的起点
   */
  public int from() {
    return from;
  }

  /**
   * 返回边的终点.
   *
   * @return 边的终点
   */
  public int to() {
    return to;
  }

  /**
   * 对象的字符串表示.
   *
   * @return 对象的字符串表示
   */
  @Override
  public String toString() {
    return String.format("%d -> %d %.2f", from, to, weight);
  }
}
