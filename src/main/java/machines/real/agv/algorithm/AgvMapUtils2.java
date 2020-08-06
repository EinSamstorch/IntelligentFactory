package machines.real.agv.algorithm;

import commons.tools.LoggerUtil;
import jade.core.AID;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AGV MAP工具类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class AgvMapUtils2 {

  // 地图内共有33个RFID， 索引表示地图点位置，值表示被谁占有, null则无人占有
  private static volatile AID[] map = new AID[33];
  // 记录AGV实时位置
  private static Map<AID, OptionalInt> agvLocations = new ConcurrentHashMap<>();

  // 记录AGV逻辑位置，避免实时位置的更新延迟导致路径规划错误
  private static Map<AID, OptionalInt> agvLocationsLogic = new ConcurrentHashMap<>();

  // 记录AGV运行状态
  private static Map<AID, AgvState> agvStateMap = new ConcurrentHashMap<>();

  // 自由停车点位置
  private static int[] freeStops = {28, 29, 30, 31, 32};

  // 不可停车点
  private static int[] nonStops = {1, 3, 5, 8, 10, 13, 16, 19, 22, 25};

  private static Map<AID, int[]> agvDirection = new ConcurrentHashMap<>();

  public static Map<AID, AgvState> getAgvStateMap() {
    return agvStateMap;
  }

  public static void updateAgvLoc(AID agv, int loc) {
    agvLocations.put(agv, OptionalInt.of(loc));
  }

  public static int getAgvLoc(AID agv) {
    return agvLocations.get(agv).orElse(-1);
  }

  public static void updateAgvState(AID agv, AgvState state) {
    agvStateMap.put(agv, state);
  }

  private static Map<Integer, Integer> combines = new ConcurrentHashMap<>();

  static {
    // combines two buffers of the same machine
    combines.put(11, 12);
    combines.put(14, 15);
    combines.put(17, 18);
    combines.put(20, 21);
    combines.put(23, 24);
    combines.put(26, 27);
  }

  /**
   * 获取到共用地图点.
   *
   * @param loc 地图点
   * @return 如果无绑定关系，返回自身；如果有绑定关系，返回绑定地图点
   */
  private static int getCombination(int loc) {
    return combines.getOrDefault(loc, loc);
  }

  public enum AgvState {
    FREE,
    BUSY,
    OFFLINE
  }

  /**
   * 尝试设置agv繁忙状态.
   *
   * @param agv agv
   * @return 成功与否
   */
  public static boolean tryLockAgvState(AID agv) {
    if (AgvState.FREE.equals(agvStateMap.get(agv))) {
      synchronized (agv) {
        if (AgvState.FREE.equals(agvStateMap.get(agv))) {
          agvStateMap.put(agv, AgvState.BUSY);
          return true;
        } else {
          return false;
        }
      }
    }
    return false;
  }

  public static int getAgvLocLogic(AID agv) {
    return agvLocationsLogic.get(agv).orElse(-1);
  }

  public static void updateAgvLocLogic(AID agv, int loc) {
    agvLocationsLogic.put(agv, OptionalInt.of(loc));
  }

  /**
   * 寻找地图点占有者，且非当前搜索者.
   *
   * @param path  路径点数组
   * @param agv   当前搜索者
   * @param index 搜索起点
   * @return 占有者，null表示无人占有
   */
  public static AID getLocationOccupy(int[] path, AID agv, int index) {
    for (int i = index; i < path.length; i++) {
      int node = getCombination(path[i]);
      if (map[node] != null && map[node] != agv) {
        return map[node];
      }
    }
    return null;
  }

  /**
   * 初始化AGV逻辑位置.
   *
   * @param agv AGV
   * @param loc 位置点
   */
  public static void initAgvLocLogic(AID agv, int loc) {
    if (!agvLocationsLogic.containsKey(agv)) {
      LoggerUtil.commonTools.info(agv.getLocalName() + " init logic pos: " + loc);
      map[getCombination(loc)] = agv;
      agvLocationsLogic.put(agv, OptionalInt.of(loc));
    }
  }

  /**
   * 锁住路径.
   *
   * <p>尝试一个一个点进行加锁，如果当前点加锁失败，则返回当前索引-1，以表示路径点数组[index, i-1]均上锁完成
   *
   * @param path   路径点数组
   * @param locker 加锁人
   * @param index  路径点数组加锁起点(包括)
   * @return 路径点数组的索引, 表示上锁到数组内的某个部分
   */
  public static synchronized int lockPath(int[] path, AID locker, int index) {
    for (int i = index; i < path.length; i++) {
      int node = getCombination(path[i]);
      if (map[node] != null && map[node] != locker) {
        return isNonStop(path[i - 1]) ? i - 2 : i - 1;
      }
      map[node] = locker;
    }
    return path.length - 1;
  }

  /**
   * 解锁路径.
   *
   * <p>尝试一个一个解锁, 对路径点数组[0 ... index) 解除锁定
   *
   * @param path   路径点数组
   * @param locker 加锁人
   * @param index  解锁范围（不包括）
   */
  public static synchronized void unlockPath(int[] path, AID locker, int index) {
    for (int i = 0; i < path.length && i < index; i++) {
      int node = getCombination(path[i]);
      if (node == path[i] && map[node] == locker) {
        map[node] = null;
      }
    }
    // 避免下述情况：
    // 点a 与 点b 绑定，路径为从b->a，
    // 当到达a时，解锁路径点b。因为a与b绑定，所以解锁b的同时自动解锁a.
    // 故完成解锁后，对剩下的部分进行重新上锁.
    lockPath(path, locker, index);
  }


  /**
   * 获得一个最近的空位点.
   *
   * @param avoid 避开点数组
   * @return 空位点，-1表示没有
   */
  public static int getFreeStop(int cur, int[] avoid, AgvRoutePlan plan) {
    int distance = Integer.MAX_VALUE;
    int choice = -1;
    for (int stop : freeStops) {
      if (map[stop] != null) {
        continue;
      }
      boolean conflict = false;
      for (int a : avoid) {
        if (a == stop) {
          conflict = true;
          break;
        }
      }
      if (!conflict) {
        int dist = plan.getDistance(cur, stop);
        if (dist < distance) {
          choice = stop;
          distance = dist;
        }
      }
    }
    return choice;
  }

  /**
   * 检查是否是自由停车点.
   *
   * @param loc 位置点
   * @return 是否自由停车点
   */
  public static boolean isFreeStop(int loc) {
    for (int f : freeStops) {
      if (f == loc) {
        return true;
      }
    }
    return false;
  }

  /**
   * 储存agv方位.
   *
   * @param agv       agv
   * @param direction {end, pre_end} pre_end表示行经停车点的前一个位置
   */
  public static void saveDirection(AID agv, int[] direction) {
    agvDirection.put(agv, direction);
  }

  /**
   * 获取AGV方向.
   *
   * @param agv agv
   * @return {end, pre_end} pre_end表示行经停车点的前一个位置
   */
  public static int[] getDirection(AID agv) {
    return agvDirection.get(agv);
  }

  private static boolean isNonStop(int loc) {
    for (int nonStop : nonStops) {
      if (nonStop == loc) {
        return true;
      }
    }
    return false;
  }
}
