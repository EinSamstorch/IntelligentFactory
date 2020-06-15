package machines.real.agv.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.leap.Iterator;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import machines.real.agv.actions.InExportAction;
import machines.real.agv.actions.MoveAction;
import machines.real.agv.algorithm.AgvMapUtils;
import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.behaviours.sequencial.CallWarehouseConveyor;
import machines.real.agv.behaviours.sequencial.CallWarehouseMoveItem;
import machines.real.agv.behaviours.sequencial.ImExportItemBehaviour;
import machines.real.agv.behaviours.simple.ActionCaller;
import machines.real.agv.behaviours.simple.InteractBuffer;
import machines.real.commons.actions.MachineAction;
import machines.real.commons.request.AgvRequest;
import machines.real.commons.request.BufferRequest;
import machines.real.commons.request.WarehouseConveyorRequest;
import machines.real.commons.request.WarehouseItemMoveRequest;

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
  private Queue<ACLMessage> msgQueue = new LinkedList<>();

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
    final int fromBuffer = request.getFromBuffer();
    int fromLoc = AgvMapUtils.getBufferLoc(fromBuffer, plan);
    AID choose = null;
    int distance = Integer.MAX_VALUE;
    for (AID agv : checkInMap.keySet()) {
      // 1. 获取agv当前位置
      int loc = AgvMapUtils.getLocationMap().get(agv);
      int d = plan.getDistance(loc, fromLoc);
      if (distance > d) {
        choose = agv;
        distance = d;
      }
    }
    // 2. 获取最近位置的agv
    if (choose == null) {
      return;
    }
    LoggerUtil.agent.info("Choose agv: " + choose.getLocalName());
    // 3. agv前往取货点
    agvMove(fromBuffer, choose);
    // 4. AGV入料
    if (fromBuffer < 0) {
      interactWarehouse(true, choose, request);
    } else {
      interactBuffer(true, choose, fromBuffer);
    }
    // 5. agv前往送货点
    int toBuffer = request.getToBuffer();
    agvMove(toBuffer, choose);
    // 6. AGV送料
    if (toBuffer < 0) {
      interactWarehouse(false, choose, request);
    } else {
      interactBuffer(false, choose, toBuffer);
    }
    ACLMessage msg = msgQueue.remove();
    ACLMessage reply = msg.createReply();
    reply.setPerformative(ACLMessage.INFORM);
    myAgent.send(reply);

    // 通知上道工序设备 货物已经取走
    if (fromBuffer > 0) {
      AID me = myAgent.getAID();
      AID preOwner = new AID(request.getWpInfo().getPreOwnerId(), false);
      Iterator allAddresses = me.getAllAddresses();
      while (allAddresses.hasNext()) {
        preOwner.addAddresses((String) allAddresses.next());
      }
      ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
      inform.addReceiver(preOwner);
      inform.setLanguage("BUFFER_INDEX");
      inform.setContent(Integer.toString(fromBuffer));
      myAgent.send(inform);
    }
  }

  private void solveConflict(List<Integer> conflict, String movePath) {
    if (conflict.size() == 0) {
      return;
    }
    String[] split = movePath.split(",");
    int target = Integer.parseInt(split[split.length - 1]);
    for (int conf : conflict) {
      AID conflictAid = AgvMapUtils.getConflictAid(conf);
      if (conflictAid != null) {
        LoggerUtil.agent.info(
            "Route plan conflict! Pos: " + conf + ", agent: " + conflictAid.getLocalName());
        // 获取不冲突位置
        int newLoc = AgvMapUtils.getFreeEdgeNode(plan.getEdgeNodes(), 4, target);
        MoveAction moveConflict = new MoveAction(plan.getRoute(conf, newLoc));
        waitCallerDone(conflictAid, moveConflict);
        // 更新解决冲突后,agv的位置
        AgvMapUtils.setAgvLoc(conflictAid, newLoc);
      }
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
    msgQueue.offer(msg);
  }

  private void waitCallerDone(AID receiver, MachineAction action) {
    // 等待动作完成
    waitBehaviourDone(new ActionCaller(receiver, action));
  }

  private void waitBehaviourDone(Behaviour b) {
    // 额外线程运行
    b = tbf.wrap(b);
    myAgent.addBehaviour(b);
    while (!b.done()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void interactWarehouse(boolean agvImport, AID agv, AgvRequest request) {
    // agv从仓库取货
    // 1. 仓库移动货物到出口
    // 2. agv启动收货模式 & 仓库传送带启动出货模式

    // agv送货入仓库
    // 1. 仓库传送带启动入货模式 & agv启动出货模式
    // 2. 仓库移动货物到指定位置
    WorkpieceStatus wpInfo = request.getWpInfo();
    AID warehouse = agvImport ? wpInfo.getRawProviderId() : wpInfo.getProductProviderId();
    int warehousePos = wpInfo.getWarehousePosition();
    Behaviour whMove = new CallWarehouseMoveItem(
        new WarehouseItemMoveRequest(warehousePos, !agvImport), warehouse);
    Behaviour agvAndConveyor = new ImExportItemBehaviour(
        new ActionCaller(agv, new InExportAction(agvImport)),
        new CallWarehouseConveyor(new WarehouseConveyorRequest(!agvImport), warehouse));
    if (agvImport) {
      waitBehaviourDone(whMove);
      waitBehaviourDone(agvAndConveyor);
    } else {
      waitBehaviourDone(agvAndConveyor);
      waitBehaviourDone(whMove);
    }
  }

  private void interactBuffer(boolean agvImport, AID agv, int bufferNo) {
    // 交互buffer， 进出货模式与agv进出货模式相反
    InteractBuffer interactBuffer = new InteractBuffer(new BufferRequest(bufferNo, !agvImport));
    // 交互agv货仓
    ActionCaller imExCaller = new ActionCaller(agv, new InExportAction(agvImport));
    // 组装行为
    Behaviour outBehaviour = new ImExportItemBehaviour(imExCaller, interactBuffer);
    // 等待完成
    waitBehaviourDone(outBehaviour);
  }

  private void agvMove(int bufferNo, AID agv) {

    int toLoc = AgvMapUtils.getBufferLoc(bufferNo, plan);
    int fromLoc = AgvMapUtils.getLocationMap().get(agv);

    String movePath = plan.getRoute(fromLoc, toLoc);
    if ("".equals(movePath)) {
      // 无需移动
      return;
    }
    LoggerUtil.agent.info("Move path: " + movePath);
    // 计算冲突
    List<Integer> conflict = AgvMapUtils.conflictNode(
        Arrays.stream(movePath.split(",")).mapToInt(Integer::parseInt).toArray(), 4);
    // 解决冲突
    solveConflict(conflict, movePath);
    // 移动agv
    MachineAction moveAction = new MoveAction(plan.getRoute(fromLoc, toLoc));
    waitCallerDone(agv, moveAction);
    // 更新agv位置
    AgvMapUtils.setAgvLoc(agv, toLoc);
  }
}
