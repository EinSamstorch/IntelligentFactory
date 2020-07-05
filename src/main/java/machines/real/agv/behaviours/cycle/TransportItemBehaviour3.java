package machines.real.agv.behaviours.cycle;

import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import machines.real.agv.algorithm.AgvMapUtils2;
import machines.real.agv.algorithm.AgvMapUtils2.AgvState;
import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.behaviours.sequencial.AgvExecuteTask;
import machines.real.commons.request.AgvRequest;

/**
 * 多Agv系统运输货物.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class TransportItemBehaviour3 extends CyclicBehaviour {

  private Queue<ACLMessage> queue = new LinkedList<>();
  private final ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
  private AgvRoutePlan plan;

  public void setPlan(AgvRoutePlan plan) {
    this.plan = plan;
  }

  @Override
  public void action() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );
    ACLMessage receive = myAgent.receive(mt);
    // 放入队列，防止无可用AGV，导致该任务丢失
    if (receive != null) {
      queue.offer(receive);
    }
    if (queue.size() == 0) {
      block();
      return;
    }
    // 从队列中取出一个任务
    receive = queue.peek();
    AgvRequest request;
    try {
      request = (AgvRequest) receive.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      LoggerUtil.agent.warn("AgvRequest Deserialization NPE.");
      return;
    }
    // 寻找最近的AGV分配任务
    AID agv = findNearestAgv(request);

    // 如果找到了AGV，则分配任务
    if (agv != null) {
      queue.poll();
      Behaviour b = tbf.wrap(new AgvExecuteTask(request, plan, agv, receive));
      myAgent.addBehaviour(b);
      AgvMapUtils2.updateAgvState(agv, AgvState.BUSY);
    } else {
      block(1000);
    }
  }

  private AID findNearestAgv(AgvRequest request) {
    int fromLoc = plan.getBufferLoc(request.getFromBuffer());
    AID agv = null;
    int distance = Integer.MAX_VALUE;
    for (Entry<AID, AgvState> entry : AgvMapUtils2.getAgvStateMap().entrySet()) {
      if (entry.getValue().equals(AgvState.FREE)) {
        AID candidate = entry.getKey();
        int dist = plan.getDistance(AgvMapUtils2.getAgvLoc(candidate), fromLoc);
        if (dist < distance) {
          agv = candidate;
          distance = dist;
        }
      }
    }
    return agv;
  }
}
