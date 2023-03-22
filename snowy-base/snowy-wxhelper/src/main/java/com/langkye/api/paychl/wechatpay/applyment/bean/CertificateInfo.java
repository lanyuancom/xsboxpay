package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

@Data
public class CertificateInfo{

    private String	certCopy;
    private String	certType;
    private String	certNumber;
    private String	merchantName;
    private String	companyAddress;
    private String	legalPerson;
    private String	periodBegin;
    private String	periodEnd;

}
