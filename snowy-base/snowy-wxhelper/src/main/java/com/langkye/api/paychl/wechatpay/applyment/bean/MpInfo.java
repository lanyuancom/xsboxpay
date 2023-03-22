package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class MpInfo{

    private String	mpAppid;
    private String	mpSubAppid;
    private List<String> mpPics;

}
