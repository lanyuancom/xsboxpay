package vip.xiaonuo.core;

import org.junit.Test;

/**
 *
 *
 * 身份认证测试
 *
 * @author yangbo
 *
 * @version 创建时间：2017年8月14日 上午11:09:23
 *
 *
 */
public class AuthTest {
    //当测试authTest时候，把genSecretTest生成的secret值赋值给它
    private static String secret = "U74XNAEYG4NNKWA4";

    @Test
    public void testGenerator(){
       /* secret = GoogleGenerator.generateSecretKey ();
        String qrCode = GoogleGenerator.getQRBarcode ( "wsygshz",secret );
        System.out.println ( "qrCode="+qrCode+";key="+secret );*/
    }

    @Test
    public void testValidCode(){
        String code = "205162";
        long time = System.currentTimeMillis ();
        GoogleGenerator g = new GoogleGenerator ();
        boolean result = g.check_code ( secret,code,time );
        System.out.println ( result );
    }
} 

