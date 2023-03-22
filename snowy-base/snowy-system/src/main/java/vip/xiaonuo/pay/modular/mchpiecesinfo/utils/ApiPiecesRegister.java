package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ClientProtocolException;
import vip.xiaonuo.pay.modular.mchpiecesinfo.entity.MchPiecesInfo;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进件demo
 */
public class ApiPiecesRegister {
    //http://platformtest.mhxxkj.com/paygateway/register/merchant/v1
	private static String host = "https://platform.mhxxkj.com/paygateway";
    private static String bossHost = "https://platform.mhxxkj.com/payboss";
    static String merAccount = "1b9e9bd5f2ad42159b457d1bcc97052c"; // 商户标识
    static String merKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMfhRsLHvwA8bd3zOruF6HQzIpldMprVknbs7p/asjj0B398XC8rGN9BRGVIFN481H/3viqTmqFj0ArA8ZmnrwXD5hXhkVOwSnAmFLkgCLyWF1ZfVj6S75Ro7Yt+Fy5+hSmOyZlUDtzahCDDJxdfj9mGRRraP3B7fyLhoGISC9RpAgMBAAECgYAhgxfGGI9hjsAs8lbqke0Dbn9ij0moxB4malsn2hb/jYqkSUl7bxJgfCTnhkpZaIGIYxUzNkQ6wu1ioC7hwEeLt+kya06z0dFT+tp2IRHYQw5V/phrR5r8A9FaC9ETz4v+Za4f/FjHRDaZ9Bp3aRU1QwF7sqE9hUHig/eCV7cZsQJBAPCdWNln6zZMND52nXzzeiZb5W0Zj5BrLZWzse6MwMi9eEUApiBnFcrfpqM+LHsDU+4bjEhJeXANIDk4oUVoAlsCQQDUqSNwUy3Ayu+rQ9/pWdF3UD7JdKYQEhAvFP7PfcNW6iDk2GGTz/PdFq4/3XdYL72ZH52ST8yVejd0cyl5AzeLAkEA6HIZc23A/SOBFRbh7eQJDSWZ7lvTJWFlGEoQirPPSh5AIekOlB8CHosRIILf1bbRTiWuz+arOmRzzNO4eKSSIQJAUVpO+1Zmp1066GqzysIYdqtVCZ49NOKkHE7M17WFt8cjREhes7wWz1ow2K+xSyQgROaqUeGDIx+4/SxdDjUj1wJAQ+o05S72znLp/YSX0He4RvEIohH8ql52R7yp6pDp8YnrdtQJKJsOkOIWqsIDkH6l9Nu8m6FEpye6/vNTvMloCA=="; // 商户密钥

    private static Log log = Log.get();
    
