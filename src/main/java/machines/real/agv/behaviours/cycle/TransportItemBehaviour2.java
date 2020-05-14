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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import machines.real.agv.actions.InExportAction;
import machines.real.agv.actions.MoveAction;
import machines.real.agv.algorithm.AgvMapUtils;
import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.behaviours.simple.ActionCaller;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.request.AgvRequest;

/**
 * 多Agv系统运输货物.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class TransportItemBehaviour2 extends CyclicBehaviour {

  private Queue<AgvRequest> queue = new LinkedList<>();
  private Map<AID, Long> checkInMap;
  private AgvRoutePlan plan;
  private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

  public void setPlan(AgvRoutePlan plan) {
    this.plan = plan;
  }

  public void setCheckInMap(Map<AID, Long> checkInMap) {
    this.checkInMap = checkInMap;
  }

  @Override
  public void action() {
    receiveRequest();

    if (queue.size() == 0) {
      block();
      return;
    }
    if (checkInMap.size() == 0) {
      LoggerUtil.agent.warn("No available agv instance!");
      block(3000);
      return;
    }
    // 应该获取最近的agv, 调用它去完成任务
    AgvRequest request = queue.poll();
    if (request == null) {
      return;
    }
    // 取货点
    int from = request.getFrom();
    AID choose = null;
    int distance = Integer.MAX_VALUE;
    for (AID agv : checkInMap.keySet()) {
      // 1. 获取agv当前位置
      int loc = AgvMapUtils.getLocationMap().get(agv);
      int d = plan.getDistance(loc, from);
      if (distance > d) {
        choose = agv;
        distance = d;
      }
    }
    // 2. 获取最近位置的agv
    if (choose == null) {
      return;
    }
    // 3. 计算路径取货与清除阻塞
    int pos = AgvMapUtils.getLocationMap().get(choose);
    String getGoodPath = plan.getRoute(pos, from);
    int conflict = AgvMapUtils
        .conflictNode(Arrays.stream(getGoodPath.split(",")).mapToInt(Integer::parseInt).toArray());
    solveConflict(conflict);
    // 4. 到达取货点
    MachineAction moveToStart = new MoveAction(AgvMapUtils.getLocationMap().get(choose), from,
        plan);
    waitCallerDone(choose, moveToStart);
    // 5. 入料
    MachineAction importAction = new InExportAction(true);
    waitCallerDone(choose, importAction);
    // 6. 计算路径送货与清除阻塞
    String sendGoodPath = plan.getRoute(from, request.getTo());
    conflict = AgvMapUtils
        .conflictNode(Arrays.stream(sendGoodPath.split(",")).mapToInt(Integer::parseInt).toArray());
    solveConflict(conflict);
    // 7. 到达送货点
    MachineAction moveToEnd = new MoveAction(AgvMapUtils.getLocationMap().get(choose),
        request.getTo(), plan);
    waitCallerDone(choose, moveToEnd);
    // 8. 送料
    MachineAction exportAction = new InExportAction(false);
    waitCallerDone(choose, exportAction);
  }

  private void solveConflict(int conflict) {
    if (conflict == -1) {
      return;
    }
    AID conflictAid = AgvMapUtils.getConflictAid(conflict);
    if (conflictAid != null) {
      LoggerUtil.agent.info(
          "Route plan conflict! Pos: " + conflict + ", agent: " + conflictAid.getLocalName());
      int newLoc = AgvMapUtils.getFreeEdgeNode(plan.getEdgeNodes(), 4);
      MoveAction moveConflict = new MoveAction(conflict, newLoc, plan);
      waitCallerDone(conflictAid, moveConflict);
    }
  }

  private void receiveRequest() {
    MessageTemplate mt = MessageTemplate.and(
        MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );
    ACLMessage msg = myAgent.receive(mt);
    if (msg == null) {
      return;
    }
    AgvRequest request;
    try {
      request = (AgvRequest) msg.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
      return;
    }
    queue.offer(request);
  }

  private void waitCallerDone(AID receiver, MachineAction action) {
    Behaviour b = tbf.wrap(new ActionCaller(receiver, action));
    // 等待动作完成
    while (!b.done()) {
      block(1000);
    }
  }
}
