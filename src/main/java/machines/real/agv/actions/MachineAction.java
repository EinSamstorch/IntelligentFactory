package machines.real.agv.actions;

import java.io.Serializable;

/**
 * 设备动作命令包装.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public interface MachineAction extends Serializable {

  /**
   * 获取命令.
   *
   * @return 命令字符串
   */
  String getCmd();

  /**
   * 获取命令额外参数.
   *
   * @return 空字符串代表无额外参数， string代表单个参数， json代表参数组
   */
  Object getExtra();

  /**
   * 储存结果返回的extra信息.
   *
   * @param extra 额外信息
   */
  void setResultExtra(Object extra);

  /**
   * 获取结果返回的extra信息.
   *
   * @return extra信息
   */
  Object getResultExtra();
}
