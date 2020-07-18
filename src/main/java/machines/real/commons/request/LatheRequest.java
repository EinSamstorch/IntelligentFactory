package machines.real.commons.request;

import commons.order.WorkpieceStatus;

import java.io.Serializable;

/**
 * 车床夹紧工件请求
 *
 * @author: <a href = "mailto:einsamstorch@qq.com">Liqun_Wang</a>
 * @date: 2020/7/18 16:34
 * @since: 1.8
 */
public class LatheRequest implements Serializable {

    private final boolean grab;
    private final String orderId;
    private final String workpieceId;

    public LatheRequest(boolean grab, WorkpieceStatus wpInfo) {
        this.grab = grab;
        this.orderId = wpInfo.getOrderId();
        this.workpieceId = wpInfo.getWorkpieceId();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getWorkpieceId() {
        return workpieceId;
    }

    public boolean isGrab() {
        return grab;
    }
}
