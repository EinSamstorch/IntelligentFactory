package machines.real.commons.hal;

/**
 * 动作执行失败 异常.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class ActionFailedException extends HALException {
    public ActionFailedException(String message) {
        super(message);
    }
}
