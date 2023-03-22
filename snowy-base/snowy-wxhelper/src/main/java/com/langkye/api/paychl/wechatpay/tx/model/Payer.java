package com.langkye.api.paychl.wechatpay.tx.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 支付者信息[PartnerJsapiRequest下单子对象]
 *
 * @author langkye
 * @date 2021/6/6
 */
@Data
public class Payer implements Serializable {
    /**
     * 用户服务标识(二选一)
     * 描述：用户在服务商appid下的唯一标识。
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     */
    private String spOpenid;

    /**
     * 用户子标识(二选一)
     * 描述：用户在子商户appid下的唯一标识。若传sub_openid，那sub_appid必填
     * 示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     */
    private String subOpenid;
}
