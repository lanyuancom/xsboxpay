package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class AppInfo{

    private String	appAppid;
    private String	appSubAppid;
    private List<String> appPics;

}
