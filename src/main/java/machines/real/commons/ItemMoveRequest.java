package machines.real.commons;

import java.io.Serializable;

/**
 * .
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class ItemMoveRequest implements Serializable {
    private Integer itemPosition;

    public ItemMoveRequest(Integer itemPosition) {
        this.itemPosition = itemPosition;
    }

    public Integer getItemPosition() {
        return itemPosition;
    }
}