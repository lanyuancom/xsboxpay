package com.langkye.api.paychl.wechatpay.applyment.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求方式
 *
 * @author langkye
 */

@AllArgsConstructor
@Getter
public enum RequestMethod {
    /**GET请求*/
    GET("GET"),
    /**POST请求*/
    POST("POST")
    ;

    /**请求方式*/
    private final String code;
}
