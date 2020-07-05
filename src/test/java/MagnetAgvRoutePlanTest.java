import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.algorithm.MagnetAgvRoutePlan;
import org.junit.Assert;
import org.junit.Test;

/**
 * 磁导航AGV路径规划测试.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class MagnetAgvRoutePlanTest {

  @Test
  public void testPath() {
    AgvRoutePlan plan = new MagnetAgvRoutePlan();
    Assert.assertEquals(plan.getRouteString(24, 2), "24,25,28,1,2");
    Assert.assertEquals(plan.getRouteString(24, 21), "24,25,28,1,2,3,4,5,30,19,20,21");
    Assert.assertTrue(plan.getDistance(23, 21) > plan.getDistance(24, 21));
  }
}
