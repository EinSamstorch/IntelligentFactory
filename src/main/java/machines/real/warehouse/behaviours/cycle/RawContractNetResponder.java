package machines.real.warehouse.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.DfServiceType;
import commons.tools.DfUtils;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import java.io.IOException;
import machines.real.commons.ContractNetContent;
import machines.real.warehouse.DbInterface;
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

  private static MessageTemplate mt =
      MessageTemplate.and(
          MessageTemplate.and(
              MessageTemplate.MatchPerformative(ACLMessage.CFP),
              MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_CONTRACT_NET)),
          MessageTemplate.MatchLanguage("RAW"));
  private int exportBuffer = -3;
  private DbInterface db;

  public void setDb(DbInterface db) {
    this.db = db;
  }

  /**
   * 原料应标行为.
   *
   * @param a 仓库agent
   */
  public RawContractNetResponder(Agent a) {
    super(a, mt);
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
      wpInfo.setBufferPos(exportBuffer);

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
