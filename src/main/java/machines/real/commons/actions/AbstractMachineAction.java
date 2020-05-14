package machines.real.commons.actions;

/**
 * 抽象封装部分实现.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */
public abstract class AbstractMachineAction implements MachineAction {

  private Object resultExtra;

  @Override
  public void setResultExtra(Object extra) {
    resultExtra = extra;
  }

  @Override
  public Object getResultExtra() {
    return resultExtra;
  }
}
