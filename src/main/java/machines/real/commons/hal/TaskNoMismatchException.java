package machines.real.commons.hal;

/**
 * 响应消息的 任务号 不正确.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class TaskNoMismatchException extends HALException {
    public TaskNoMismatchException(String message) {
        super(message);
    }
}
