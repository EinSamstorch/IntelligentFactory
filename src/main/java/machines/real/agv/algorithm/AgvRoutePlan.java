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
   * 计算AGV路径，且删除某个边.
   *
   * @param from    起点
   * @param to      终点
   * @param delFrom 删除边的起点
   * @param delTo   删除边的终点
   * @return 路径数组
   */
  int[] getRouteArray(int from, int to, int delFrom, int delTo);

  /**
   * 获取两个点的距离.
   *
   * @param from 点A
   * @param to   点B
   * @return AB距离
   */
  int getDistance(int from, int to);

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
   * 获取工位台/仓库位置.
   *
   * <p>根据工位台编号寻找工位台在地图中的位置，仓库的工位台编号是负数，表明仓库出入口在地图的位置.
   *
   * @param bufferNo 工位台编号, 从1开始
   * @return 工位台位置
   */
  int getBufferLoc(int bufferNo);

}
