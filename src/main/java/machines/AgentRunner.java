package machines;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Properties;
import java.util.Random;

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
        ApplicationContext jadeContext = new FileSystemXmlApplicationContext("./resources/jade.xml");
        Properties jadeProps = jadeContext.getBean("jadeConfig", Properties.class);
        String ip = jadeProps.getProperty("setting.ip", null);
        int port = Integer.parseInt(jadeProps.getProperty("setting.port", "-1"));
        String agentName = jadeProps.getProperty("setting.name", String.format("Agent%d", new Random().nextInt()));
        String xmlPath = jadeProps.getProperty("setting.xml");

        Profile p = new ProfileImpl(ip, port, null, false);
        ContainerController cc = rt.createAgentContainer(p);

        try {
            ApplicationContext context = new FileSystemXmlApplicationContext(xmlPath);
            Agent agent = context.getBean("agent", Agent.class);
            AgentController ac = cc.acceptNewAgent(agentName, agent);
            ac.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
