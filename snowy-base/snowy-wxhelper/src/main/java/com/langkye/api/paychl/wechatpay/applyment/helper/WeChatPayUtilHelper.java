package com.langkye.api.paychl.wechatpay.applyment.helper;

import com.langkye.api.paychl.wechatpay.applyment.bean.*;
import com.langkye.api.paychl.wechatpay.applyment.common.ApplymentQueryType;
import com.langkye.api.paychl.wechatpay.applyment.common.FileLocation;
import com.langkye.api.paychl.wechatpay.applyment.utils.WeChatPayIncomingApi;
import com.langkye.api.paychl.wechatpay.common.WeChatPayUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.util.Map;

/**
 * @author langkye
 */
public class WeChatPayUtilHelper {

    public static void main(String[] args) throws Exception {


        //PrivateKey privateKey = WeChatPayWrapper.getPrivateKey(WeChatPayWrapper.MCH_PRI_KEY_PATH);
        //System.out.println("privateKey = " + privateKey);

        //特约商户进件
        //applymentHelper();

        //查询申请状态
        //queryStatusByApplymentIdHelper();
        //queryStatusByBusinessCodeHelper();

        //修改结算帐号API
        //modifySettlementHelper();

        //查询结算账户API
        //querySettlementHelper();

        //图片上传
        //uploadPicHelper();

        //加密 ｜ 解密
        //rsaEncytpAndDecrypt();

        //获取证书序列号
        //getCertSerialNo();
    }


    /***
     * 接口示例：APPLYMENT_20210312093332
     *      - 特约商户进件: 提交申请单API
     * 返回示例:
     *      - {"applyment_id":2000002177371579}
     *      - {"applyment_id":2000002177345181}
     */
    public static void applymentHelper(Map<String,Object> map) {
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        Root root = BeanHelper.toConvert(map);
        System.out.println("root = " + root);
        String applyment = weChatPayIncomingApi.applyment(root);
        System.out.println("applyment = " + applyment);
    }


    /***
     * 接口示例：
     *      - 查询申请状态:通过【申请单号】查询申请状态
     *      返回示例:
     *          {"applyment_id":2000002177293742,"applyment_state":"APPLYMENT_STATE_REJECTED","applyment_state_msg":"","audit_detail":[{"reject_reason":"填写的商户名称与营业执照注册号不匹配，查询营业执照名称应为：国际铜专业协会北京代表处，请核实填写内容与查询名称是否一致（若一致仍无法提交请联系微信支付客服处理）"}],"business_code":"APPLYMENT_20210312031540","sign_url":"https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHk7zwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyRnZTcjlrb3JlUjIxcmNBTzF3Y3MAAgTMF0tgAwQAjScA"}
     */
    public static void queryStatusByApplymentIdHelper() {
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        String applymentStatus = weChatPayIncomingApi.applymentStatus("2000002309316653", ApplymentQueryType.APPLYMENT_ID);
        System.out.println("applymentStatus = " + applymentStatus);
    }

    /***
     * 接口示例：
     *      - 查询申请状态:通过【业务申请编号】查询申请状态
     *      返回示例：
     *          applymentStatus = {"code":"PARAM_ERROR","detail":{"location":"uri_template","value":"APPLYMENT_20210312084316"},"message":"无法将输入源“/uri_template/applyment_id”映射到目标字段“申请单号 ”中，此字段需要一个合法的 64 位无符号整数"}
     */
    public static void queryStatusByBusinessCodeHelper(){
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        String applymentStatus = weChatPayIncomingApi.applymentStatus("APPLYMENT_20210313065716", ApplymentQueryType.BUSINESS_CODE);
        System.out.println("applymentStatus = " + applymentStatus);
    }

    /***
     * 接口示例：
     *      - 特约商户进件: 修改结算帐号API
     */
    public static void modifySettlementHelper(){
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        final ModifySettlement settlement = BeanHelper.convertModifySettlement();
        final String modifySettlement = weChatPayIncomingApi.modifySettlement(settlement);
        System.out.println("modifySettlement = " + modifySettlement);
    }


