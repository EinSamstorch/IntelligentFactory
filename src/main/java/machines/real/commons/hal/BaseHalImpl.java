package machines.real.commons.hal;

import com.alibaba.fastjson.JSONObject;
import commons.tools.JsonTool;
import commons.tools.LoggerUtil;
import java.util.Map;
import java.util.Objects;
import machines.real.commons.hal.socket.SocketListener;
import machines.real.commons.hal.socket.SocketMessage;
import machines.real.commons.hal.socket.SocketSender;

/**
 * 封装HAL底层通信.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BaseHalImpl implements BaseHal {

  public static final String HAL_ONLINE = "online";
  private static int taskNo = 0;
  private SocketSender sender;
  private SocketListener listener;
  private String extraInfo;

  /**
   * 最底层的hal 封装共同的特征,使用默认端口5656生成sender与listener.
   */
  public BaseHalImpl() {
    sender = new SocketSender();
    listener = new SocketListener();

    listener.start();
  }

  /**
   * 最底层的hal 封装共同的特征, 使用指定端口生成sender和listener.
   *
   * @param port 指定的server端口
   */
  public BaseHalImpl(int port) {
    sender = new SocketSender(port);
    listener = new SocketListener(port);

    listener.start();
  }

  /**
   * 向SocketServer发送命令.
   *
   * @param cmd   命令
   * @param extra 额外内容
   * @return 任务号
   */
  protected int sendMessageToMachine(String cmd, Object extra) {
    JSONObject message = new JSONObject();
    message.put(SocketMessage.FIELD_TASK_NO, ++taskNo);
    message.put(SocketMessage.FIELD_CMD, cmd);
    message.put(SocketMessage.FIELD_EXTRA, extra);

    sender.sendMessageToMachine(message);
    return taskNo;
  }

  protected JSONObject getResponse(int taskNo) {
    return getResponse(taskNo, 0);
  }

  /**
   * 获得response.
   *
   * @param taskNo       任务号
   * @param timeoutMills 超时时间 毫秒
   * @return response, 超时返回null
   */
  protected JSONObject getResponse(int taskNo, long timeoutMills) {
    Map<Integer, JSONObject> m = listener.getResponseMap();
    long currentTimestamp = 0;
    if (timeoutMills > 0) {
      currentTimestamp = System.currentTimeMillis();
    }
    while (true) {
      JSONObject response = m.get(taskNo);
      if (response != null) {
        return response;
      }
      if (currentTimestamp > 0
          && System.currentTimeMillis() > currentTimestamp + timeoutMills) {
        return null;
      }
      sleep(500);
    }
  }

  private void sleep(long mills) {
    try {
      Thread.sleep(mills);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  protected boolean executeCmd(String cmd) {
    return executeCmd(cmd, "", 0);
  }

  protected boolean executeCmd(String cmd, Object extra) {
    return executeCmd(cmd, extra, 0);
  }

  protected boolean executeCmd(String cmd, Object extra, long timeoutMills) {
    if (!checkHalOnline()) {
      return false;
    }
    int taskNo = sendMessageToMachine(cmd, extra);

    JSONObject response = getResponse(taskNo, timeoutMills);
    if (response == null) {
      LoggerUtil.hal.warn("Hal timeout. Task no: " + taskNo);
      return false;
    }
    if (!Objects.equals(SocketMessage.RESULT_SUCCESS,
        response.getString(SocketMessage.FIELD_RESULT))) {
      return false;
    }
    extraInfo = response.getString(SocketMessage.FIELD_EXTRA);

    return true;
  }

  /**
   * 检查对应的hal是否在线.
   *
   * @return 在线true, 离线false.
   */
  @Override
  public boolean checkHalOnline() {
    JSONObject msg = new JSONObject();
    msg.put(SocketMessage.FIELD_ACTION, SocketMessage.ACTION_CHECK);
    msg.put(SocketMessage.FIELD_VALUE, SocketMessage.TYPE_MACHINE);
    String response = sender.sendCmdToServer(msg);
    JSONObject json = JsonTool.parse(response);
    // 包含 result:success, info: online 返回true
    return json.containsKey(SocketMessage.FIELD_RESULT)
        && json.containsKey(SocketMessage.FIELD_INFO)
        && HAL_ONLINE.equals(json.getString(SocketMessage.FIELD_INFO))
        && SocketMessage.RESULT_SUCCESS.equals(json.getString(SocketMessage.FIELD_RESULT));
  }

  @Override
  public String getExtraInfo() {
    return extraInfo == null ? "" : extraInfo;
  }
}
