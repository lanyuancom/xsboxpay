package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {

    public static Map<String, String> createKeys() throws NoSuchAlgorithmException{
        //为RSA算法创建一个KeyPairGenerator对象
    	KeyPair keyPair = KeyGenerator.generateKeyPair();
    	KeyPair asnKeyPair = keyPair.toASNKeyPair();
        //得到公钥
    	String publicKey = asnKeyPair.getPublicKey();
        String privateKey = asnKeyPair.getPrivateKey();
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKey);
        keyPairMap.put("privateKey", privateKey);

        return keyPairMap;
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, String publicKey){
        try{
        	KeyWorker publicWorker = new KeyWorker(publicKey, KeyFormat.ASN);
        	return publicWorker.encrypt(data);
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, String privateKey){
        try{
        	KeyWorker privateWorker = new KeyWorker(privateKey, KeyFormat.ASN);
        	return privateWorker.decrypt(data);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, String privateKey){
        try{
        	KeyWorker privateWorker = new KeyWorker(privateKey, KeyFormat.ASN);
        	return privateWorker.encrypt(data);
        }catch(Exception e){
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, String publicKey){
        try{
        	KeyWorker publicWorker = new KeyWorker(publicKey, KeyFormat.ASN);
        	return publicWorker.decrypt(data);
        }catch(Exception e){
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
    	Map<String,String> keys = createKeys();
    	String publicKey = keys.get("publicKey");
    	String privateKey = keys.get("privateKey");
    	System.out.println("private:"+privateKey);
    	System.out.println("public:"+publicKey);
    	String conent = "sdsfalfasdl2对方是否";
    	String en = publicEncrypt(conent, publicKey);
    	System.out.println("公钥加密："+en);
    	String de = privateDecrypt(en, privateKey);
    	System.out.println("私钥解密："+de);
	}
    
}
