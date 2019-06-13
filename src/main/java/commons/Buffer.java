package commons;

/**
 * 工位台.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class Buffer {
    private Integer index;
    private WorkpieceInfo wpInfo;

    public Integer getIndex() {
        return index;
    }

    public WorkpieceInfo getWpInfo() {
        return wpInfo;
    }

    public void setWpInfo(WorkpieceInfo wpInfo) {
        this.wpInfo = wpInfo;
    }

    public Buffer(Integer index) {
        this.index = index;
    }
}
