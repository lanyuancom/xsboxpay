package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class SalesInfo{

    private List<String> salesScenesType;
    private BizStoreInfo	bizStoreInfo;
    private MpInfo	mpInfo;
    private MiniProgramInfo	miniProgramInfo;
    private AppInfo	appInfo;
    private WebInfo	webInfo;
    private WeworkInfo	weworkInfo;

}
