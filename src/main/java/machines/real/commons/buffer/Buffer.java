package machines.real.commons.buffer;

import commons.order.WorkpieceStatus;
import commons.tools.LoggerUtil;

/**
 * 工位台.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class Buffer {
    /**
     * Buffer编号 唯一
     */
    private Integer index;
    /**
     * Buffer上 工件信息
     */
    private WorkpieceStatus wpInfo;
    /**
     * Buffer状态信息
     */
    private BufferState bufferState = new BufferState();

    /**
     * Buffer上工件预估时间
     */
    private Integer evaluateTime = 0;
    /**
     * Buffer上工件加工开始时间
     */
    private Long processTimestamp = 0L;

    public Buffer(int index) {
        this.index = index;
    }

    /**
     * 返回还有多长时间加工完成
     *
     * 对于车床加工而言，该函数有问题，第二段加工时 会更新 processTimestamp，导致预估时间不正确
     * @return 单位 秒
     */
    public Integer getRemainProcessTime() {
        int remainTime = evaluateTime - (int) (System.currentTimeMillis() - processTimestamp) / 1000;
        return remainTime > 0 ? remainTime : 0;
    }

    public void setProcessTimestamp(Long processTimestamp) {
        this.processTimestamp = processTimestamp;
    }

    public Integer getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Integer evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public Integer getIndex() {
        return index;
    }

    public WorkpieceStatus getWpInfo() {
        return wpInfo;
    }

    public void setWpInfo(WorkpieceStatus wpInfo) {
        this.wpInfo = wpInfo;
    }

    public void reset() {
        LoggerUtil.hal.debug(String.format("Buffer %d reset", index));
        wpInfo = null;
        evaluateTime = 0;
        processTimestamp = 0L;
        bufferState.resetState();
    }

    public Boolean isEmpty() {
        return wpInfo == null;
    }

    public BufferState getBufferState() {
        return bufferState;
    }
}
