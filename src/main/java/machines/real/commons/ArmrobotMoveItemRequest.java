package machines.real.commons;

import java.io.Serializable;

/**
 * move item.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ArmrobotMoveItemRequest implements Serializable {
    private String from;
    private String to;
    private String goodsid;
    private Integer step;

    public ArmrobotMoveItemRequest(String from, String to, String goodsid, Integer step) {
        this.from = from;
        this.to = to;
        this.goodsid = goodsid;
        this.step = step;
    }

    public ArmrobotMoveItemRequest(String from, String to, String goodsid){
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
}
