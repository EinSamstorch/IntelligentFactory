package machines.real.warehouse.behaviours.cycle;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames.InteractionProtocol;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.ContractNetResponder;
import java.io.IOException;
import machines.real.commons.ContractNetContent;
import machines.real.commons.behaviours.sequantial.CallForAgv;
import machines.real.commons.request.AgvRequest;
import machines.real.warehouse.DbInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ProductContractNetResponder extends ContractNetResponder {

  private static MessageTemplate mt = MessageTemplate.and(
      MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP),
          MessageTemplate.MatchProtocol(InteractionProtocol.FIPA_CONTRACT_NET)),
      MessageTemplate.MatchLanguage("PRODUCT")
  );
  private int importBuffer = -2;
  private DbInterface db;

  /**
   * 成品库应标行为.
   *
   * @param a 提供成品存储的agent
   */
  public ProductContractNetResponder(Agent a) {
    super(a, mt);
    ApplicationContext ac = new FileSystemXmlApplicationContext("./resources/sql.xml");
    db = ac.getBean("db", DbInterface.class);
  }

  @Override
  protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException {
    LoggerUtil.agent.debug(String.format("CFP received from: %s.", cfp.getSender().getName()));
    int quantity = db.getProductQuantity();
    if (quantity > 0) {
      ACLMessage propose = cfp.createReply();
      propose.setPerformative(ACLMessage.PROPOSE);
      try {
        propose.setContentObject(new ContractNetContent(quantity));
      } catch (IOException e) {
        e.printStackTrace();
      }
      return propose;
    } else {
      LoggerUtil.agent.info("Product Full!");
      throw new RefuseException("Product Full!");
    }
  }

  @Override
  protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
      throws FailureException {
    LoggerUtil.agent.info("Proposal accepted: " + accept.getSender().getName());
    int quantity = Integer.parseInt(accept.getContent());
    WorkpieceStatus wpInfo = null;
    try {
      wpInfo = (WorkpieceStatus) cfp.getContentObject();
    } catch (UnreadableException e) {
      e.printStackTrace();
    }
    if (wpInfo != null) {
      int position = db.getProduct(wpInfo);
      wpInfo.setWarehousePosition(position);
      // 更新 wpInfo
      wpInfo.setCurOwnerId(myAgent.getLocalName());
      int oldBuffer = wpInfo.getBufferPos();
      // 入库口
      wpInfo.setBufferPos(importBuffer);
      // call for agv
      AgvRequest request = new AgvRequest(oldBuffer, importBuffer, wpInfo);
      myAgent.addBehaviour(new CallForAgv(request));

      ACLMessage inform = accept.createReply();
      inform.setPerformative(ACLMessage.INFORM);
      return inform;
    } else {
      throw new FailureException("WorkpieceInfo NPE Error.");
    }
  }
}
