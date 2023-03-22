package com.langkye.api.paychl.wechatpay.tx.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单金额[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class Amount implements Serializable {
    /**
     * 描述：订单总金额，单位为分。
     * 示例值：100
     */
    private Integer total;

    /**
     * 描述：CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     */
    private String currency;
}
