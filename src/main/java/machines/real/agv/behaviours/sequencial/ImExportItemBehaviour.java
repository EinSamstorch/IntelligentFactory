package machines.real.agv.behaviours.sequencial;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import machines.real.agv.behaviours.simple.ActionCaller;

/**
 * agv进出货行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ImExportItemBehaviour extends ParallelBehaviour {

  /**
   * AGV出入物料动作组合.
   *
   * @param imExAction     agv接收输出物料动作
   * @param interactOther 对应工位台动作相应/对应仓库传送带相应动作
   */
  public ImExportItemBehaviour(ActionCaller imExAction, Behaviour interactOther) {
    addSubBehaviour(interactOther);
    addSubBehaviour(imExAction);
  }

}
