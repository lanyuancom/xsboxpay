package com.langkye.api.paychl.wechatpay.common;

import lombok.Getter;

/**
 * 微信支付API常量
 *
 * @author langkye
 */
@Getter
public enum WechatPayApiConst {
    /**微信支付API常量定义*/
    //----------微信特约商户进件   ----------
    WECHAT_PAY_INCOMING_APPLYMENY("https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/","微信特约商户进件：最后一个斜杠'/'不能省"),
    WECHAT_PAY_INCOMING_STATUS_BUSINESSCODE("https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/business_code","查询申请状态:通过业务申请编号查询申请状态"),
    WECHAT_PAY_INCOMING_STATUS_APPLYMENTID("https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/applyment_id","查询申请状态:通过申请单号查询申请状态"),
    WECHAT_PAY_INCOMING_SETTLEMENT_MODIFY("https://api.mch.weixin.qq.com/v3/apply4sub/sub_merchants/{sub_mchid}/modify-settlement","修改结算帐号API"),
    WECHAT_PAY_INCOMING_SETTLEMENT_QR("https://api.mch.weixin.qq.com/v3/apply4sub/sub_merchants/{sub_mchid}/settlement","查询结算账户API"),
    WECHAT_PAY_INCOMING_UPLOAD_PIC("https://api.mch.weixin.qq.com/v3/merchant/media/upload","微信图片上传"),
    //----------普通商户基础支付API----------
    WECHAT_PAY_TX_JSAPI("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi","JSAPI下单"),
    WECHAT_PAY_TX_QR_ID("https://api.mch.weixin.qq.com/v3/pay/transactions/id/{transaction_id}","查询订单：通过微信支付订单号查询"),
    WECHAT_PAY_TX_QR_OTN("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}","查询订单：通过商户订单号查询"),
    WECHAT_PAY_TX_CLOSE("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/{out_trade_no}/close","关闭订单API"),
    WECHAT_PAY_TX_REFUND("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds","申请退款API"),
    WECHAT_PAY_TX_REFUND_QR("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/{out_refund_no}","查询单笔退款API"),
    WECHAT_PAY_TX_BILL_TRADEBILL("https://api.mch.weixin.qq.com/v3/bill/tradebill","申请交易账单API"),
    WECHAT_PAY_TX_BILL_FUNDFLOWBILL("https://api.mch.weixin.qq.com/v3/bill/fundflowbill","申请资金账单API"),
    //----------服务商基础支付API  ----------
    WECHAT_PAY_PARTNER_TX_JSAPI("https://api.mch.weixin.qq.com/v3/pay/partner/transactions/jsapi","JSAPI下单"),
    WECHAT_PAY_PARTNER_TX_QR_ID("https://api.mch.weixin.qq.com/v3/pay/partner/transactions/id/{transaction_id}","查询订单：通过微信支付订单号查询"),
    WECHAT_PAY_PARTNER_TX_QR_OTN("https://api.mch.weixin.qq.com/v3/pay/partner/transactions/out-trade-no/{out_trade_no}","查询订单：通过商户订单号查询"),
    WECHAT_PAY_PARTNER_TX_CLOSE("https://api.mch.weixin.qq.com/v3/pay/partner/transactions/out-trade-no/{out_trade_no}/close","关闭订单API"),
    WECHAT_PAY_PARTNER_TX_REFUND("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds","申请退款API"),
    WECHAT_PAY_PARTNER_TX_REFUND_QR("https://api.mch.weixin.qq.com/v3/refund/domestic/refunds/{out_refund_no}","查询单笔退款API"),
    WECHAT_PAY_PARTNER_TX_BILL_TRADEBILL("https://api.mch.weixin.qq.com/v3/bill/tradebill","申请交易账单API"),
    WECHAT_PAY_PARTNER_TX_BILL_FUNDFLOWBILL("https://api.mch.weixin.qq.com/v3/bill/fundflowbill","申请资金账单API"),
    ;

    /**接口url*/
    private final String url;
    /**接口描述*/
    private final String description;

    private WechatPayApiConst(String url, String description){
        this.url = url;
        this.description = description;
    }
}
