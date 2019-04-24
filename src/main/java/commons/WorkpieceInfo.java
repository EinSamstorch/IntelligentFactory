package commons;

import java.io.Serializable;
import java.util.List;

/**
 * 单个工件所携带信息.
 *
 * @author <a href="mailto:junfeng_pan96@qq.com">junfeng</a>
 * @version 1.0.0.0
 * @since 1.8
 */


public class WorkpieceInfo implements Serializable {
    private String orderId;
    private String workpieceId;
    private String goodsId;
    private String detailSize;
    private String providerId;
    private String preOwnerId;
    private String curOwnerId;
    private String lastAgvId;
    private int bufferPos;
    private List<String> processPlan;

    public WorkpieceInfo(String orderId, String workpieceId, String goodsId, String detailSize) {
        this.orderId = orderId;
        this.workpieceId = workpieceId;
        this.goodsId = goodsId;
        this.detailSize = detailSize;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getWorkpieceId() {
        return workpieceId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public String getDetailSize() {
        return detailSize;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getPreOwnerId() {
        return preOwnerId;
    }

    public void setPreOwnerId(String preOwnerId) {
        this.preOwnerId = preOwnerId;
    }

    public String getCurOwnerId() {
        return curOwnerId;
    }

    public void setCurOwnerId(String curOwnerId) {
        this.curOwnerId = curOwnerId;
    }

    public String getLastAgvId() {
        return lastAgvId;
    }

    public void setLastAgvId(String lastAgvId) {
        this.lastAgvId = lastAgvId;
    }

    public int getBufferPos() {
        return bufferPos;
    }

    public void setBufferPos(int bufferPos) {
        this.bufferPos = bufferPos;
    }

    public List<String> getProcessPlan() {
        return processPlan;
    }

    public void setProcessPlan(List<String> processPlan) {
        // List 内容是 String，不可变类型 无需深拷贝
        this.processPlan = processPlan;
    }
}
