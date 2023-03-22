package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class MiniProgramInfo{

    private String	miniProgramAppid;
    private String	miniProgramSubAppid;
    private List<String> miniProgramPics;

}
