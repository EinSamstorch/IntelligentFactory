package commons.tools;

/**
 * 消息创建失败异常.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

class MessageCreateFailedException extends Exception {

  MessageCreateFailedException(String message) {
    super(message);
  }
}
