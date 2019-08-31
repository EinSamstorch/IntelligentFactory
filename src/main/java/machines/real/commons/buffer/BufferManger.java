package machines.real.commons.buffer;


/**
 * 工位台管理者.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */

public class BufferManger {
    private Buffer[] buffers;

    public BufferManger(int[] indexes) {
        buffers = new Buffer[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            buffers[i] = new Buffer(indexes[i]);
        }
    }

    /**
     * 检查buffer是否已满
     *
     * @return true 已满, false 未满
     */
    public Boolean isBufferFull() {
        for (Buffer buffer1 : buffers) {
            if (buffer1.getWpInfo() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获得一个空的buffer
     *
     * @return 获得一个空的Buffer对象, 若buffer已满 则返回null
     */
    public Buffer getEmptyBuffer() {
        if (isBufferFull()) {
            return null;
        }
        for (Buffer buffer : buffers) {
            if (buffer.isEmpty()) {
                return buffer;
            }
        }
        return null;
    }

    public Integer getEvaluateTimeByIndex(int index) {
        return buffers[index].getEvaluateTime();
    }

    /**
     * 获取所有工位台加工时长总和
     *
     * @return 单位 秒
     */
    public Integer getAllWaitingTime() {
        int totalTime = 0;
        for (Buffer buffer : buffers) {
            if(buffer.getBufferState().getState() == BufferState.STATE_ARRIVING) {
                totalTime += buffer.getEvaluateTime();
            }
            if (buffer.getBufferState().getState() == BufferState.STATE_WAITING) {
                // 待加工 返回 预估时间
                totalTime += buffer.getEvaluateTime();
            } else if (buffer.getBufferState().getState() == BufferState.STATE_PROCESSING) {
                // 正在加工  返回 当前时间 - 开始加工时间
                totalTime += buffer.getRemainProcessTime();
            }
        }
        return totalTime;
    }

    /**
     * 获取 一个 等待加工的 buffer
     *
     * @return 等待加工的buffer对象, {null} 若无待加工buffer
     */
    public Buffer getWaitingBuffer() {
        for (Buffer buffer : buffers) {
            if (buffer.getBufferState().getState() == BufferState.STATE_WAITING) {
                return buffer;
            }
        }
        return null;
    }

    /**
     * 通过指定index reset指定buffer
     *
     * @param index buffer 的 index
     * @return true 成功复位, false 未找到buffer
     */
    public Boolean resetBufferByIndex(int index) {
        for (Buffer buffer : buffers) {
            if (buffer.getIndex() == index) {
                buffer.reset();
                return true;
            }
        }
        return false;
    }


    public int getFreeQuantity() {
        int quantity = 0;
        for (Buffer buffer : buffers) {
            if (buffer.getWpInfo() == null) {
                quantity += 1;
            }
        }
        return quantity;
    }
}
