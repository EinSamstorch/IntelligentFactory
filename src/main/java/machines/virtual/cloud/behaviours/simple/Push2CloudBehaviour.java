package machines.virtual.cloud.behaviours.simple;

import com.alibaba.fastjson.JSONObject;
import commons.tools.HttpRequest;
import commons.tools.LoggerUtil;
import jade.core.behaviours.SimpleBehaviour;


/**
 * SimpleBehaviour类,用于将订单状态推送至云端与保存到本地mysql中. 失败重试最大次数:3，失败后将写入日志.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class Push2CloudBehaviour extends SimpleBehaviour {

  /**
   * 更新订单状态信息. API网址：http://{website}/ks/FactoryAction_updateJobState.action? .
   * 需求参数：jobId=003002003001&jobState=001001001001in.
   */
  public static final int UPDATE_STATE = 1;
  /**
   * 更新工件位置信息. API网址：http://{website}/ks/FactoryAction_updateJobMachine.action? .
   * 需求参数：jobId=003004003010&machine=C001&process=2.
   */
  public static final int UPDATE_POSITION = 2;
  /**
   * 最大重试次数.
   */
  private static final int RETRY_MAX = 3;
  /**
   * 对应api网址. http://{website}/ks/FactoryAction_updateJobState.action?
   * http://{website}/ks/FactoryAction_updateJobMachine.action?
   */
  private String url;
  /**
   * 网址api需要的参数. update state : jobId=003002003001&jobState=001001001001in update position :
   * jobId=003004003010&machine=C001&process=2
   */
  private String param;
  /**
   * 云端更新flag.
   */
  private boolean cloudFlag = false;
  /**
   * 本地数据库更新flag.
   */
  private boolean sqlFlag = false;
  /**
   * 失败重试计数器.
   */
  private int retryCnt = 0;

  /**
   * 构造器.
   *
   * @param param  API对应参数
   * @param choice 选择API
   */
  public Push2CloudBehaviour(String website, String param, int choice) {
    this.param = param;
    switch (choice) {
      case UPDATE_STATE:
        url = String.format("http://%s/ks/FactoryAction_updateJobState.action?", website);
        break;
      case UPDATE_POSITION:
        url = String.format("http://%s/ks/FactoryAction_updateJobMachine.action?", website);
        break;
      default:
    }
  }

  /**
   * 尝试向云端和本地mysql数据库更新信息.
   */
  @Override
  public void action() {
    if (!cloudFlag) {
      updateCloud();
    }
    if (!sqlFlag) {
      updateSql();
    }
  }

  private void updateCloud() {
    String result = HttpRequest.sendGet(url, param);
    if (result != null) {
      JSONObject jsonObject = JSONObject.parseObject(result);
      String flag = jsonObject.getString("flag");
      if ("ok".equals(flag)) {
        cloudFlag = true;
      }
    }
  }

  private void updateSql() {
    /* 未来再说 */
    sqlFlag = true;
  }

  /**
   * 是否完成推送更新，超过三次失败重试取消推送，并写入日志.
   *
   * @return 云端推送与本地MYSQL推送 都成功时，True；失败重试超过三次，True；否则，False.
   */
  @Override
  public boolean done() {
    if (!(cloudFlag && sqlFlag)) {
      retryCnt += 1;
    }
    if (retryCnt > RETRY_MAX) {
      if (!cloudFlag) {
        LoggerUtil.agent.error("Push update to cloud failed! Param: " + param);
      }
      if (!sqlFlag) {
        LoggerUtil.agent.error("Push update to sql failed! Param: " + param);
      }
      return true;
    }
    return cloudFlag && sqlFlag;
  }
}
