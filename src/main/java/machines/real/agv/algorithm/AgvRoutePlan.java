package machines.real.agv.algorithm;

/**
 * 路径规划.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface AgvRoutePlan {

  /**
   * 计算Agv路径.
   *
   * @param from 起点
   * @param to   终点
   * @return 以逗号分割的路径点
   */
  String getRoute(int from, int to);

  /**
   * 获取两个点的距离.
   *
   * @param from 点A
   * @param to   点B
   * @return AB距离
   */
  int getDistance(int from, int to);

  /**
   * 获取边界点坐标数组.
   *
   * @return 边界点坐标数组
   */
  int[] getEdgeNodes();
}
