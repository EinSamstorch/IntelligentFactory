package CommonTools.HAL;

/**
 * 硬件适配层的异常类.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class HALException extends RuntimeException {
    public HALException(String message) {
        super(message);
    }
}
