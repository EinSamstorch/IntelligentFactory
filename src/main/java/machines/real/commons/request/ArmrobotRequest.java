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
    private String from;
    private String to;
    private String goodsid;
    private Integer step;

    public ArmrobotRequest(String from, String to, String goodsid, Integer step) {
        this.from = from;
        this.to = to;
        this.goodsid = goodsid;
        this.step = step;
    }

    public ArmrobotRequest(ArmrobotRequest request) {
        this.from = request.getFrom();
        this.to = request.getTo();
        this.goodsid = request.getGoodsid();
        this.step = request.getStep();
    }

    public ArmrobotRequest(String from, String to, String goodsid) {
        this(from, to, goodsid, 0);
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

    public void setStep(Integer step) {
        this.step = step;
    }
}
