package machines.real.agv.behaviours.simple;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import machines.real.agv.actions.MoveAction;
import machines.real.agv.algorithm.AgvMapUtils2;
import machines.real.agv.algorithm.AgvMapUtils2.AgvState;
import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.commons.actions.MachineAction;

/**
 * 移动AGV.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class AgvMoveBehaviour extends SimpleBehaviour {

  private final AID agv;
  private boolean done = false;
  private final ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
  private final AgvRoutePlan plan;
  private final int to;
  private int[] path = null;
  private Behaviour caller = null;
  private boolean reachEnd = false;

  /**
   * AGV移动行为.
   *
   * @param agv  执行者
   * @param plan 路径规划算法
   * @param to   目的地
   */
  public AgvMoveBehaviour(AID agv, AgvRoutePlan plan, int to) {
    this.agv = agv;
    this.plan = plan;
    this.to = to;
  }

  @Override
  public void onStart() {
    int curLoc = AgvMapUtils2.getAgvLoc(agv);
    // 判断起点是否在自由停车位，若是则需要删边计算路径
    if (AgvMapUtils2.isFreeStop(curLoc)) {
      int[] dir = AgvMapUtils2.getDirection(agv);
      path = plan.getRouteArray(curLoc, to, dir[0], dir[1]);
    } else {
      path = plan.getRouteArray(curLoc, to);
    }
    LoggerUtil.agent.debug(
        String.format("%s in %d to %d, path: %s",
            agv.getLocalName(), curLoc, to, getPathString(path, 0, path.length - 1)));
  }

  @Override
  public int onEnd() {
    // 强制更新AGV位置
    AgvMapUtils2.updateAgvLoc(agv, path[path.length - 1]);
    // 判断终点是否在自由停车位
    if (AgvMapUtils2.isFreeStop(path[path.length - 1])) {
      AgvMapUtils2.saveDirection(agv, new int[]{path[path.length - 1], path[path.length - 2]});
    }
    AgvMapUtils2.unlockPath(path, agv, path.length - 1);
    return super.onEnd();
  }

  @Override
  public void action() {
    int curLoc = AgvMapUtils2.getAgvLoc(agv);
    // 获取AGV当前位于执行路径的哪个位置
    int curIndex = findPathIndex(path, curLoc);
    // 解锁已经行驶过的路径
    AgvMapUtils2.unlockPath(path, agv, curIndex);

    if (caller != null) {
      // 已有运行任务，则检查是否完成
      if (caller.done()) {
        caller = null;
        done = reachEnd;
      }
    } else {
      // 加锁未行驶的路径
      int endIndex = AgvMapUtils2.lockPath(path, agv, curIndex);
      // 是否获取到终点
      if (endIndex == path.length - 1) {
        reachEnd = true;
      } else {
        // 检查被占用点AGV是否处于空闲态，若是则调度其离开
        AID occupy = AgvMapUtils2.getLocationOccupy(path, endIndex + 1);
        // 存在并发释放，所以再次检查occupy != null
        if (occupy != null && AgvMapUtils2.getAgvStateMap().get(occupy).equals(AgvState.FREE)) {
          int freeStop = AgvMapUtils2.getFreeStop(AgvMapUtils2.getAgvLoc(occupy), path, plan);
          Behaviour b = tbf.wrap(new AgvMoveBehaviour(occupy, plan, freeStop) {
            @Override
            public void onStart() {
              super.onStart();
              AgvMapUtils2.updateAgvState(occupy, AgvState.BUSY);
            }

            @Override
            public int onEnd() {
              AgvMapUtils2.updateAgvState(occupy, AgvState.FREE);
              return super.onEnd();
            }
          });
          myAgent.addBehaviour(b);
        }
      }
      // curIndex == endIndex 表示原地等待，无需移动
      if (curIndex < endIndex) {
        // 构建可行驶路径字符串
        String pathStr = getPathString(path, curIndex, endIndex);
        // 执行移动
        MachineAction action = new MoveAction(pathStr);
        caller = tbf.wrap(new ActionCaller(agv, action));
        myAgent.addBehaviour(caller);
        LoggerUtil.agent.info("Call " + agv.getLocalName() + " move: " + pathStr);
      } else if (curIndex == path.length - 1) {
        // 当前已经位于最后一个点，无需运行
        done = true;
        return;
      }
    }
    block(500);
  }

  private int findPathIndex(int[] path, int loc) {
    // 获取AGV当前位于执行路径的哪个位置
    for (int i = 0; i < path.length; i++) {
      if (path[i] == loc) {
        return i;
      }
    }
    return 0;
  }

  private String getPathString(int[] path, int start, int end) {
    // 构建可行驶路径字符串
    StringBuilder sb = new StringBuilder();
    for (int i = start; i <= end; i++) {
      sb.append(path[i]);
      sb.append(",");
    }
    return sb.substring(0, sb.length() - 1);
  }

  @Override
  public boolean done() {
    return done;
  }
}
