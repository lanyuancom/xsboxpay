package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class HiDesUtils {
    public HiDesUtils() {
    }

    public static String desEnCode(String srcStr) {
        try {
            Key localException = jdMethod_super("cputest".getBytes());
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(1, localException);
            return byteArr2HexStr(localCipher.doFinal(srcStr.getBytes()));
        } catch (InvalidKeyException |NoSuchAlgorithmException | NoSuchPaddingException var3) {
            var3.printStackTrace();
        }  catch (Exception var6) {
            var6.printStackTrace();
        }

        return "0";
    }

    public static String desDeCode(String desStr) {
        try {
            Key localException = jdMethod_super("cputest".getBytes());
            Cipher localCipher = Cipher.getInstance("DES");
            localCipher.init(2, localException);
            return new String(localCipher.doFinal(hexStr2ByteArr(desStr)));
        } catch (InvalidKeyException |NoSuchAlgorithmException | NoSuchPaddingException var3) {
            var3.printStackTrace();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return "0";
    }

    private static Key jdMethod_super(byte[] paramArrayOfByte) throws Exception {
        byte[] arrayOfByte = new byte[8];

        for(int localSecretKeySpec = 0; localSecretKeySpec < paramArrayOfByte.length && localSecretKeySpec < arrayOfByte.length; ++localSecretKeySpec) {
            arrayOfByte[localSecretKeySpec] = paramArrayOfByte[localSecretKeySpec];
        }

        SecretKeySpec var3 = new SecretKeySpec(arrayOfByte, "DES");
        return var3;
    }

    public static String byteArr2HexStr(byte[] paramArrayOfByte) throws Exception {
        int i = paramArrayOfByte.length;
        StringBuffer localStringBuffer = new StringBuffer(i * 2);

        for(int j = 0; j < i; ++j) {
            int k;
            for(k = paramArrayOfByte[j]; k < 0; k += 256) {
                ;
            }

            if(k < 16) {
                localStringBuffer.append("0");
            }

            localStringBuffer.append(Integer.toString(k, 16));
        }

        return localStringBuffer.toString();
    }

    public static byte[] hexStr2ByteArr(String paramString) throws Exception {
        byte[] arrayOfByte1 = paramString.getBytes();
        int i = arrayOfByte1.length;
        byte[] arrayOfByte2 = new byte[i / 2];

        for(int j = 0; j < i; j += 2) {
            String str = new String(arrayOfByte1, j, 2);
            arrayOfByte2[j / 2] = (byte)Integer.parseInt(str, 16);
        }

        return arrayOfByte2;
    }

    public static void main(String arg[]) {
        String str = "dbc2c603db13d0212559c9c4dc01b82dde16bc982da5dbbccd4d315c69723588434ce71cb0011bf311031e5a424963f7e74f30d8cce7c9f11f5998438af6adc2efb5b0525faf117e";
        System.out.println(str.length());
//        System.out.println(HiDesUtils.desEnCode("阮鑫"));
//        System.out.println(HiDesUtils.desDeCode("ef0f2e3a873f4f25"));
    }

}
