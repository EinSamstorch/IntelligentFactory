package Cloud.Behaviours;

import Cloud.CloudAgent;
import CommonTools.DFServiceType;
import CommonTools.LoggerUtil;
import CommonTools.OrderInfo;
import CommonTools.Workpiece;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 拆分新订单内的工件列表，同时为工件向仓库进行招标.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class CFPNewOrderBehaviour extends TickerBehaviour {
    private Queue<Workpiece> retryQueue = new LinkedBlockingDeque<>();
    private CloudAgent cagent;
    private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private Queue<Workpiece> wpQueue = new LinkedBlockingDeque<>();

    public CFPNewOrderBehaviour(CloudAgent cagent, long period) {
        super(cagent, period);
        this.cagent = cagent;
    }

    /**
     * 1. 从 OrderInfo Queue中取出 待分配订单
     * 2. 遍历订单内的工件信息
     * 3. 为每个工件发起原料招标行为， 若无招标对象，则放入retryQueue
     * 4. 提取retryQueue，重新招标，失败则放入retryQueue
     */
    @Override
    protected void onTick() {
        while (true) {
            OrderInfo oi = cagent.getOrderQueue().poll();
            if (oi == null) break;
            for (Workpiece wp : oi.getWorkpieceList()) {
                processWorkpiece(wp);
            }
        }
        while (true) {
            Workpiece wp = retryQueue.poll();
            if (wp == null) break;
            processWorkpiece(wp);
        }
    }

    /**
     * 处理招标
     *
     * @param wp
     */
    protected void processWorkpiece(Workpiece wp) {
        String goodsid = wp.getGoodsId();
        ACLMessage msg = null;
        try {
            msg = searchDF(createMsg(goodsid), DFServiceType.WAREHOUSE);
            Behaviour b = new RawCNInitiator(cagent, msg, wp);
            cagent.addBehaviour(tbf.wrap(b));
        } catch (Exception e) {
            LoggerUtil.agent.error(e.getMessage());
            retryQueue.offer(wp);
        }
    }


    /**
     * 创建 招标消息
     * 消息类别 CFP
     * 消息条约 合同网
     * 超时时间 10秒
     * 消息语言 RAW
     * 消息内容 String goodsid
     *
     * @param goodsid 原料种类
     * @return 招标消息
     */
    protected ACLMessage createMsg(String goodsid) {
        ACLMessage msg = new ACLMessage(ACLMessage.CFP);
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        msg.setLanguage("RAW");
        msg.setContent(goodsid);

        return msg;
    }

    /**
     * 搜索 黄页服务
     * 按照 服务类型 搜索 提供方
     * 将提供方地址写入 招标消息 收件人
     *
     * @param msg  招标消息
     * @param type 服务类型 {@link DFServiceType}
     * @return 招标消息
     */
    protected ACLMessage searchDF(ACLMessage msg, String type) throws Exception {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription templateSd = new ServiceDescription();
        templateSd.setType(type);
        template.addServices(templateSd);

        SearchConstraints sc = new SearchConstraints();
        sc.setMaxResults(new Long(100));
        try {
            DFAgentDescription[] results = DFService.search(cagent, template, sc);
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
                throw new Exception("Service " + DFServiceType.WAREHOUSE + " not found!");
            }
        } catch (FIPAException e) {
            LoggerUtil.agent.error(e.getMessage());
        }
        return msg;
    }
}
