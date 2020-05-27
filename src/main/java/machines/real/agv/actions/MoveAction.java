package machines.real.agv.actions;

import com.alibaba.fastjson.JSONObject;
import machines.real.agv.MultiAgvHal;
import machines.real.commons.actions.AbstractMachineAction;

/**
 * Agv移动命令.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class MoveAction extends AbstractMachineAction {

  private JSONObject extra;

  /**
   * 构造移动动作.
   *
   * @param path 路径
   */
  public MoveAction(String path) {
    extra = new JSONObject();
    extra.put("path", path);
  }

  @Override
  public String getCmd() {
    return MultiAgvHal.CMD_MOVE;
  }

  @Override
  public Object getExtra() {
    return extra;
  }
}
