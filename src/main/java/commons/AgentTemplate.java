package commons;

import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Agent模板，添加一些必须实现的函数.
 * <p>
 * 继承步骤：
 * 1.继承该类
 * 2.实现 protected abstract void loadINI();
 * 3.重载 setup函数，并调用super.setup();
 * 4.调用registerDF();
 * 5.增加自定义行为
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public abstract class AgentTemplate extends Agent {
    @Override
    protected void setup() {
        super.setup();
        loadINI();
    }

    /**
     * 从setting.ini配置文件中载入对应配置参数
     */
    protected abstract void loadINI();

    /**
     * 向DF注册服务
     *
     * @param serviceType 自身提供的服务类型，{@link commons.tools.DFServiceType}
     */
    protected void registerDF(String serviceType) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType(serviceType);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
            LoggerUtil.agent.fatal(e.getMessage());
        }
    }

    // Put agent clean-up operations here
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

}
