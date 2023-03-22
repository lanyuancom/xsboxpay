package com.langkye.api.paychl.wechatpay.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import okhttp3.HttpUrl;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.springframework.core.io.ClassPathResource;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

/**
 * @author langkye
 * @doc https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/applyment4sub.shtml
 */
public class WeChatPayUtil {
    private static final Log logger = Log.get();

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    public static String WECHATPAY_PUB_KEY_PATH;
    public static String MCH_PUB_KEY_PATH;
    public static String MCH_PRI_KEY_PATH;
    public static String MCH_ID = "1606010980";
    public static String API_V3KEY = "etrqerqe32424312HHH3452345dsf432";

    static {
        Properties properties = new Properties();
        try {
            InputStream inputStream = (new ClassPathResource("wechatpay-config.properties")).getInputStream();
            properties.load(inputStream);
            MCH_PUB_KEY_PATH = properties.getProperty("wechat.pay.mch.public");
            WECHATPAY_PUB_KEY_PATH = properties.getProperty("wechat.pay.pem");
            MCH_PRI_KEY_PATH = properties.getProperty("wechat.pay.mch.private");
            API_V3KEY = properties.getProperty("wechat.pay.mch.apiV3Key");
            MCH_ID = properties.getProperty("wecaht.pay.mch.mchId");
        } catch (IOException e) {
            System.out.println("读取配置文件[wechatpay-config.properties]失败!");
        }
    }

//-----------------------------------------------------------工具-----------------------------------------------------------//
    /**
     * 获取证书序列号
     *
     * @param certPath 获取商户证书序列号 传递商号证书路径 apiclient_cert
     * @return
     * @throws IOException
     */
    public static String getSerialNo(String certPath) {
        X509Certificate certificate = getCertificate(certPath);
        return certificate.getSerialNumber().toString(16).toUpperCase();
    }