    public static JSONObject registerquery(String subUserNo,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{
        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("subUserNo", subUserNo);
        
        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info(JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/queryMerchant/v1", merAccount ,data);
        //System.out.println(object.toJSONString());
	}
	
	public static JSONObject queryProduct(String subUserNo,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{
        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("requestId","requestId123");
        json.put("subUserNo", subUserNo);
        
        String sign = PayUtils.buildSign(json,merKey);
        //json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info(JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/queryProduct/v1", merAccount ,data);
	}
	
	public static JSONObject product(String subUserNo,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{
    	
        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("requestId","requestId123545");
        json.put("time", time);
        json.put("subUserNo", subUserNo);
        ArrayList<JSONObject> prolist = new ArrayList<>();
        JSONObject product1 = new JSONObject();
        product1.put("payType", "MINIAPP_WEIXIN");
        product1.put("feeRate", 1.20);
        product1.put("productAppid", "wx955ee2e9dddc7dda");
        product1.put("productName", "阅读");
        prolist.add(product1);
//        prolist.add(product3);
        String productInfos = JSONObject.toJSONString(prolist);
        json.put("productInfos", productInfos);
        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info(JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/payProduct/v1", merAccount ,data);
	}
	
    public static JSONObject registerFile(String requestId,String subUserNo,String fileType,byte[] bytes,String fileName,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException, ClientProtocolException, IOException{
        //String requestId = "requestId21312"; // 商户订单号
        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("merAccount", merAccount);
        json.put("subUserNo", subUserNo);
        json.put("requestId",requestId);
        json.put("time", time);
        json.put("fileType", fileType);
        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        HttpResponse response = HttpRequest.post(bossHost+"/register/file/v1")
                .setConnectionTimeout(300000)
                .setReadTimeout(300000)
                .form("data",data)
                .form("merAccount", merAccount)
                .form("file", bytes,fileName)
                .execute();
        String body = response.body();
        log.info(body);
        return JSON.parseObject(body);
        /*CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000000).build();
        HttpPost httpPost = new HttpPost(bossHost+"/register/file/v1");
        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        File file = new File("C:/Users/xing/Desktop/1.png");
        multipartEntityBuilder.addBinaryBody("file",file);
        //multipartEntityBuilder.addPart("comment", new StringBody("This is comment", ContentType.TEXT_PLAIN));
        multipartEntityBuilder.addTextBody("data", data);
        multipartEntityBuilder.addTextBody("merAccount", merAccount);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
             
        httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        int statusCode= httpResponse.getStatusLine().getStatusCode();
        if(statusCode == 200){
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseEntity.getContent()));
            StringBuffer buffer = new StringBuffer();
            String str = "";
            while(!StringUtils.isEmpty(str = reader.readLine())) {
                buffer.append(str);
            }
            System.out.println(buffer.toString());
        }
             
        httpClient.close();
        if(httpResponse!=null){
            httpResponse.close();
        }*/
    }
	
	public static JSONObject filequery(String subUserNo,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{
    	
        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("subUserNo", subUserNo);
        json.put("requestId", "requestid1234");
        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info(JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/queryFile/v1", merAccount ,data);
	}

    public static JSONObject confirm(String subUserNo,String merKey,String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{

        long time = System.currentTimeMillis()/1000; // 时间戳
        JSONObject json = new JSONObject();
        json.put("time", time);
        json.put("subUserNo", subUserNo);
        json.put("requestId", "requestid1234");
        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        //System.out.println("sign:");
        //System.out.println(json.toJSONString());
        String data = PayUtils.buildDataPrivate(json,merKey);
        //System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info(JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/confirm/v1", merAccount ,data);
    }

	
    public static JSONObject register(MchPiecesInfo mchPiecesInfo,String merKey, String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{
        JSONObject json = new JSONObject();
        json.put("orderId",mchPiecesInfo.getId());
        //json.put("subuserNo",mchPiecesInfo.getMerNo());//子商户编号(作为修改时唯一标识)
        //商户签约名称,营业执照上的工商注册名称（个人商户除外），个人商户请传“店铺名”
        json.put("fullName", StrUtil.isBlank(mchPiecesInfo.getBusinessFullName())?mchPiecesInfo.getBusinessShortName():mchPiecesInfo.getBusinessFullName());
        json.put("shortName", mchPiecesInfo.getBusinessShortName());
        json.put("certType",mchPiecesInfo.getNetType());
        long time = System.currentTimeMillis()/1000; // 时间戳
        json.put("time", time);
        //营业执照号(个人填身份证号)
        json.put("certNo", StrUtil.isBlank(mchPiecesInfo.getBusinessNo())?mchPiecesInfo.getLegarCard():mchPiecesInfo.getBusinessNo());
        json.put("isOrgcodeLong", mchPiecesInfo.getOrgcodeLong());
        json.put("orgcodeStart", mchPiecesInfo.getOrgcodeStart());
        json.put("orgcodeExpiry", mchPiecesInfo.getOrgcodeEnd());
        json.put("accountLicense", mchPiecesInfo.getBusinessAccountLicenseNo());

        json.put("legarName", mchPiecesInfo.getLegarName());
        json.put("legarCard", mchPiecesInfo.getLegarCard());
        json.put("legarPhone", mchPiecesInfo.getLegarPhone());

        json.put("legarCardStart", mchPiecesInfo.getCardStart());
        json.put("legarCardExpiry", mchPiecesInfo.getCardEnd());

        json.put("contactName", mchPiecesInfo.getContactName());
        json.put("contactPhone", mchPiecesInfo.getContactPhone());
        json.put("contactEmail", mchPiecesInfo.getContactEmail());

        json.put("servePhone", mchPiecesInfo.getServePhone());


        json.put("level1No", mchPiecesInfo.getLevelOneNo());
        json.put("level2No", mchPiecesInfo.getLevelTwoNo());


        json.put("province", mchPiecesInfo.getSubUserProvinceCode());
        json.put("city", mchPiecesInfo.getSubUserCityCode());
        json.put("district", mchPiecesInfo.getSubUserDistrictCode());
        json.put("address", mchPiecesInfo.getSubUserAddress());

        json.put("accountType", mchPiecesInfo.getAccountType());
        json.put("accountName", mchPiecesInfo.getBankAccount());
        json.put("accountNo", mchPiecesInfo.getBankNo());
        json.put("headBankcode", mchPiecesInfo.getBankCode());

        json.put("bankProvince", mchPiecesInfo.getBankProvinceCode());
        json.put("bankCity", mchPiecesInfo.getBankCityCode());
        json.put("settType", mchPiecesInfo.getSettType());
        json.put("settCycle", mchPiecesInfo.getSettCycle());

        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        String data = PayUtils.buildDataPrivate(json,merKey);
        System.out.println(data);
        Map<String, Object> mp = new HashMap<>();
        mp.put("data", data);
        mp.put("merAccount", merAccount);
        log.info("register -> "+JSONObject.toJSONString(mp));
        return PayUtils.httpPost(host+"/register/merchant/v1", merAccount ,data);
    }

    public static JSONObject splitOrder(String requestNo, String subUserNo,String amount,String merKey, String merAccount) throws NoSuchAlgorithmException, InvalidKeySpecException{

        long time = System.currentTimeMillis()/1000; // 时间戳

        String notifyUrl = "http://platform.mhxxkj.com/paygateway/mHTestNotifyController/pay";
        JSONObject json = new JSONObject();
        json.put("merAccount", merAccount);
        json.put("requestNo", requestNo);
        json.put("orderId", requestNo);
        json.put("time", time);
        //json.put("mbOrderId", mbOrderId);
        json.put("notifyUrl", notifyUrl);

        /**分账明细**/
        JSONObject splitBillDetail2 = new JSONObject();
        splitBillDetail2.put("subUserNo", subUserNo);
        splitBillDetail2.put("splitBillType", "2");
        splitBillDetail2.put("splitBillValue", amount);
        List<JSONObject> list = new ArrayList<>();
        list.add(splitBillDetail2);
        json.put("splitBillDetail", JSONObject.toJSONString(list));

        String sign = PayUtils.buildSign(json,merKey);
        json.put("sign", sign);
        String data = PayUtils.buildDataPrivate(json,merKey);
        return PayUtils.httpPost("https://platform.mhxxkj.com/paygateway/mbpay/splitOrder/v1", merAccount, data);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        register();
        //{"code":"000000","data":{"merchantName":"福建米花网络科技有限公司","orderId":"orderrid12346576","subUserNo":"20004078"},"msg":"SUCCESS"}
    	String subUserNo = "20004078";
//    	registerquery(subUserNo);
//		registerFile();
//    	filequery(subUserNo);
//    	product(subUserNo);
//    	queryProduct(subUserNo);
	}
}
