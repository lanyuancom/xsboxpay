package com.langkye.api.paychl.wechatpay.applyment.bean;

import lombok.Data;

@Data
public class SubjectInfo{

    private String	subjectType;
    private BusinessLicenseInfo	businessLicenseInfo;
    private CertificateInfo	certificateInfo;
    private OrganizationInfo	organizationInfo;
    private String	certificateLetterCopy;
    private IdentityInfo	identityInfo;
    private UboInfo	uboInfo;

}
