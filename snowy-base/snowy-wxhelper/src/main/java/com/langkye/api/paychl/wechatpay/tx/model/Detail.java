package com.langkye.api.paychl.wechatpay.tx.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 优惠功能[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class Detail implements Serializable {
    /**
     * 订单原价
     * 描述：1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
     *      2、当订单原价与支付金额不相等，则不享受优惠。
     *      3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
     * 示例值：608800
     */
    private Integer costPrice;

    /**
     * 商品小票ID
     * 描述：商家小票ID
     * 示例值：微信123
     */
    private Integer invoiceId;
}
