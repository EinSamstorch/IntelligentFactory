package machines;

import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.Properties;
import java.util.Random;
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

  /**
   * Agent启动器, jar包启动入口.
   * @param args 无用参数
   */
  public static void main(String[] args) {
    try {
      startAgent();
    } catch (StaleProxyException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void startAgent() throws StaleProxyException {
    ApplicationContext settingContext = new FileSystemXmlApplicationContext(
        "resources/settings.xml");
    Properties jadeProps = settingContext.getBean("settings", Properties.class);
    String ip = jadeProps.getProperty("jade.platform.ip", null);
    int port = Integer.parseInt(jadeProps.getProperty("setting.port", "-1"));
    String agentName = jadeProps
        .getProperty("jade.agent.name", String.format("Agent%d", new Random().nextInt()));
    String xmlPath = jadeProps.getProperty("agent.xml");

    Runtime rt = Runtime.instance();
    Profile p = new ProfileImpl(ip, port, null, false);
    ContainerController cc = rt.createAgentContainer(p);

    ApplicationContext context = new FileSystemXmlApplicationContext(xmlPath);
    Agent agent = context.getBean("agent", Agent.class);
    AgentController ac = cc.acceptNewAgent(agentName, agent);
    ac.start();
  }
}
