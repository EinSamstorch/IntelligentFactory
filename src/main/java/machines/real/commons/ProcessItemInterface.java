package machines.real.commons;

import jade.core.behaviours.Behaviour;

/**
 * @author junfeng
 */

public interface ProcessItemInterface {
    /**
     * 定义处理工件的动作集合，一般是 SequentialBehaviour
     * @return 父类Behaviour 动作集
     */
    public Behaviour getBehaviour();
}
