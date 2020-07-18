package machines.real.agv.behaviours.sequencial;

import commons.order.WorkpieceStatus;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import machines.real.agv.actions.InExportAction;
import machines.real.agv.algorithm.AgvMapUtils2;
import machines.real.agv.algorithm.AgvMapUtils2.AgvState;
import machines.real.agv.algorithm.AgvRoutePlan;
import machines.real.agv.behaviours.simple.ActionCaller;
import machines.real.agv.behaviours.simple.AgvMoveBehaviour;
import machines.real.agv.behaviours.simple.InteractBuffer;
import machines.real.commons.request.AgvRequest;
import machines.real.commons.request.BufferRequest;
import machines.real.commons.request.WarehouseConveyorRequest;
import machines.real.commons.request.WarehouseItemMoveRequest;

/**
 * AGV执行搬运任务.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0
 * @since 1.8
 */
public class AgvExecuteTask extends SequentialBehaviour {

  /**
   * 执行AGV运输任务.
   *
   * @param request 运输请求
   * @param plan    路径规划算法
   * @param agv     执行者
   * @param message 请求消息
   */
  public AgvExecuteTask(AgvRequest request, AgvRoutePlan plan, AID agv, ACLMessage message) {
    this(plan.getBufferLoc(request.getFromBuffer()),
        plan.getBufferLoc(request.getToBuffer()),
        plan, agv, request.getWpInfo(), message);
  }

  /**
   * 执行AGV运输请求.
   *
   * @param from    起点
   * @param to      终点
   * @param plan    路径规划算法
   * @param agv     执行者
   * @param wpInfo  工件具体信息
   * @param message 请求消息来源
   */
  public AgvExecuteTask(int from, int to, AgvRoutePlan plan, AID agv, WorkpieceStatus wpInfo,
      ACLMessage message) {
    // 更新AGV状态
    AgvMapUtils2.updateAgvState(agv, AgvState.BUSY);
    // 1. 前往取货点
    addSubBehaviour(new AgvMoveBehaviour(agv, plan, from));
    // 2. 与仓库/工位台交互
    boolean fromWarehouse = !plan.isBuffer(from);
    addSubBehaviour(fromWarehouse
        ? interactWarehouse(agv, true, wpInfo)
        : interactBuffer(agv, plan.getBufferNo(from), true, wpInfo));
    // 3. 前往送货点
    addSubBehaviour(new AgvMoveBehaviour(agv, plan, to));
    // 4. 与仓库/工位台交互
    boolean toWarehouse = !plan.isBuffer(to);
    addSubBehaviour(toWarehouse
        ? interactWarehouse(agv, false, wpInfo)
        : interactBuffer(agv, plan.getBufferNo(to), false, wpInfo));
    // 5. 提示上个设备，工件已取走
    if (!fromWarehouse) {
      addSubBehaviour(new OneShotBehaviour() {
        @Override
        public void action() {
          AID me = myAgent.getAID();
          AID preOwner = new AID(wpInfo.getPreOwnerId(), false);
          Iterator allAddresses = me.getAllAddresses();
          while (allAddresses.hasNext()) {
            preOwner.addAddresses((String) allAddresses.next());
          }
          ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
          inform.addReceiver(preOwner);
          inform.setLanguage("BUFFER_INDEX");
          inform.setContent(Integer.toString(plan.getBufferNo(from)));
          myAgent.send(inform);
        }
      });
    }
    // 6. 回复任务发起者，AGV运输任务已经完成, 且复位AGV状态
    addSubBehaviour(new OneShotBehaviour() {
      @Override
      public void action() {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.INFORM);
        myAgent.send(reply);
        AgvMapUtils2.updateAgvState(agv, AgvState.FREE);
      }
    });
  }

  /**
   * 交互工位台.
   *
   * <p>工位台出(入)料与AGV入(出)料动作并行执行
   *
   * @param agv       AGV
   * @param bufferNo  工位台编号
   * @param agvImport AGV入料
   * @return 组装好的行为
   */
  private Behaviour interactBuffer(AID agv, int bufferNo, boolean agvImport,
      WorkpieceStatus wpInfo) {
    Behaviour bufferBehaviour = new InteractBuffer(new BufferRequest(bufferNo, !agvImport, wpInfo));
    Behaviour agvBehaviour = new ActionCaller(agv, new InExportAction(agvImport));
    ParallelBehaviour pb = new ParallelBehaviour();
    pb.addSubBehaviour(bufferBehaviour);
    pb.addSubBehaviour(agvBehaviour);
    return pb;
  }

  /**
   * 交互仓库.
   *
   * <p>AGV入料： 1.寻找原料库 2.仓库取货 3.传送带和AGV并行动作. AGV出料： 1.寻找成品库 2.传送带和AGV并行动作 3.仓库存货.
   *
   * @param agv       AGV
   * @param agvImport AGV入料
   * @param wpInfo    工件信息(内涵仓库联系方式)
   * @return 组装好的行为
   */
  private Behaviour interactWarehouse(AID agv, boolean agvImport, WorkpieceStatus wpInfo) {
    AID warehouse = agvImport ? wpInfo.getRawProviderId() : wpInfo.getProductProviderId();
    int warehousePos = wpInfo.getWarehousePosition();
    Behaviour whMove = new CallWarehouseMoveItem(
        new WarehouseItemMoveRequest(warehousePos, !agvImport, wpInfo), warehouse);
    Behaviour agvAndConveyor = new ImExportItemBehaviour(
        new ActionCaller(agv, new InExportAction(agvImport)),
        new CallWarehouseConveyor(new WarehouseConveyorRequest(!agvImport, wpInfo), warehouse));
    SequentialBehaviour sb = new SequentialBehaviour();
    if (agvImport) {
      sb.addSubBehaviour(whMove);
      sb.addSubBehaviour(agvAndConveyor);
    } else {
      sb.addSubBehaviour(agvAndConveyor);
      sb.addSubBehaviour(whMove);
    }
    return sb;
  }
}
