package machines.real.agv.algorithm.algs4;

import edu.princeton.cs.algs4.IndexMinPQ;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * dijkstra算法求出的最短路径.
 *
 * @author <a href = "mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @since 1.8
 */
public class DijkstraSP implements SP {

  /**
   * edgeTo[v]的值为树中连接v和它的父节点的边. 也就是从起点s到顶点v的最短路径上的最后一条边
   */
  private final DirectedEdge[] edgeTo;
  /**
   * 从s到v的最短路径的长度.
   */
  private final double[] distTo;
  private final IndexMinPQ<Double> pq;

  /**
   * dijkstra算法求最短路径.
   *
   * @param graph 加权有向图
   * @param s     起点
   */
  public DijkstraSP(EdgeWeightedDigraph graph, int s) {
    edgeTo = new DirectedEdge[graph.getVertices()];
    distTo = new double[graph.getVertices()];
    pq = new IndexMinPQ<>(graph.getVertices());

    for (int v = 0; v < graph.getVertices(); v++) {
      distTo[v] = Double.POSITIVE_INFINITY;
    }
    distTo[s] = 0.0;

    pq.insert(s, 0.0);
    while (!pq.isEmpty()) {
      relax(graph, pq.delMin());
    }
  }

  /**
   * 顶点的松弛.
   *
   * @param graph 加权有向图
   * @param v     要松弛的顶点
   */
  private void relax(EdgeWeightedDigraph graph, int v) {
    for (DirectedEdge e : graph.adj(v)) {
      int w = e.to();
      if (distTo[w] > distTo[v] + e.weight()) {
        distTo[w] = distTo[v] + e.weight();
        edgeTo[w] = e;
        if (pq.contains(w)) {
          pq.changeKey(w, distTo[w]);
        } else {
          pq.insert(w, distTo[w]);
        }
      }
    }
  }

  /**
   * 返回起点s到顶点v的最短路径的长度.
   *
   * @param v 顶点v
   * @return 起点s到顶点v的最短路径的长度
   */
  @Override
  public double distTo(int v) {
    return distTo[v];
  }

  /**
   * 判断是否可以到达顶点v.
   *
   * @param v 顶点v
   * @return 可以到达，返回true，否则返回false
   */
  @Override
  public boolean hasPathTo(int v) {
    return distTo[v] < Double.POSITIVE_INFINITY;
  }

  /**
   * 返回到顶点v的路径.
   *
   * @param v 顶点v
   * @return 到顶点v的路径
   */
  @Override
  public Iterable<DirectedEdge> pathTo(int v) {
    if (!hasPathTo(v)) {
      return null;
    }
    Deque<DirectedEdge> path = new ArrayDeque<>();
    for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
      path.push(e);
    }
    return path;
  }

  private Deque<Integer> getPath(int v) {
    if (!hasPathTo(v)) {
      return null;
    }
    Deque<Integer> path = new ArrayDeque<>();
    for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
      path.addFirst(e.from());
    }
    path.add(v);
    return path;
  }

  /**
   * 返回到顶点v的路径.
   *
   * @param v 目的地
   * @return 路径数组
   */
  public int[] getPathIntArray(int v) {
    Deque<Integer> path = getPath(v);
    if (path == null) {
      return new int[]{v};
    }
    return path.stream().mapToInt(Integer::intValue).toArray();
  }

  /**
   * 返回到顶点v的路径.
   *
   * @param v 目的地
   * @return 路径数组
   */
  public String[] getPathStringArray(int v) {
    Deque<Integer> path = getPath(v);
    if (path == null) {
      return new String[]{String.valueOf(v)};
    }
    return path.stream().map(String::valueOf).toArray(String[]::new);
  }
}
