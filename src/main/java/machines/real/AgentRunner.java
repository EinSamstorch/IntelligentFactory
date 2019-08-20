package machines.real;
import jade.core.Agent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 使用java代码启动agent.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgentRunner {
    public static void main(String[] args) {
        startAgent();
    }
    private static void startAgent() {
        Runtime rt = Runtime.instance();
        Profile p = new ProfileImpl();
        ContainerController cc =rt.createAgentContainer(p);

        Object reference = new Object();
        Object[] args = new Object[1];
        args[0] = reference;
        try {
            ApplicationContext context = new FileSystemXmlApplicationContext("./resources/warehouse.xml");
            Agent agent = context.getBean("agent", Agent.class);
            AgentController ac = cc.acceptNewAgent("ck",agent);
//            AgentController ac = cc.createNewAgent("ck", "machines.real.warehouse.WarehouseAgent", args);
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
