package rpc.services;

/**
 * AGV路径规划.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface AgvRoutePlan {

  /**
   * 路径规划.
   *
   * @param start 起点编号
   * @param end   终点编号
   * @return 以逗号分割的路径点字符串
   */
  String plan(int start, int end);
}
