package machines.real.agv.actions;

import machines.real.agv.MultiAgvHal;
import machines.real.agv.algorithm.AgvRoutePlan;

/**
 * Agv移动命令.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class MoveAction extends AbstractMachineAction {

  private int from;
  private int to;
  private AgvRoutePlan plan;

  /**
   * 构造移动动作.
   *
   * @param from 起点
   * @param to   终点
   * @param plan 规划算法
   */
  public MoveAction(int from, int to, AgvRoutePlan plan) {
    this.from = from;
    this.to = to;
    this.plan = plan;
  }

  @Override
  public String getCmd() {
    return MultiAgvHal.CMD_MOVE;
  }

  @Override
  public Object getExtra() {
    return plan.getRoute(from, to);
  }
}
