package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class WeworkInfo{

    private String	corpId;
    private String	subCorpId;
    private List<String> weworkPics;

}
