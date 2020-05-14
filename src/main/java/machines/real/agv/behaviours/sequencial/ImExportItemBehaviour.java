package machines.real.agv.behaviours.sequencial;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import machines.real.agv.behaviours.simple.ActionCaller;

/**
 * agv进出货行为.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public class ImExportItemBehaviour extends SequentialBehaviour {

  /**
   * AGV出入物料动作组合.
   *
   * @param importMode     接收/输出物料
   * @param imExAction     agv接收输出物料动作
   * @param interactBuffer 对应工位台动作相应
   */
  public ImExportItemBehaviour(boolean importMode, ActionCaller imExAction,
      Behaviour interactBuffer) {
    if (importMode) {
      addSubBehaviour(imExAction);
      addSubBehaviour(interactBuffer);
    } else {
      addSubBehaviour(interactBuffer);
      addSubBehaviour(imExAction);
    }
  }
}
