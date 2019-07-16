package commons.tools;

import commons.exceptions.MsgCreateFailedException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

/**
 * 黄页服务 功能集合.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class DFUtils {
    /**
     * 搜索 黄页服务
     * 按照 服务类型 搜索 提供方
     * 将提供方地址写入 消息 收件人
     *
     * @param msg  招标消息
     * @param type 服务类型 {@link DFServiceType}
     * @return 消息-已装填所有提供方地址
     */
    public static ACLMessage searchDF(Agent agent, ACLMessage msg, String type, String password) throws Exception {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription templateSd = new ServiceDescription();
        templateSd.setType(type);
        if(password != null) {
            templateSd.addProperties(new Property("password", password));
        }
        template.addServices(templateSd);

        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(100L);
        try {
            DFAgentDescription[] results = DFService.search(agent, template, sc);
            if (results.length > 0) {
                for (int i = 0; i < results.length; i++) {
                    DFAgentDescription dfd = results[i];
                    AID provider = dfd.getName();

                    Iterator it = dfd.getAllServices();
                    while (it.hasNext()) {
                        ServiceDescription sd = (ServiceDescription) it.next();
                        if (sd.getType().equals(type)) {
                            msg.addReceiver(provider);
                        }
                    }
                }
            } else {
                throw new Exception("Service: " + type + ". Not found!");
            }
        } catch (FIPAException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
        return msg;
    }

    public static ACLMessage searchDF(Agent agent, ACLMessage msg, String type) throws Exception{
        return searchDF(agent, msg, type, null);
    }

    /**
     * 创建 招标消息
     * 消息类别 CFP
     * 消息条约 合同网
     * 超时时间 10秒
     *
     * @param  content 消息载体 object类型 需实现Serializable接口
     * @return 招标消息
     */
    public static ACLMessage createCFPMsg(Object content) throws MsgCreateFailedException {
        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        msg = msgSetContent(msg, content);
        return msg;
    }

    /**
     * 创建 请求消息
     * 消息类别 REQUEST
     * 消息条约 REQUEST
     *
     * @param  content 消息载体 object类型 需实现Serializable接口
     * @return 招标消息
     */
    public static ACLMessage createRequestMsg(Object content) throws MsgCreateFailedException {
        ACLMessage msg = createRequstMsgHeader();
        msg = msgSetContent(msg, content);
        return msg;
    }

    public static ACLMessage createRequstMsgHeader(){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
        return msg;
    }


    private static ACLMessage msgSetContent(ACLMessage msg, Object content) throws MsgCreateFailedException{
        try {
            msg.setContentObject((Serializable) content);
        } catch (IOException e) {
            LoggerUtil.commonTools.fatal(e.getMessage());
            throw new MsgCreateFailedException(e.getMessage());
        }
        return msg;
    }
}
