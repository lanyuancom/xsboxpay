package com.langkye.api.paychl.wechatpay.applyment.utils;

import cn.hutool.core.util.StrUtil;
import com.langkye.api.paychl.wechatpay.applyment.bean.*;
import com.langkye.api.paychl.wechatpay.common.WeChatPayUtil;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.security.cert.X509Certificate;

/**
 * 加密进件字段
 * @author langkye
 */
public class EncryptRootField {
    public static Root toEncrypt(Root root) throws IOException, IllegalBlockSizeException {
        /**
         * 加载微信证书
         */
        X509Certificate certificate = WeChatPayUtil.getCertificate(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);
        Cipher cipher = WeChatPayUtil.getCipher(certificate);

        /**
         * 加密 超级管理员信息
         */
        encryptContactInfo(root,cipher);


        /**
         * 加密 主体信息
         */
        encryptSubjectInfo(root,cipher);


        /**
         * 结算银行
         */
        encryptBankAccountInfo(root,cipher);

        return root;
    }

    /**
     * 加密银行信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptBankAccountInfo(Root root, Cipher cipher) throws IllegalBlockSizeException {
        BankAccountInfo bankAccountInfo = root.getBankAccountInfo();
        if (bankAccountInfo==null){
            return;
        }

        String accountName = bankAccountInfo.getAccountName();
        if (!StrUtil.isBlank(accountName)){
            bankAccountInfo.setAccountName(WeChatPayUtil.rsaEncryptOAEP(accountName,cipher));
        }

        String accountNumber = bankAccountInfo.getAccountNumber();
        if (!StrUtil.isBlank(accountNumber)){
            bankAccountInfo.setAccountNumber(WeChatPayUtil.rsaEncryptOAEP(accountNumber,cipher));
        }
        root.setBankAccountInfo(bankAccountInfo);
    }

    /**
     * 加密主体信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptSubjectInfo(Root root, Cipher cipher) throws IllegalBlockSizeException {
        SubjectInfo subjectInfo = root.getSubjectInfo();
        if (subjectInfo==null){
            return;
        }

        //主体-法人
        IdentityInfo identityInfo = subjectInfo.getIdentityInfo();
        if (identityInfo!=null){
            //主体-法人-身份证
            IdCardInfo idCardInfo = identityInfo.getIdCardInfo();
            if (idCardInfo!=null){
                String idCardName = idCardInfo.getIdCardName();
                if (!StrUtil.isBlank(idCardName)){
                    idCardInfo.setIdCardName(WeChatPayUtil.rsaEncryptOAEP(idCardName,cipher));
                }

                String idCardNumber = idCardInfo.getIdCardNumber();
                if (!StrUtil.isBlank(idCardNumber)){
                    idCardInfo.setIdCardNumber(WeChatPayUtil.rsaEncryptOAEP(idCardNumber,cipher));
                }

                String idCardAdree = idCardInfo.getIdCardAddress();
                if (!StrUtil.isBlank(idCardAdree)){
                    idCardInfo.setIdCardAddress(WeChatPayUtil.rsaEncryptOAEP(idCardAdree,cipher));
                }

                identityInfo.setIdCardInfo(idCardInfo);
            }


            //主体-法人-其他
            IdDocInfo idDocInfo = identityInfo.getIdDocInfo();
            if (idDocInfo!=null){
                String idDocName = idDocInfo.getIdDocName();
                if (!StrUtil.isBlank(idDocName)){
                    idDocInfo.setIdDocName(WeChatPayUtil.rsaEncryptOAEP(idDocName,cipher));
                }

                String idDocNumber = idDocInfo.getIdDocNumber();
                if (!StrUtil.isBlank(idDocNumber)){
                    idDocInfo.setIdDocNumber(WeChatPayUtil.rsaEncryptOAEP(idDocNumber,cipher));
                }

                identityInfo.setIdDocInfo(idDocInfo);
            }
            subjectInfo.setIdentityInfo(identityInfo);
        }





        //主体-最终受益人
        //UboInfo uboInfo = subjectInfo.getUboInfo();
        //uboInfo.setName(WeChatPayUtil.rsaEncryptOAEP(uboInfo.getName(),cipher));
        //uboInfo.setIdNumber(WeChatPayUtil.rsaEncryptOAEP(uboInfo.getIdNumber(),cipher));

        //subjectInfo.setUboInfo(uboInfo);
        root.setSubjectInfo(subjectInfo);
    }

    /**
     * 加密联系人信息
     * @param root
     * @param cipher
     * @throws IllegalBlockSizeException
     */
    private static void encryptContactInfo(Root root,Cipher cipher) throws IllegalBlockSizeException {
        ContactInfo contactInfo = root.getContactInfo();
        if (contactInfo == null){
            return;
        }

        String contactName = contactInfo.getContactName();
        if (!StrUtil.isBlank(contactName)){
            contactInfo.setContactName(WeChatPayUtil.rsaEncryptOAEP(contactName,cipher));
        }

        String contactIdNumber = contactInfo.getContactIdNumber();
        if (!StrUtil.isBlank(contactIdNumber)){
            contactInfo.setContactIdNumber(WeChatPayUtil.rsaEncryptOAEP(contactIdNumber,cipher));
        }

        String openid = contactInfo.getOpenid();
        if (!StrUtil.isBlank(openid)){
            contactInfo.setOpenid(WeChatPayUtil.rsaEncryptOAEP(openid,cipher));
        }

        String mobilePhone = contactInfo.getMobilePhone();
        if (!StrUtil.isBlank(mobilePhone)){
            contactInfo.setMobilePhone(WeChatPayUtil.rsaEncryptOAEP(mobilePhone,cipher));
        }

        String contactEmail = contactInfo.getContactEmail();
        if (!StrUtil.isBlank(contactEmail)){
            contactInfo.setContactEmail(WeChatPayUtil.rsaEncryptOAEP(contactEmail,cipher));
        }

        root.setContactInfo(contactInfo);
    }
}
