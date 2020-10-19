package machines.real.agv.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 激光雷达Agv路径规划程序.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class LaserAgvRoutePlan implements AgvRoutePlan {

  /** 定义地图列数. */
  private static int mCol = 4;
  /** 定义地图行数. */
  private static int nRow = 15;
  /**
   * 定义在边界上的非边界点.
   *
   * <p>如：4 x 15地图中， 1 4 58 59这4个点.
   */
  private static int[] nonEdgePoints;

  private static int[] bufferNodes;
  private static int[] bufferLocation;

  static {
    nonEdgePoints = new int[mCol];
    nonEdgePoints[0] = 1;
    nonEdgePoints[1] = mCol;
    for (int i = 2; i < mCol; i++) {
      nonEdgePoints[i] = mCol * (nRow - 1) + i;
    }

    bufferNodes =
        new int[] {
          8, 9, 12, 13, 16, 17, 20, 21, 24, 25, 28, 29, 32, 33, 36, 37, 40, 41, 44, 45, 48, 49, 52
        };

    try {
      initBufferLocation();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void initBufferLocation() throws IOException {
    File txt = new File("./resources/constant/buffer_map.txt");

    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
    String line = readLine(reader);
    if ("".equalsIgnoreCase(line)) {
      throw new IOException("Reading Buffer Map Error!");
    }
    bufferLocation = new int[1 + Integer.parseInt(line.trim())];
    for (int i = 1; i < bufferLocation.length; i++) {
      line = readLine(reader);
      String[] s = line.split(" ");
      bufferLocation[Integer.parseInt(s[0])] = Integer.parseInt(s[1]);
    }
  }

  private static String readLine(BufferedReader reader) throws IOException {
    String line;
    while (null != (line = reader.readLine()) && line.contains("#")) {}
    return line == null ? "" : line;
  }

  private String plan0(int start, int end) {
    if (start <= 0 || end <= 0) {
      // 非法坐标点
      return "";
    }
    if (start == end) {
      // 无需规划
      return "";
    }
    if (notEdgePoint(start) || notEdgePoint(end)) {
      // 起点或终点不合法
      return start + "," + end;
    }
    int startRow = getRow(start);
    int startCol = getCol(start);
    int endRow = getRow(end);
    int endCol = getCol(end);
    if (startRow == endRow) {
      // 起点与终点在一行, 如果点相邻距离小于(mCol - 1)则是仓库入库与出库口，否则是工位台之间
      return Math.abs(start - end) < mCol - 1
          ? planBetweenWarehouse(start, end)
          : planSameRow(start, end);
    }
    if (startCol == endCol) {
      // 起点与终点在一列
      return planSameCol(start, end);
    }
    if ((startCol == 1 || startCol == mCol) && (endCol == 1 || endCol == mCol)) {
      // 一个在第一列，一个在第mCol列， 两个同一列的情况在上面的情形已经匹配
      return planBetweenBuffers(start, end);
    }
    return planWarehouse2Buffer(start, end);
  }

  private String planBetweenWarehouse(int start, int end) {
    final boolean reverse = start > end;
    // assume a < b
    int a = Math.min(start, end);
    int b = Math.max(start, end);
    List<String> path = new ArrayList<>(2 + b - a + 1);
    path.add(String.valueOf(a));
    for (int i = 0; i < b - a + 1; i++) {
      path.add(String.valueOf(a + mCol + i));
    }
    path.add(String.valueOf(b));
    if (reverse) {
      Collections.reverse(path);
    }
    return String.join(",", path);
  }

  private String planWarehouse2Buffer(int start, int end) {
    int startRow = getRow(start);
    int endRow = getRow(end);
    int startCol = getCol(start);
    int endCol = getCol(end);
    boolean changed = start > end;
    if (changed) {
      // 交换起点和终点
      int t = start;
      start = end;
      end = t;
    }
    String[] path = new String[Math.abs(startRow - endRow) + Math.abs(startCol - endCol) + 1];
    // 交叉点之一是(1或mCol), 另一个点位于地图中间
    int crossPoint = Math.max(getPoint(startRow, endCol), getPoint(endRow, startCol));
    // 计算起点和终点
    int temp = start;
    path[0] = String.valueOf(start);
    path[path.length - 1] = String.valueOf(end);
    // 计算仓库到交叉点
    for (int i = 1; temp < crossPoint; i++) {
      temp += mCol;
      path[i] = String.valueOf(temp);
    }
    int offset = 1 + Math.abs(startRow - endRow);
    int sign = crossPoint < end ? 1 : -1;
    for (int i = offset; i < path.length - 1; i++) {
      temp += sign;
      path[i] = String.valueOf(temp);
    }
    List<String> pathList = Arrays.stream(path).collect(Collectors.toList());
    if (changed) {
      // 起点在工位台，需要反转路径点
      Collections.reverse(pathList);
    }
    return String.join(",", pathList);
  }

  /**
   * 工位台之间路径规划，非同一列情况.
   *
   * @param start 起点编号
   * @param end 终点编号
   * @return 逗号分隔的路径字符串
   */
  private String planBetweenBuffers(int start, int end) {
    int startRow = getRow(start);
    int endRow = getRow(end);
    int startCol = getCol(start);
    int endCol = getCol(end);
    String[] path = new String[mCol + Math.abs(startRow - endRow)];
    int colDiff = startCol < endCol ? 1 : -1;
    int rowDiff = startRow < endRow ? mCol : -mCol;
    int temp = start + colDiff;
    path[0] = String.valueOf(start);
    path[1] = String.valueOf(temp);
    path[path.length - 1] = String.valueOf(end);
    int offset = 2 + Math.abs(startRow - endRow);
    for (int i = 2; i < offset; i++) {
      temp += rowDiff;
      path[i] = String.valueOf(temp);
    }
    for (int i = offset; i < path.length - 1; i++) {
      temp += colDiff;
      path[i] = String.valueOf(temp);
    }
    return String.join(",", path);
  }

  private String planSameRow(int start, int end) {
    String[] path = new String[mCol];
    int sign = start < end ? 1 : -1;
    for (int i = 0; i < mCol; i++) {
      path[i] = String.valueOf(start + sign * i);
    }
    return String.join(",", path);
  }

  private String planSameCol(int start, int end) {
    int startRow = getRow(start);
    int endRow = getRow(end);
    int startCol = getCol(start);
    String[] path = new String[2 + Math.abs(startRow - endRow) + 1];
    // 写入起点和终点
    path[0] = String.valueOf(start);
    path[path.length - 1] = String.valueOf(end);
    int colDiff = startCol == 1 ? 1 : -1;
    int rowDiff = startRow < endRow ? mCol : -mCol;
    int tempPoint = start + colDiff;
    for (int i = 0; i <= Math.abs(startRow - endRow); i++) {
      path[i + 1] = String.valueOf(tempPoint);
      tempPoint += rowDiff;
    }
    return String.join(",", path);
  }

  private int getRow(int num) {
    // range 1, 2, 3, ..., nRow
    return (num - 1) / mCol + 1;
  }

  private int getCol(int num) {
    // range 1, 2, ..., mCol
    return num % mCol == 0 ? mCol : num % mCol;
  }

  /**
   * 计算给定行列位置的点的标号.
   *
   * @param row 行号，从1 - nRow
   * @param col 列号，从1 - mCol
   * @return 点标号
   */
  private int getPoint(int row, int col) {
    return (row - 1) * mCol + col;
  }

  private boolean notEdgePoint(int num) {
    for (int nonEdgePoint : nonEdgePoints) {
      if (num == nonEdgePoint) {
        return true;
      }
    }
    int r = getRow(num);
    int c = getCol(num);
    if (r == 1 || r == nRow) {
      return false;
    }
    if (c == 1 || c == mCol) {
      return false;
    }
    return true;
  }

  // TODO 相领的工位台规划未完成
  @Override
  public String getRoute(int from, int to) {
    return plan0(from, to);
  }

  @Override
  public int getDistance(int from, int to) {
    return getRoute(from, to).split(",").length - 1;
  }

  @Override
  public int[] getEdgeNodes() {
    return bufferNodes;
  }

  @Override
  public int[] getBufferLocation() {
    return bufferLocation;
  }

  @Override
  public int getBufferMap(int bufferNo) {
    return bufferLocation[bufferNo];
  }
}
