package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 工具类
 */

public class PayUtils {
	//编码格式UTF-8
	public static final String CHARSET 	= "UTF-8";
	private static JSONObject decrypt;

	
	//生成RSA签名：sign
	public static String buildSign(JSONObject json,String merKey) {
		StringBuffer buffer		= new StringBuffer();
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		for (Entry<String, Object> entry : json.entrySet()) {
			if (entry.getValue() != null) {
				treeMap.put(entry.getKey(), entry.getValue());
			}
		}
		for (Entry<String, Object> entry : treeMap.entrySet()) {
			if (entry.getValue() != null) {
				buffer.append(entry.getValue());
			}
		}
		buffer.append(merKey.replaceAll("(\r\n|\r|\n|\n\r)", ""));
		System.out.println(buffer.toString());
		return HashUtil.md5(buffer.toString()).toUpperCase();
	}

	public static String buildDataPrivate(JSONObject json, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return RSAUtil.privateEncrypt(json.toJSONString(), privateKey);
	}

	public static String buildDataPublic(JSONObject json, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return RSAUtil.publicEncrypt(json.toJSONString(), publicKey);
	}

	public static JSONObject decryptDataPrivate(String data, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		JSONObject result	= null;
		String jsonStr = RSAUtil.privateDecrypt(data.trim(), privateKey);
		result = JSON.parseObject(jsonStr);
		return result;
	}

	public static JSONObject decryptDataPublic(String data, String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		JSONObject result	= null;
		String jsonStr = RSAUtil.publicDecrypt(data.trim(), publicKey);
		result = JSON.parseObject(jsonStr);
		return result;
	}

	//生成密文：data
	public static String buildData(JSONObject json, String merKey) {
		return AES.encryptWithKeyBase64(json.toJSONString(), merKey);
	}

	//解密data，获得明文参数
	public static JSONObject decrypt(String data, String merKey) {
		JSONObject result	= null;
		//2.使用AESKey解开data，取得明文参数；解密后格式为json
		String jsonStr = AES.decryptWithKeyBase64(data.trim(), merKey.trim());
		result = JSON.parseObject(jsonStr);
		return result;
	}

	// sign验签
	public static boolean checkSign(JSONObject json,String merKey) {
		//获取明文参数中的sign。
		String signPay	= StrUtil.trimToEmpty(json.getString("sign"));
		//将明文参数中sign之外的其他参数，拼接成字符串
		StringBuffer buffer	= new StringBuffer();
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		for (Entry<String, Object> entry : json.entrySet()) {
			if (entry.getValue() != null) {
				treeMap.put(entry.getKey(), entry.getValue());
			}
		}
		for(Entry<String, Object> entry : treeMap.entrySet()) {
			String key		= formatStr(entry.getKey());
			String value	= formatStr(entry.getValue());
			if("sign".equals(key)) {
				continue;
			}
			buffer.append(value);
		}
		buffer.append(merKey.replaceAll("(\r\n|\r|\n|\n\r)", ""));
		//result为true时表明验签通过
		String checkSign = HashUtil.md5(buffer.toString()).toUpperCase();
		return signPay.equals(checkSign);
	}

	//一键支付post请求
	public static JSONObject httpPost(String url, String merAccount, String data) {
		//请求参数为如下：merAccount、data
		Map<String, Object> paramMap	= new HashMap<String, Object>();
		paramMap.put("data", data);
		paramMap.put("merAccount", merAccount);
		String responseBody = HttpRequest.post(url)
				.charset(CHARSET)
				.form(paramMap)//表单内容
				.timeout(6000)//超时，毫秒
				.execute().body();
		Log.get().info(" responseBody > "+responseBody);
		return JSON.parseObject(responseBody);
	}
	
	//get请求
	public static JSONObject httpGet(String url, String merAccount, String data) {
		//请求参数为如下：merAccount、data
		Map<String, Object> paramMap	= new HashMap<String, Object>();
		paramMap.put("data", data);
		paramMap.put("merAccount", merAccount);
		String responseBody = HttpRequest.get(url)
				.charset(CHARSET)
				.form(paramMap)//表单内容
				.timeout(6000)//超时，毫秒
				.execute().body();
		return JSON.parseObject(responseBody);
	}
	
	//get请求
	public static JSONObject httpGet(String url, String merAccount, String appid, String data) {
		//请求参数为如下：merAccount、data
		Map<String, Object> paramMap	= new HashMap<String, Object>();
		paramMap.put("data", data);
		paramMap.put("appid", appid);
		paramMap.put("merAccount", merAccount);
		String responseBody = HttpRequest.get(url)
				.charset(CHARSET)
				.form(paramMap)//表单内容
				.timeout(6000)//超时，毫秒
				.execute().body();
		return JSON.parseObject(responseBody);
	}
	
	//字符串格式化
	public static String formatStr(Object text) {
		return (text == null) ? "" : (text+"").trim();
	}

}
