package commons.tools;

/**
 * 运输任务请求.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class TransportRequest {
    /**
     * 从from地点取货 放置到 to 地点
     */
    private Integer from;
    private Integer to;

    public TransportRequest(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }
}
