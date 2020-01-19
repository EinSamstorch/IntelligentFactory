import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import rpc.services.AgvRoutePlan;

/**
 * 测试远程调用AGV路径规划.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class AgvRoutePlanTest {

  @Test
  public void testRpcCall() {
    ApplicationContext ac = new FileSystemXmlApplicationContext("resources/services.xml");
    AgvRoutePlan plan = (AgvRoutePlan) ac.getBean("agvRoutePlan");
    Assert.assertTrue("1,10".equals(plan.plan(1, 10)));
  }
}
