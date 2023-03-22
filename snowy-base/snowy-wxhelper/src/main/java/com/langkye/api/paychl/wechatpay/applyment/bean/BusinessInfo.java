package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

@Data
public class BusinessInfo{

    private String	merchantShortname;
    private String	servicePhone;
    private SalesInfo	salesInfo;

}
