import org.junit.Test;
import warehouse.WarehouseHal;
import static junit.framework.TestCase.assertTrue;
/**
 * 测试HAL.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class WarehouseHalTest {
    @Test
    public void testHal(){
        WarehouseHal hal = new WarehouseHal();
        hal.start();
        hal.moveItem(1,10);
        assertTrue(hal.moveItem(1,10));
    }
}
