package machines.real.commons.hal;

/**
 * BaseHal应该实现的接口.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public interface BaseHal {

  /**
   * 检查对应的hal是否在线.
   *
   * @return 在线true, 离线false.
   */
  boolean checkHalOnline();

  /**
   * 获得回复消息中的extra信息.
   *
   * @return extra信息
   */
  String getExtraInfo();
}
