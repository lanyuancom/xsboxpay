package com.langkye.api.paychl.wechatpay.applyment.utils;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.langkye.api.paychl.wechatpay.applyment.bean.ModifySettlement;
import com.langkye.api.paychl.wechatpay.applyment.bean.Root;
import com.langkye.api.paychl.wechatpay.applyment.common.ApplymentQueryType;
import com.langkye.api.paychl.wechatpay.applyment.common.FileLocation;
import com.langkye.api.paychl.wechatpay.common.WeChatPayUtil;
import com.langkye.api.paychl.wechatpay.common.WechatPayApiConst;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.WechatPayUploadHttpPost;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * 商户进件API
 *
 * @author langkye
 */
public class WeChatPayIncomingApi {

    private static final Log logger = Log.get();
    WeChatPayUtil weChatPayUtil = new WeChatPayUtil();

    /**
     * 特约商户进件: 提交申请单API
     *
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
     */
    public String applyment(Root root) {
        //加密参数
        try {
            root = EncryptRootField.toEncrypt(root);

            //将驼峰转下划线
            SerializeConfig config = new SerializeConfig();
            config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            String requestParams = JSON.toJSONString(root, config);

            return weChatPayUtil.sendPost(requestParams, WechatPayApiConst.WECHAT_PAY_INCOMING_APPLYMENY.getUrl());
        } catch (IOException | IllegalBlockSizeException e) {
            throw new RuntimeException("请求失败！",e);
        }
    }

    /**
     * 特约商户进件: 查询申请状态
     *      通过【业务申请编号】或【申请单号】查询申请状态
     *
     *
     * @param arg    业务申请编号(business_code) | 申请单号(applyment_id)
     * @param aqType 查询方式 : BUSINESS_CODE | APPLYMENT_ID
     * @return 申请提交结果
     * @see ApplymentQueryType
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_2.shtml
     */
    public String applymentStatus(String arg, ApplymentQueryType aqType){
        String url = "";
        if (ApplymentQueryType.APPLYMENT_ID.equals(aqType)) {
            url = WechatPayApiConst.WECHAT_PAY_INCOMING_STATUS_APPLYMENTID.getUrl();
        } else {
            url = WechatPayApiConst.WECHAT_PAY_INCOMING_STATUS_BUSINESSCODE.getUrl();
        }
        try {
            return weChatPayUtil.sendGet(arg,url);
        } catch (IOException e) {
            throw new RuntimeException("请求失败！：",e);
        }
    }

    /**
     * 特约商户进件: 修改结算帐号API
     * @param modifySettlement 修改结算账号bean
     * @see ModifySettlement
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_3.shtml
     */
    public String modifySettlement(ModifySettlement modifySettlement){
        String url = "";
        final String subMchid = modifySettlement.getSubMchid();

        //移除subMchid属性，该参数作为path参数
        modifySettlement.setSubMchid(null);

        //拼接url
        try {
            final String[] split = WechatPayApiConst.WECHAT_PAY_INCOMING_SETTLEMENT_MODIFY.getUrl().split("\\{sub_mchid\\}");
            url = split[0] + subMchid + split[1];
        } catch (Exception e) {
            throw new RuntimeException("组装修改结算账号URL失败,error:\t" + e);
        }

        //加密
        final String eoAccountNumber = WeChatPayUtil.rsaEncryptOAEP(modifySettlement.getAccountNumber());
        modifySettlement.setAccountNumber(eoAccountNumber);

        //小驼峰属性转换下划线
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String requestParams = JSON.toJSONString(modifySettlement, config);

        //发送请求
        return weChatPayUtil.sendPost(requestParams, url);
    }

    /**
     * 特约商户进件: 查询结算账户API
     * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_4.shtml
     */
    public String querySettlement(String subMchid){
        String url = "";
        //拼接url
        try {
            final String[] split = WechatPayApiConst.WECHAT_PAY_INCOMING_SETTLEMENT_QR.getUrl().split("\\{sub_mchid\\}");
            url = split[0] + subMchid + split[1];
        } catch (Exception e) {
            throw new RuntimeException("组装修改结算账号URL失败,error:\t" + e);
        }

        //发送请求
        return weChatPayUtil.sendGet(url);
    }

    /***
     * 上传图片
     *
     * @param location 图片位置： 本地｜远程
     * @param imageUrl 本地图片路径 或 网络图片url（大小2M以内）
     * @return example: {"media_id":"DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY"}
     */
    public String uploadPic(String imageUrl, FileLocation location)  {
        String result = null;
        try {
            File file = new File(imageUrl);
            URI uri = new URI(WechatPayApiConst.WECHAT_PAY_INCOMING_UPLOAD_PIC.getUrl());
            InputStream inputStream1 = null;
            InputStream inputStream2 = null;

            if (FileLocation.LOCALHOST.equals(location)) {
                inputStream1 = new FileInputStream(file);
                inputStream2 = new FileInputStream(file);

            } else {
                URL fileUrl = new URL(imageUrl);
                inputStream1 = fileUrl.openStream();
                inputStream2 = fileUrl.openStream();
            }

            String sha256 = DigestUtils.sha256Hex(inputStream1);
            HttpPost request = new WechatPayUploadHttpPost.Builder(uri)
                    .withImage(file.getName(), sha256, inputStream2)
                    .build();

            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(WeChatPayUtil.MCH_ID, new PrivateKeySigner(WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH), WeChatPayUtil.getPrivateKey(WeChatPayUtil.MCH_PRI_KEY_PATH))),
                    WeChatPayUtil.API_V3KEY.getBytes("utf-8"));

            CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(WeChatPayUtil.MCH_ID, WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH), WeChatPayUtil.getPrivateKey(WeChatPayUtil.MCH_PRI_KEY_PATH))
                    .withValidator(new WechatPay2Validator(verifier))
                    .build();
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);

            logger.info("请求地址:{}", WechatPayApiConst.WECHAT_PAY_INCOMING_UPLOAD_PIC.getUrl());
            logger.info("上传内容:{}", imageUrl);
            logger.info("请求参数:{}", request.getEntity());
            logger.info("返回参数:{}\n", result);
            Map map = JSONUtil.toBean(result,Map.class);
            result = map.get("media_id")+"";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上传文件失败");
        }
        return result;
    }

}
