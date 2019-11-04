package machines.real.warehouse;

/**
 * 仓库Hal应该实现的接口.
 * @author junfeng
 */
public interface WarehouseHal {

  /**
   * 将货物从from移动至to.
   *
   * @param from 起点
   * @param to 终点
   * @return 成功true，失败false
   */
  boolean moveItem(int from, int to);
}
