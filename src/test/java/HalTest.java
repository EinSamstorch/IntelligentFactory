import machines.real.agv.AgvHal;
import commons.order.WorkpieceInfo;
import machines.real.lathe.LatheHal;
import org.junit.Test;
import machines.real.warehouse.WarehouseHal;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
/**
 * 测试HAL.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class HalTest {
    @Test
    public void testWarehouseHal(){
        WarehouseHal hal = new WarehouseHal();
        assertTrue(hal.moveItem(1,10));
    }

    @Test
    public void testLatheHal(){
        LatheHal hal = new LatheHal();
        assertTrue(hal.grabItem());
        assertTrue(hal.releaseItem());

        WorkpieceInfo wpInfo = new WorkpieceInfo("001","001","002","{\"L1\":10}");
        assertEquals(10, hal.evaluate(wpInfo));
        wpInfo.setProcessStep(1);
        assertTrue(hal.process(wpInfo));
    }

    @Test
    public void testAgvHal(){
        AgvHal hal = new AgvHal();
        assertTrue(hal.move(1, 10));
    }
}
