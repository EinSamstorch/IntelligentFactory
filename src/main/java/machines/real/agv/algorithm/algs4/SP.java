package machines.real.agv.algorithm.algs4;

/**
 * 最短路径的api.
 *
 * @author <a href = "mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @since 1.8
 */
public interface SP {

  /**
   * 从顶点s到v的距离，如果不存在则路径为无穷大.
   *
   * @param v 顶点v
   * @return 距离
   */
  double distTo(int v);

  /**
   * 是否存在从顶点s到v的路径.
   *
   * @param v 顶点v
   * @return 存在返回true，否则返回false
   */
  boolean hasPathTo(int v);

  /**
   * 从顶点s到v的路径，如果不存在则为null.
   *
   * @param v 顶点v
   * @return 从顶点s到v的路径，如果不存在则为null
   */
  Iterable<DirectedEdge> pathTo(int v);
}
