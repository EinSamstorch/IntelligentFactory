package commons;

import commons.tools.DfServiceType;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import machines.real.commons.IMachineOnline;
import machines.real.commons.IOfferService;

import java.lang.reflect.Field;

/**
 * Agent模板，添加一些必须实现的函数.
 * <p>
 * 继承步骤：
 * 1.继承该类
 * 2.重载 setup函数，并调用super.setup();
 * 删除(3.调用registerDF();) 由agent状态管理行为监管 CheckHalState
 * 4.增加自定义行为
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class BaseAgent extends Agent implements IMachineOnline, IOfferService {
    protected String serviceType;
    protected Behaviour[] behaviours;
    private boolean agentOnline = false;

    public void setBehaviours(Behaviour[] behaviours) {
        this.behaviours = behaviours;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    protected void setup() {
        super.setup();
    }

    /**
     * 注册服务
     *
     * @param serviceType 自身提供的服务类型，{@link DfServiceType}
     * @param password    额外属性 使用该服务时 需要配对密码
     */
    @Override
    public void registerDf(String serviceType, String password) {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerDf(String serviceType) {
        registerDf(serviceType, null);
    }

    @Override
    public void deregisterDf() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Put agent clean-up operations here
     */
    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    @Override
    public boolean isAgentOnline() {
        return agentOnline;
    }

    @Override
    public void setAgentOnline(boolean agentOnline) {
        Class selfClaz = this.getClass();
        String password = null;
        // 通过反射获取password
        try {
            Field pwdFiled = selfClaz.getDeclaredField("armPwd");
            pwdFiled.setAccessible(true);
            password = (String) pwdFiled.get(this);
            pwdFiled.setAccessible(false);
        } catch (NoSuchFieldException e) {
            password = null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (this.agentOnline != agentOnline) {
            if (this.agentOnline) {
                // 下线agent
                LoggerUtil.agent.info("Agent Offline.");
                deregisterDf();
            } else {
                // 上线agent
                LoggerUtil.agent.info("Agent Online.");
                registerDf(serviceType, password);
            }
            this.agentOnline = agentOnline;
        }
    }
}
