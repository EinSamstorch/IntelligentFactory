package Commons;

import jade.core.AID;

import java.io.Serializable;

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
    private AID providerAID;
    private AID preOwnnerAID;
    private AID curOwnnerAID;
    private AID lastAGV;
    private int bufferPos;

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

    public AID getProviderAID() {
        return providerAID;
    }

    public void setProviderAID(AID providerAID) {
        this.providerAID = providerAID;
    }

    public AID getPreOwnnerAID() {
        return preOwnnerAID;
    }

    public void setPreOwnnerAID(AID preOwnnerAID) {
        this.preOwnnerAID = preOwnnerAID;
    }

    public AID getCurOwnnerAID() {
        return curOwnnerAID;
    }

    public void setCurOwnnerAID(AID curOwnnerAID) {
        this.curOwnnerAID = curOwnnerAID;
    }

    public AID getLastAGV() {
        return lastAGV;
    }

    public void setLastAGV(AID lastAGV) {
        this.lastAGV = lastAGV;
    }

    public int getBufferPos() {
        return bufferPos;
    }

    public void setBufferPos(int bufferPos) {
        this.bufferPos = bufferPos;
    }
}
