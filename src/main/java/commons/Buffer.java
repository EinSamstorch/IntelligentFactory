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
    private Boolean arrived = false;
    private Boolean processed = false;
    private Boolean onMachine = false;

    public int getIndex() {
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

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isOnMachine() {
        return onMachine;
    }

    public void setOnMachine(boolean onMachine) {
        this.onMachine = onMachine;
    }

    public void reset(){
        this.arrived = false;
        this.processed = false;
        this.onMachine = false;
    }
}
