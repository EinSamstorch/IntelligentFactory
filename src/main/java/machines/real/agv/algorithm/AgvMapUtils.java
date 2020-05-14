package machines.real.agv.algorithm;

import jade.core.AID;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Agv地图工具集合.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvMapUtils {

  private static volatile boolean[] nodeMap = new boolean[60];
  private static Map<AID, Integer> locationMap = new ConcurrentHashMap<>();

  /**
   * 获取地图点状态.
   *
   * @return 地图点数组， true表示该位置有车存在
   */
  public static boolean[] getNodeMap() {
    return nodeMap;
  }

  /**
   * 获取agent位置.
   *
   * @return Map id, pos
   */
  public static Map<AID, Integer> getLocationMap() {
    return locationMap;
  }

  /**
   * 设置当前agent位置.
   *
   * @param aid id
   * @param pos 位置
   */
  public static void setNodeMap(AID aid, int pos) {
    Integer oldLoc = locationMap.get(aid);
    if (oldLoc != null) {
      nodeMap[oldLoc] = false;
    }
    locationMap.put(aid, pos);
    nodeMap[pos] = true;
  }

  /**
   * 获取pos位置上agent id.
   *
   * @param pos 地图位置
   * @return aid, null表示当前点无agent
   */
  public static AID getConflictAid(int pos) {
    for (Entry<AID, Integer> entry : locationMap.entrySet()) {
      if (pos == entry.getValue()) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * 获取空的边界点.
   *
   * @param edgeNodes     边界点坐标
   * @param neighbourSize 相领位置坐标差
   * @return 空边界点位置
   */
  public static int getFreeEdgeNode(int[] edgeNodes, int neighbourSize) {
    for (int node : edgeNodes) {
      if (nodeMap[node]
          || (node - neighbourSize >= 0 && nodeMap[node - neighbourSize])
          || (node + neighbourSize < nodeMap.length && nodeMap[node + neighbourSize])) {
        continue;
      }
      return node;
    }
    return -1;
  }

  /**
   * 当前路径是否有冲突.
   *
   * @param path 路径点
   * @return -1代表无冲突， 返回第一个有冲突的坐标
   */
  public static int conflictNode(int[] path) {
    for (int i : path) {
      if (nodeMap[i]) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 转换地图点->工位台编号.
   *
   * @param mapNode        地图点
   * @param bufferLocation 工位台对应的地图点信息数组
   * @return 工位台编号， -1表示未找到
   */
  public static int getBufferNo(int mapNode, int[] bufferLocation) {
    for (int i = 1; i < bufferLocation.length; i++) {
      if (mapNode == bufferLocation[i]) {
        return i;
      }
    }
    return -1;
  }
}
