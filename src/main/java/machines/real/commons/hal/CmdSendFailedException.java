package machines.real.commons.hal;

/**
 * 发送控制命令失败 异常.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class CmdSendFailedException extends HALException {
    public CmdSendFailedException(String message) {
        super(message);
    }
}
