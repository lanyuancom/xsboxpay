package com.langkye.api.paychl.wechatpay.tx.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 结算信息[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class SettleInfo implements Serializable {
    /**是否指定分账*/
    private Boolean profitSharing;
}
