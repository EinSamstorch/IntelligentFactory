package machines.real.agv.algorithm;

import edu.princeton.cs.algs4.In;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import machines.real.agv.algorithm.algs4.DijkstraSP;
import machines.real.agv.algorithm.algs4.EdgeWeightedDigraph;

/**
 * 磁道航AGV路径规划.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class MagnetAgvRoutePlan implements AgvRoutePlan {

  private static final String filepath = "./resources/constant/agv_map.txt";
  private static final EdgeWeightedDigraph graph = new EdgeWeightedDigraph(new In(filepath));
  private static int[] bufferLocArray;

  static {
    File txt = new File("./resources/constant/buffer_map.txt");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(txt));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
    bufferLocArray = reader.lines().filter((line) -> !line.contains("#"))
        .mapToInt(Integer::parseInt).toArray();
  }

  @Override
  public String getRouteString(int from, int to) {
    DijkstraSP sp = new DijkstraSP(graph, from);
    return String.join(",", sp.getPathStringArray(to));
  }

  @Override
  public int[] getRouteArray(int from, int to) {
    DijkstraSP sp = new DijkstraSP(graph, from);
    return sp.getPathIntArray(to);
  }

  @Override
  public int getDistance(int from, int to) {
    DijkstraSP sp = new DijkstraSP(graph, from);
    return (int) sp.distTo(to);
  }

  @Override
  public boolean isBuffer(int loc) {
    for (int v : bufferLocArray) {
      if (v == loc) {
        return true;
      }
    }
    return false;
  }

  @Override
  public int getBufferNo(int loc) {
    for (int i = 0; i < bufferLocArray.length; i++) {
      if (bufferLocArray[i] == loc) {
        return i + 1;
      }
    }
    return -1;
  }

  @Override
  public int getBufferLoc(int bufferNo) {
    return bufferLocArray[bufferNo - 1];
  }

}
