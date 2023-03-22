package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

@Data
public class IdentityInfo{

    private String	idDocType;
    private IdCardInfo	idCardInfo;
    private IdDocInfo	idDocInfo;
    private boolean owner;

}
