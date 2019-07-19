package machines.real.commons.request;

import commons.WorkpieceInfo;

import java.io.Serializable;

/**
 * 运输任务请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class AgvRequest implements Serializable {
    /**
     * 从from地点取货 放置到 to 地点
     */
    private Integer from;
    private Integer to;
    private WorkpieceInfo wpInfo;

    public AgvRequest(Integer from, Integer to, WorkpieceInfo wpInfo) {
        this.from = from;
        this.to = to;
        this.wpInfo = wpInfo;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public WorkpieceInfo getWpInfo() {
        return wpInfo;
    }
}
