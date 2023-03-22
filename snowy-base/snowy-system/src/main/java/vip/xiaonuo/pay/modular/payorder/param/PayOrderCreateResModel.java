
package vip.xiaonuo.pay.modular.payorder.param;

public class PayOrderCreateResModel{
    private String payOrderId;
    private String mchOrderNo;
    private Integer orderState;
    private String payDataType;
    private String payData;
    private String errCode;
    private String errMsg;

    public PayOrderCreateResModel() {
    }

    public String getPayOrderId() {
        return this.payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchOrderNo() {
        return this.mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public Integer getOrderState() {
        return this.orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getPayDataType() {
        return this.payDataType;
    }

    public void setPayDataType(String payDataType) {
        this.payDataType = payDataType;
    }

    public String getPayData() {
        return this.payData;
    }

    public void setPayData(String payData) {
        this.payData = payData;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
