package machines.real.commons.behaviours.simple;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;
import jade.core.behaviours.SimpleBehaviour;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import machines.real.commons.buffer.Buffer;
import machines.real.commons.buffer.BufferState;

/**
 * 执行特定动作.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ExecuteActionBehaviour extends SimpleBehaviour {

  private boolean isDone = false;
  private Method actionMethod;
  private Buffer buffer;
  private Object actionObj;
  private String infoStr;

  /**
   * 执行特定动作的行为.
   *
   * @param actionObj    动作的执行者对象
   * @param actionMethod 需要执行的动作函数名
   * @param buffer       工位台对象
   * @param infoStr      提示消息
   */
  public ExecuteActionBehaviour(Object actionObj, Method actionMethod, Buffer buffer,
      String infoStr) {
    this.actionMethod = actionMethod;
    this.buffer = buffer;
    this.actionObj = actionObj;
    this.infoStr = infoStr;
  }

  /**
   * 执行特定动作的行为.
   *
   * @param actionObj    动作的执行者对象
   * @param actionMethod 需要执行的动作函数名
   * @param buffer       工位台对象
   */
  public ExecuteActionBehaviour(Object actionObj, Method actionMethod, Buffer buffer) {
    this(actionObj, actionMethod, buffer, "");
  }

  @Override
  public void action() {
    WorkpieceStatus wpInfo = null;
    if (buffer != null) {
      wpInfo = buffer.getWpInfo();
      buffer.getBufferState().setState(BufferState.STATE_PROCESSING);
      buffer.setProcessTimestamp(System.currentTimeMillis());
    }
    if (wpInfo != null) {
      try {
        isDone = (boolean) actionMethod.invoke(actionObj, wpInfo);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    } else {
      try {
        isDone = (boolean) actionMethod.invoke(actionObj);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    if (isDone) {
      LoggerUtil.hal.info(infoStr);
    } else {
      // 动作失败 5秒后重试
      LoggerUtil.hal.info("Failed! " + infoStr);
      block(5000);
    }
  }

  @Override
  public boolean done() {
    return isDone;
  }
}