    /**
     * 获取证书。
     *
     * @param filename 证书文件路径  (required)
     * @return X509证书
     */
    public static X509Certificate getCertificate(String filename) {
        ClassPathResource resource = new ClassPathResource(filename);
        try (InputStream inputStream = resource.getInputStream()) {
            try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate) cf.generateCertificate(bis);
                cert.checkValidity();
                return cert;
            } catch (CertificateExpiredException e) {
                throw new RuntimeException("证书已过期", e);
            } catch (CertificateNotYetValidException e) {
                throw new RuntimeException("证书尚未生效", e);
            } catch (CertificateException e) {
                throw new RuntimeException("无效的证书文件", e);
            }
        } catch (IOException e) {
            throw new RuntimeException("读取证书失败", e);
        }
    }

    /**
     * 获取token
     *
     * @param method       请求方式         (请求类型GET、POST url(请求url)
     * @param url          请求路径         (请求路径)
     * @param body         请求参数         (请求body，GET请求时body传""，POST请求时body为请求参数的json串)
     * @param merchantId   商户号           (商户号)
     * @param certSerialNo API证书序列号     (证书序列号)
     * @return 签名串
     * @throws Exception 异常
     */
    public static String getToken(String method, String url, String body, String merchantId, String certSerialNo) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signStr = "";
        HttpUrl httpurl = HttpUrl.parse(url);
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        long timestamp = System.currentTimeMillis() / 1000;
        if (StrUtil.isEmpty(body)) {
            body = "";
        }
        assert httpurl != null;
        String message = buildMessage(method, httpurl, timestamp, nonceStr, body);
        String signature = sign(message.getBytes(StandardCharsets.UTF_8), MCH_PRI_KEY_PATH);
        signStr = "mchid=\"" + merchantId
                + "\",nonce_str=\"" + nonceStr
                + "\",timestamp=\"" + timestamp
                + "\",serial_no=\"" + certSerialNo
                + "\",signature=\"" + signature + "\"";
        return signStr;
    }

    public static String buildMessage(String method, HttpUrl url, long timestamp, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }


    /**
     * 签名
     *
     * @param message
     * @param keyPath
     * @return
     * @throws Exception
     */
    public static String sign(byte[] message, String keyPath) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey(keyPath));
        sign.update(message);
        return Base64.encodeBase64String(sign.sign());
    }

    /**
     * 获取私钥。
     *
     * @param filename 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filename) {
        String content = getContent(filename);
        try {
            String privateKey = content.replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                    new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

    /**
     * 获取Cipher，用于加密
     * @param certificate
     * @return
     */
    public static Cipher getCipher(X509Certificate certificate) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        }
    }

    /**
     * 通过Cipher加密
     *
     * @param message
     * @param cipher  通过getcipher方法获取
     * @return
     * @throws IllegalBlockSizeException
     */
    public static String rsaEncryptOAEP(String message, Cipher cipher) throws IllegalBlockSizeException {
        try {
            if (StrUtil.isBlank(message)) {
                return null;
            }
            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] cipherdata = cipher.doFinal(data);
            return Base64.encodeBase64String(cipherdata);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }

    /**
     * 敏感信息加密。通过证书加密。
     *
     * @param message
     * @param certificate
     * @return
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static String rsaEncryptOAEP(String message, X509Certificate certificate) throws IllegalBlockSizeException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes("utf-8");
            byte[] cipherdata = cipher.doFinal(data);
            return java.util.Base64.getEncoder().encodeToString(cipherdata);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的证书", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
        }
    }

    /**
     * 敏感信息加密。(默认使用微信公钥加密）
     *
     * @param message
     * @return
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static String rsaEncryptOAEP(String message) {
        //return rsaEncryptOAEP(message, getCertificate(MCH_PUB_KEY_PATH));
        final X509Certificate certificate = getCertificate(WECHATPAY_PUB_KEY_PATH);
        final String eo;
        try {
            eo = rsaEncryptOAEP(message, certificate);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("加密失败：" + e);
        } catch (IOException e) {
            throw new RuntimeException("加密失败：" + e);
        }
        return eo;
    }

    /**
     * 解密敏感信息。通过证书解密。
     *
     * @param ciphertext
     * @param privateKey
     * @return
     * @throws BadPaddingException
     * @throws IOException
     */
    public static String rsaDecryptOAEP(String ciphertext, PrivateKey privateKey) throws BadPaddingException, IOException {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] data = java.util.Base64.getDecoder().decode(ciphertext);
            return new String(cipher.doFinal(data), "utf-8");
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("无效的私钥", e);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            throw new BadPaddingException("解密失败");
        }
    }

    /**
     * 解密敏感信息
     *
     * @param message
     * @return
     * @throws IOException
     * @throws IllegalBlockSizeException
     */
    public static String rsaDecryptOAEP(String message) {
        try {
            return rsaDecryptOAEP(message, getPrivateKey(MCH_PRI_KEY_PATH));
        } catch (BadPaddingException e) {
            throw new RuntimeException("解密失败:",e);
        } catch (IOException e) {
            throw new RuntimeException("解密失败:",e);
        }
    }

    /**
     * 获取文本内容
     *
     * @param path
     * @return
     * @throws IOException
     */
    public static String getContent(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            inputStream = resource.getInputStream();
            br = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String data = new String(sb);
        return data;
    }

    public String sendGet(String url){
        //获取商户证书序列号
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);

        //平台证书路径, 获取微信证书序列号
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //设置请求头，请求参数并发起请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Wechatpay-Serial", serialNo);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);

        CloseableHttpClient httpClient = getVerifyHttpClient();

        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new RuntimeException("请求失败："+e);
        }

        //解析响应，返回字符串
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String responseEntityStr = null;
        try {
            responseEntityStr = EntityUtils.toString(httpResponseEntity);
        } catch (IOException e) {
            throw new RuntimeException("转换响应体失败："+e);
        }
        try {
            httpResponse.close();
        } catch (IOException e) {
            logger.error("关闭响应流失败："+e);
        }

        return responseEntityStr;
    }

    public String sendGet(String endArg, String url) throws IOException {
        url = url + "/" + endArg;
        return sendGet(url);
    }

    public String sendGet1(String arg,String url) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        url = url + "/" + arg;

        //获取商户证书序列号
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);
        String token = WeChatPayUtil.getToken("GET", url, null, WeChatPayUtil.MCH_ID, serialNo);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        //平台证书路径, 获取微信证书序列号
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //设置请求头，请求参数并发起请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Wechatpay-Serial", serialNo);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Content-Type", "application/json");
        httpGet.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);
        httpGet.setHeader("Authorization", authorization);

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.build();

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        //解析响应，返回字符串
        HttpEntity httpResponseEntity = httpResponse.getEntity();
        String responseEntityStr = EntityUtils.toString(httpResponseEntity);
        httpResponse.close();

        return responseEntityStr;
    }

    public String sendPost(String requestParams, String url) {
        //微信证书路径，获取微信证书序列号
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //组装请求头、请求参数并发起请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Wechatpay-Serial", serialNo);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        //httpPost.setHeader("user-agent", WeChatPayWrapper.DEFAULT_USER_AGENT);
        httpPost.setEntity(new StringEntity(requestParams, ContentType.create("application/json", "utf-8")));
        CloseableHttpClient httpClient = getVerifyHttpClient();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (IOException e) {
            throw new RuntimeException("请求失败：" + e);
        }

        //获取响应文本
        HttpEntity httpResponseEntity = httpResponse.getEntity();

        String responseEntityStr = null;
        try {
            responseEntityStr = EntityUtils.toString(httpResponseEntity);
        } catch (IOException e) {
            throw new RuntimeException("转换响应体失败：" + e);
        }
        try {
            httpResponse.close();
        } catch (IOException e) {
            logger.error("关闭流失败：" + e);
        }

        return responseEntityStr;
    }

    public String sendPost1(String requestParams, String url) throws IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //获取商户证书序列号
        String serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.MCH_PUB_KEY_PATH);

        String token = WeChatPayUtil.getToken("POST", url, requestParams, MCH_ID, serialNo);
        String authorization = "WECHATPAY2-SHA256-RSA2048 " + token;

        //微信证书路径，获取微信证书序列号
        serialNo = WeChatPayUtil.getSerialNo(WeChatPayUtil.WECHATPAY_PUB_KEY_PATH);

        //组装请求头、请求参数并发起请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Wechatpay-Serial", serialNo);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("user-agent", WeChatPayUtil.DEFAULT_USER_AGENT);
        //如果使用原生的HttpClientBuilder构造 httpclient 需要手动添加
        httpPost.setHeader("Authorization", authorization);
        httpPost.setEntity(new StringEntity(requestParams, ContentType.create("application/json", "utf-8")));

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        CloseableHttpClient httpClient = httpClientBuilder.build();


        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);

        //获取响应文本
        HttpEntity httpResponseEntity = httpResponse.getEntity();

        String responseEntityStr = EntityUtils.toString(httpResponseEntity);
        httpResponse.close();

        return responseEntityStr;
    }

    private static CloseableHttpClient getVerifyHttpClient() {
        ArrayList<X509Certificate> certificates = new ArrayList<>();
        certificates.add(getCertificate(WECHATPAY_PUB_KEY_PATH));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(MCH_ID, getSerialNo(MCH_PUB_KEY_PATH), getPrivateKey(MCH_PRI_KEY_PATH))
                .withWechatpay(certificates)
                .build();

        return httpClient;
    }
}
