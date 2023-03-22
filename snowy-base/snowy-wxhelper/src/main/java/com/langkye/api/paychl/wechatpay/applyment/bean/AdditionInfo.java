package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

import java.util.List;

@Data
public class AdditionInfo{

    private String	legalPersonCommitment;
    private String	legalPersonVideo;
    private  List<String> businessAdditionPics;
    private String	businessAdditionMsg;

}
