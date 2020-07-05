package machines.real.agv.algorithm.algs4;

import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.List;

/**
 * 加权有向图.
 *
 * @author <a href = "mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @since 1.8
 */
public class EdgeWeightedDigraph {

  private static final String NEWLINE = System.getProperty("line.separator");

  /**
   * 顶点总数.
   */
  private int vertices;
  /**
   * 边的总数.
   */
  private int edges;
  /**
   * 领接表.
   */
  private final List<DirectedEdge>[] adj;
  public final List<DirectedEdge> deletedEdges = new LinkedList<>();

  /**
   * 含有v个顶点的空有向图.
   *
   * @param vertices 顶点总数
   */
  @SuppressWarnings("unchecked")
  public EdgeWeightedDigraph(int vertices) {
    this.vertices = vertices;
    this.edges = 0;
    adj = new LinkedList[vertices];
    for (int v = 0; v < vertices; v++) {
      adj[v] = new LinkedList<>();
    }
  }

  /**
   * 从输入流中读取图的构造函数.
   *
   * @param in 输入流
   */
  public EdgeWeightedDigraph(In in) {
    this(in.readInt());
    int edges = in.readInt();
    for (int i = 0; i < edges; i++) {
      int from = in.readInt();
      int to = in.readInt();
      // 检查点的有效性
      validateVertex(from);
      validateVertex(to);
      double weight = in.readDouble();
      DirectedEdge edge = new DirectedEdge(from, to, weight);
      addEdge(edge);
    }
  }

  /**
   * 返回顶点总数.
   *
   * @return 返回顶点总数
   */
  public int getVertices() {
    return vertices;
  }

  /**
   * 返回边的总数.
   *
   * @return 返回顶点总数
   */
  public int getEdges() {
    return edges;
  }

  /**
   * 将边e添加到该有向图中.
   *
   * @param e 加权有向边
   */
  public void addEdge(DirectedEdge e) {
    adj[e.from()].add(e);
    edges++;
  }

  /**
   * 根据给定的起点和终点删除一条边.
   *
   * @param from 起点
   * @param to   终点
   */
  public void deleteEdge(int from, int to) {
    validateVertex(from);
    validateVertex(to);
    for (DirectedEdge edge : adj[from]) {
      if (edge.to() == to) {
        deletedEdges.add(edge);
        deleteAnEdge(edge);
        break;
      }
    }
  }

  /**
   * 删除一条指定的边.
   *
   * @param e 指定的边
   */
  public void deleteAnEdge(DirectedEdge e) {
    adj[e.from()].remove(e);
    edges--;
  }

  /**
   * 根据给定的起点和终点恢复边.
   *
   * @param from 起点
   * @param to   终点
   */
  public void restoreEdge(int from, int to) {
    validateVertex(from);
    validateVertex(to);
    for (DirectedEdge edge : deletedEdges) {
      if (edge.to() == to && edge.from() == from) {
        deletedEdges.remove(edge);
        addEdge(edge);
      }
      break;
    }
  }

  /**
   * 恢复所有顶点.
   */
  public void restoreAllEdges() {
    for (DirectedEdge edge : deletedEdges) {
      addEdge(edge);
    }
  }

  /**
   * 检查顶点的有效性.
   *
   * @param v 顶点
   */
  private void validateVertex(int v) {
    if (v < 0 || v >= vertices) {
      throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (vertices - 1));
    }
  }

  /**
   * 返回顶点v的所有边.
   *
   * @param v 顶点
   * @return 该顶点的所有边
   */
  public Iterable<DirectedEdge> adj(int v) {
    return adj[v];
  }

  /**
   * 返回所有边的集合.
   *
   * @return 所有边的集合
   */
  public Iterable<DirectedEdge> edges() {
    List<DirectedEdge> list = new LinkedList<>();
    for (int v = 0; v < vertices; v++) {
      list.addAll(adj[v]);
    }
    return list;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(vertices).append(" ").append(edges).append(NEWLINE);
    for (int v = 0; v < vertices; v++) {
      s.append(v).append(": ");
      for (DirectedEdge e : adj[v]) {
        s.append(e).append("  ");
      }
      s.append(NEWLINE);
    }
    return s.toString();
  }
}
