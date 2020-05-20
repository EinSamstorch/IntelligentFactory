import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.algorithm.LaserAgvRoutePlan;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试路径规划算法.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class LaserAgvRoutePlanTest {

  /**
   * 测试仓库与工位台之间的路径生成
   */
  @Test
  public void testWarehouse2Buffer() {
    AgvRoutePlan plan = new LaserAgvRoutePlan();
    int[][] input = new int[][]{{3, 9}, {16, 2}};
    String[] expects = new String[]{"3,7,11,10,9", "16,15,14,10,6,2"};
    for (int i = 0; i < input.length; i++) {
      Assert.assertEquals(expects[i], plan.getRoute(input[i][0], input[i][1]));
    }
  }

  /**
   * 测试相领工位台
   */
  @Test
  public void testNeighbours() {
    AgvRoutePlan plan = new LaserAgvRoutePlan();
    int[][] input = new int[][]{{41, 45}};
    String[] expects = new String[]{"41,42,46,45"};
    for (int i = 0; i < input.length; i++) {
      Assert.assertEquals(expects[i], plan.getRoute(input[i][0], input[i][1]));
    }
  }

  /**
   * 测试同列工位台，非邻居
   */
  @Test
  public void testSameCol() {
    AgvRoutePlan plan = new LaserAgvRoutePlan();
    int[][] input = new int[][]{{9, 45}};
    String[] expects = new String[]{"9,10,14,18,22,26,30,34,38,42,46,45"};
    for (int i = 0; i < input.length; i++) {
      Assert.assertEquals(expects[i], plan.getRoute(input[i][0], input[i][1]));
    }
  }

  /**
   * 测试同行工位台
   */
  @Test
  public void testSameRow() {
    AgvRoutePlan plan = new LaserAgvRoutePlan();
    int[][] input = new int[][]{{12, 9}, {9, 12}};
    String[] expects = new String[]{"12,11,10,9", "9,10,11,12"};
    for (int i = 0; i < input.length; i++) {
      Assert.assertEquals(expects[i], plan.getRoute(input[i][0], input[i][1]));
    }
  }
}