    /***
     * 接口示例：
     *      - 特约商户进件: 查询结算账户API
     *      返回示例：
     *          applymentStatus = {"account_type": "ACCOUNT_TYPE_BUSINESS","account_bank": "工商银行","bank_name": "施秉县农村信用合作联社城关信用社","bank_branch_id": "402713354941","account_number": "***************8678","verify_result": "VERIFY_SUCCESS"}
     */
    public static void querySettlementHelper(){
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        final String querySettlement = weChatPayIncomingApi.querySettlement("12345");
        System.out.println("querySettlement = " + querySettlement);
    }


    /**
     * 接口示例：
     *      - 上传图片
     *      返回示例：
     *          {"media_id":"ysJR93_VqntdiTwOPM2fPBKrawp6xg__rWmGoQ5nyZG4E3-Uz9sbhihjzUdKn5N3eAF_nYt29TXCj6Xjgc9otSkxZhHf5NDQNO_PNgkLH4c"}
     *          {"media_id":"ysJR93_VqntdiTwOPM2fPK4yBkMaktixy63YmljUvdWhBYEjtcUVEvv8yyOiKrtzDCFfC4Y7Z9r5G91s7_SmGv6S_bZtcE7NbhRm-eEryVY"}
     */
    public static void uploadPicHelper() {
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        //本地文件
        String filePath1 = "/Users/langkye/Pictures/1.png";
        String media1 = weChatPayIncomingApi.uploadPic(filePath1, FileLocation.LOCALHOST);
        System.out.println("media1 = " + media1);

        //远程文件
        String filePath2 = "http://mocard-1251489075.cos.ap-beijing.myqcloud.com/etc/user/zx/advertising/b8f3cd0387fd6f278e9e4bcf823c71f.png";
        String media2= weChatPayIncomingApi.uploadPic(filePath2, FileLocation.REMOTE);
        System.out.println("media2 = " + media2);

    }

    /**
     * 加密消息 | 解密
     *
     * @throws IOException
     */
    public static void rsaEncytpAndDecrypt() throws IOException, IllegalBlockSizeException, BadPaddingException {
        //X509Certificate certificate = WeChatPayWrapper.getCertificate(WeChatPayWrapper.WECHATPAY_PUB_KEY_PATH);
        //Cipher cipher = WeChatPayWrapper.getCipher(certificate);
        //String s = WeChatPayWrapper.rsaEncryptOAEP("xx", cipher);
        //System.out.println(s);

        String source = "{\"code\":200,\"message\":\"success\"}";
        System.out.println("source = " + source);

        //使用自己的公钥加密（然后使用自己的私钥解密）【测试用，请使用对方的公钥加密, 然后将加密信息给对方解密】
        String encrypt = WeChatPayUtil.rsaEncryptOAEP(source, WeChatPayUtil.getCertificate(WeChatPayUtil.MCH_PUB_KEY_PATH));

        //使用微信公钥加密（微信平台使用商户的公钥解密）
        //String encrypt = WeChatPayWrapper.rsaEncryptOAEP(source);

        System.out.println("encrypt = " + encrypt);

        //使用自己的私钥解密（解密由自己的公钥加密的信息）
        String target = WeChatPayUtil.rsaDecryptOAEP(encrypt);
        System.out.println("target = " + target);

        System.out.println("验证结果：" + source.equals(target));
    }

    /**
     * 获取证书序列号
     */
    public static void getCertSerialNo(){
        //传入微信平台公钥，获取微信平台证书序列号
        final String weChatSerialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);
        System.out.println("weChatSerialNo = " + weChatSerialNo);

        //传入商户自己的公钥，获取商户的证书序列号
        final String mchSerialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);
        System.out.println("mchSerialNo = " + mchSerialNo);
    }

}