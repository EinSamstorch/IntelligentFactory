package machines.real.commons.request;

import java.io.Serializable;

/**
 * move item.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmrobotRequest implements Serializable {
    private final String from;
    private final String to;
    private final String goodsid;
    private final Integer step;

    public ArmrobotRequest(String from, String to, String goodsid, Integer step) {
        this.from = from;
        this.to = to;
        this.goodsid = goodsid;
        this.step = step;
    }

    public ArmrobotRequest(ArmrobotRequest request) {
        this.from = request.from;
        this.to = request.to;
        this.goodsid = request.goodsid;
        this.step = request.step;
    }

    public ArmrobotRequest(String from, String to, String goodsid) {
        this(from, to, goodsid, 0);
    }

    public static ArmrobotRequest unloadRequest(ArmrobotRequest request) {
        return new ArmrobotRequest(request.to,
                request.from,
                request.goodsid,
                0);
    }

    public static ArmrobotRequest reverseRequest(ArmrobotRequest request) {
        return new ArmrobotRequest(request.to,
                request.to,
                request.goodsid,
                0);
    }

    public static ArmrobotRequest nextStep(ArmrobotRequest request) {
        return new ArmrobotRequest(request.from,
                request.to,
                request.goodsid,
                request.step + 1);
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

}
