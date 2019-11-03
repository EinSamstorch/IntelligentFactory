package machines.virtual.worker.behaviours.cycle;

import commons.order.WorkpieceStatus;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public final class RetryMessage {

  final WorkpieceStatus wpInfo;
  final String serviceType;

  public RetryMessage(WorkpieceStatus wpInfo, String serviceType) {
    this.wpInfo = wpInfo;
    this.serviceType = serviceType;
  }
}
