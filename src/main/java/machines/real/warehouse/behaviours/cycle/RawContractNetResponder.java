package machines.real.warehouse.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import java.io.IOException;
import machines.real.commons.ContractNetContent;
import machines.real.warehouse.DbInterface;
import machines.real.warehouse.WarehouseAgent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 原材料 合同网 招标回应方.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class RawContractNetResponder extends ContractNetResponder {

  private DbInterface db;

  /**
   * 原料应标行为.
   * @param warehouseAgent 仓库agent
   * @param mt 消息模板
   */
  public RawContractNetResponder(WarehouseAgent warehouseAgent, MessageTemplate mt) {
    super(warehouseAgent, mt);
    ApplicationContext ac = new FileSystemXmlApplicationContext("./resources/sql.xml");
    db = ac.getBean("db", DbInterface.class);
  }

  /**
   * 处理原料招标请求.
   *
   * @param cfp 招标信息， content: String goodsid
   * @return ACLMessage content: String quantity
   */
  @Override
  protected ACLMessage handleCfp(ACLMessage cfp)
      throws RefuseException, FailureException, NotUnderstoodException {
    LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
    // cfp 内容: WorkpieceInfo
    String goodsid = null;
    try {
      WorkpieceStatus wpInfo = (WorkpieceStatus) cfp.getContentObject();
      goodsid = wpInfo.getGoodsId();
    } catch (UnreadableException e) {
      e.printStackTrace();
    }
    // 查询仓库余量
    int quantity = db.getRawQuantityByGoodsId(goodsid);
    if (quantity > 0) {
      // propose 内容: String quantity
      ACLMessage propose = cfp.createReply();
      propose.setPerformative(ACLMessage.PROPOSE);

      try {
        propose.setContentObject(new ContractNetContent(quantity));
      } catch (IOException e) {
        e.printStackTrace();
      }

      return propose;
    } else {
      // 仓库余量不足
      LoggerUtil.agent.info(String.format("Goodsid: %s is out of stock.", goodsid));
      throw new RefuseException(String.format("Goodsid: %s is out of stock.", goodsid));
    }
  }

  @Override
  protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
      throws FailureException {
    LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
    int quantity = Integer.parseInt(accept.getContent());

    try {
      // 对 workpieceInfo 添加 warehousePosition
      WorkpieceStatus wpInfo = ((WorkpieceStatus) cfp.getContentObject());
      String goodsid = wpInfo.getGoodsId();
      Integer position = db.getRaw(goodsid);
      wpInfo.setWarehousePosition(position);
      // 更新 wpInfo
      wpInfo.setProviderId(myAgent.getLocalName());
      wpInfo.setCurOwnerId(myAgent.getLocalName());
      // map location for exporter of warehouse
      wpInfo.setBufferPos(26);

      // 发送至Worker 进行下一轮招标
      ACLMessage msg = DfUtils.createRequestMsg(wpInfo);
      DfUtils.searchDf(myAgent, msg, DfServiceType.WORKER);
      myAgent.send(msg);

      // 完成本次招标动作
      ACLMessage inform = accept.createReply();
      inform.setPerformative(ACLMessage.INFORM);
      return inform;
    } catch (UnreadableException e) {
      e.printStackTrace();
      throw new FailureException("Get goodsid failed");
    } catch (Exception e) {
      e.printStackTrace();
      throw new FailureException(e.getMessage());
    }
  }

  @Override
  protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
    super.handleRejectProposal(cfp, propose, reject);
  }
}
