package machines.real.agv.algorithm;

import jade.core.AID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Agv地图工具集合.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvMapUtils {

  /**
   * 硬编码了地图点个数， 可以通过Spring 反射注入AgvRoutePlan 获取实例，获取地图大小.
   */
  private static volatile boolean[] nodeMap = new boolean[60 + 1];
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
  public static void setAgvLoc(AID aid, int pos) {
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
   * @param target 目的地地点
   * @return 空边界点位置
   */
  public static int getFreeEdgeNode(int[] edgeNodes, int neighbourSize, int target) {
    int val;
    for (int node : edgeNodes) {
      // 该点被占/该点是目标地点/相邻或相邻2位是目标点/相邻被占/隔两个相邻被占，均不可用
      if (nodeMap[node]
          || node == target
          || ((val = node - neighbourSize) >= 0 && val != target && nodeMap[val])
          || ((val = node + neighbourSize) < nodeMap.length && val != target && nodeMap[val])
          || ((val = node - 2 * neighbourSize) >= 0 && val != target && nodeMap[val])
          || ((val = node + 2 * neighbourSize) < nodeMap.length && val != target && nodeMap[val])) {
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
   * @return 返回有冲突的坐标点数组
   */
  public static List<Integer> conflictNode(int[] path, int mcol) {
    List<Integer> conflict = new ArrayList<>();
    for (int i = 1; i < path.length; i++) {
      if (nodeMap[path[i]]) {
        conflict.add(path[i]);
      }
    }
    int start = path[0];
    Consumer<Integer> consumer = (node) -> {
      // 非起点/且该点被占
      if (node >= 0 && node < nodeMap.length
          && node != start && nodeMap[node]) {
        conflict.add(node);
      }
    };
    int end = path[path.length - 1];
    consumer.accept(end - mcol);
    consumer.accept(end - 2 * mcol);
    consumer.accept(end + mcol);
    consumer.accept(end + 2 * mcol);

    return conflict;
  }

  /**
   * 转换地图点->工位台编号.
   *
   * @param mapNode        地图点
   * @param bufferLocation 工位台对应的地图点信息数组
   * @return 工位台编号， -1表示未找到
   */
  public static int getBufferIndex(int mapNode, int[] bufferLocation) {
    for (int i = 1; i < bufferLocation.length; i++) {
      if (mapNode == bufferLocation[i]) {
        return i;
      }
    }
    return -1;
  }

  /**
   * 获取工位台对应位置.
   *
   * @param bufferNo 工位台编号，规定：仓库入库出库口为特殊工位台，编号为 地图点索引 的相反值， 如-2, -1等
   * @return 地图点位置
   */
  public static int getBufferLoc(int bufferNo, AgvRoutePlan plan) {
    if (bufferNo < 0) {
      return -bufferNo;
    }
    return plan.getBufferMap(bufferNo);
  }
}
