package machines.real.commons.request;

import java.io.Serializable;

/**
 * move item.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmRequest implements Serializable {
    private final String from;
    private final String to;
    private final String goodsid;
    private final Integer step;

    public ArmRequest(String from, String to, String goodsid, Integer step) {
        this.from = from;
        this.to = to;
        this.goodsid = goodsid;
        this.step = step;
    }

    public ArmRequest(ArmRequest request) {
        this.from = request.from;
        this.to = request.to;
        this.goodsid = request.goodsid;
        this.step = request.step;
    }

    public ArmRequest(String from, String to, String goodsid) {
        this(from, to, goodsid, 0);
    }

    public static ArmRequest unloadRequest(ArmRequest request) {
        return new ArmRequest(
                request.to,
                request.from,
                request.goodsid,
                0);
    }

    public static ArmRequest reverseRequest(ArmRequest request) {
        return new ArmRequest(
                request.to,
                request.to,
                request.goodsid,
                0);
    }

    public static ArmRequest nextStep(ArmRequest request) {
        return new ArmRequest(
                request.from,
                request.to,
                request.goodsid,
                request.step + 1);
    }

    public static ArmRequest endStep(ArmRequest request) {
        return new ArmRequest(
                request.from,
                request.to,
                request.goodsid,
                -1
        );
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGoodsid() {
        return goodsid;
    }

    public Integer getStep() {
        return step;
    }

    @Override
    public String toString() {
        return String.format("FROM:%s;TO:%s", from, to);
    }
}
