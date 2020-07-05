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
  String getRouteString(int from, int to);

  /**
   * 计算AGV路径.
   *
   * @param from 起点
   * @param to   终点
   * @return 路径数组
   */
  int[] getRouteArray(int from, int to);

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

  /**
   * 获取工位台在地图点里坐标位置.
   *
   * @return 工位台标号为数组索引，数组值为所在地图点，工位台标号从1开始
   */
  int[] getBufferLocation();

  /**
   * 判断该点是否是工位台.
   *
   * @param loc 地图点
   * @return 是否工位台
   */
  boolean isBuffer(int loc);

  /**
   * 获取工位台编号.
   *
   * @param loc 工位台位置
   * @return 工位台编号, 从1开始
   */
  int getBufferNo(int loc);

  /**
   * 获取工位台位置.
   *
   * @param bufferNo 工位台编号, 从1开始
   * @return 工位台位置
   */
  int getBufferLoc(int bufferNo);

  /**
   * 定义工位台在地图中的位置.
   *
   * @param bufferNo 工位台索引
   * @return 工位台的地图索引
   */
  int getBufferMap(int bufferNo);
}
