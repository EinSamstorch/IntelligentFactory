package machines.real.agv;

import machines.real.commons.hal.BaseHal;

/**
 * 多AGV系统 agv agent硬件层.
 *
 * <p>分离出多个功能模块.
 *
 * @author junfeng
 */
public interface MultiAgvHal extends BaseHal {

  /**
   * 运输工件.
   *
   * @param path 移动路径
   * @return 成功true
   */
  boolean move(String path);

  /**
   * 输出货物.
   *
   * @return 输出完成, 返回true
   */
  boolean exportItem();

  /**
   * 接收货物.
   *
   * @return 货物接收完成, 返回true
   */
  boolean importItem();

  /**
   * 获取Agv当前位置.
   *
   * @return 位置标号
   */
  int getPosition();
}
