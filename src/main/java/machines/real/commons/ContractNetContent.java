package machines.real.commons;

import java.io.Serializable;

/**
 * 合同网标书内容.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ContractNetContent implements Serializable {
    private Integer offerPrice;

    public ContractNetContent(Integer offerPrice) {
        this.offerPrice = offerPrice;
    }

    public Integer getOfferPrice() {
        return offerPrice;
    }
}
