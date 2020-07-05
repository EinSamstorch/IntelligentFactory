package machines.real.agv.algorithm;

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

  // 记录AGV运行状态
  private static Map<AID, AgvState> agvStateMap = new ConcurrentHashMap<>();


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

  public enum AgvState {
    FREE,
    BUSY,
    OFFLINE
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
  public static int lockPath(int[] path, AID locker, int index) {
    for (int i = index; i < path.length; i++) {
      int node = path[i];
      if (map[node] != null && map[node] != locker) {
        return i - 1;
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
  public static void unlockPath(int[] path, AID locker, int index) {
    for (int i = 0; i < path.length && i < index; i++) {
      int node = path[i];
      if (map[node] == locker) {
        map[node] = null;
      }
    }
  }
}
