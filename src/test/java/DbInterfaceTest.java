import machines.real.warehouse.DbInterface;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class DbInterfaceTest {
    @Test
    public void testBean() {
        ApplicationContext ac = new FileSystemXmlApplicationContext("./resources/sql.xml");
        DbInterface db = ac.getBean("warehouse_db", DbInterface.class);
        System.out.println(db.getProductQuantity());
    }
}
