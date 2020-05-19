package machines.real.warehouse.behaviours.simple;

import jade.core.behaviours.OneShotBehaviour;
import machines.real.warehouse.WarehouseHal;

/**
 * 仓库传送带进出货.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ConveyorActionBehaviour extends OneShotBehaviour {

  private boolean importMode;
  private WarehouseHal hal;

  /**
   * 控制传送带进出货.
   *
   * @param importMode true输入模式, false输出模式
   * @param hal        硬件层
   */
  public ConveyorActionBehaviour(boolean importMode, WarehouseHal hal) {
    this.importMode = importMode;
    this.hal = hal;
  }

  @Override
  public void action() {
    while (!hal.conveyorControl(importMode)) {
      block(1000);
    }
  }
}
