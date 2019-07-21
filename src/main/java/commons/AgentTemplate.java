package commons;

import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Agent模板，添加一些必须实现的函数.
 * <p>
 * 继承步骤：
 * 1.继承该类
 * 2.重载 setup函数，并调用super.setup();
 * 3.调用registerDF();
 * 4.增加自定义行为
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public abstract class AgentTemplate extends Agent {
    protected Integer halPort;

    @Override
    protected void setup() {
        super.setup();
    }

    /**
     * 注册服务
     *
     * @param serviceType 自身提供的服务类型，{@link commons.tools.DFServiceType}
     * @param password    额外属性 使用该服务时 需要配对密码
     */
    protected void registerDF(String serviceType, String password) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType(serviceType);
        if (password != null) {
            sd.addProperties(new Property("password", password));
        }
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            LoggerUtil.agent.fatal(e.getMessage());
        }
    }

    protected void registerDF(String serviceType) {
        registerDF(serviceType, null);
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

}
