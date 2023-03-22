package vip.xiaonuo.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 持久化示例。如何将内存中的数据保存起来，并没有一定的格式，任何人
 * 都可以根据自己的喜好来制定。持久化需要文件操作，所以请务必先弄懂
 * 如何读写文件。
 */
public class FileDataUtil {

    // 文件名可随意指定，你可以用文本编辑器打开这个文件（注意，记事本无法处理换行）
    static String filename = System.getProperty("user.home") + File.separator+".xsbox"+File.separator+"pay";
 
    public static void main(String[] args) throws Exception {
        // 将这个程序运行两遍。
        // 第一遍它会创建一些 Person 对象并保存到文件；
        // 第二遍它会从文件中读取对象数据并显示出来。
        //savekey("525<>!@s一工￥？要","xsbox.ini");
        //System.out.println(readPersons("xsbox.ini"));
    }

    public static void deleteAll(String fileName){
        File f = new File(filename+File.separator+fileName);
        if(f.exists()){
            f.delete();
        }
    }

    public static void deleteKey(String key,String fileName){
        try {
            Map<String,String> map = readDataAll(fileName);
            map.remove(key);
            String data = JSONUtil.toJsonStr(map);
            File f = new File(filename);
            if(!f.exists()){
                f.mkdirs();
            }
            // 保存文件内容
            OutputStream os = new FileOutputStream(filename+File.separator+fileName);
            os.write(base64Encrypt(data));
            os.flush();    //将存储在管道中的数据强制刷新出去
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
 
    // 加密
    public static void savekey(String key,String value,String fileName){
        try {
            Map<String,String> map = readDataAll(fileName);
            map.remove(key);
            map.put(key, value);
            String data = JSONUtil.toJsonStr(map);
            File f = new File(filename);
            if(!f.exists()){
                f.mkdirs();
            }
            // 保存文件内容
            OutputStream os = new FileOutputStream(filename+File.separator+fileName);
            os.write(base64Encrypt(data));
            os.flush();    //将存储在管道中的数据强制刷新出去
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // 从文件中读取 Person 对象
    public static String readDataKey(String key,String fileName){
        try {
            File f = new File(filename+File.separator+fileName);
            if(!f.exists()){
                return null;
            }
            FileInputStream inputStream = new FileInputStream(filename+File.separator+fileName);// 创建FileInputStream类对象
            byte byt[] = new byte[1024];// 创建byte数组
            int len = inputStream.read(byt);
            String str = new String(byt,0,len);
            inputStream.close();// 关闭流
            String strMap =  new String(base64Decrypt(str.getBytes()));
            if(StrUtil.isBlank(strMap)){
                return null;
            }
            Map<String,String> map = JSONUtil.toBean(strMap,Map.class);
            return map.get(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
 
    // 从文件中读取 Person 对象
    public static Map<String,String> readDataAll(String fileName){
        try {
            File f = new File(filename+File.separator+fileName);
            if(!f.exists()){
                return new HashMap<>();
            }
            FileInputStream inputStream = new FileInputStream(filename+File.separator+fileName);// 创建FileInputStream类对象
            byte byt[] = new byte[1024];// 创建byte数组
            int len = inputStream.read(byt);
            String str = new String(byt,0,len);
            inputStream.close();// 关闭流
            String strMap =  new String(base64Decrypt(str.getBytes()));
            if(StrUtil.isBlank(strMap)){
                return new HashMap<>();
            }
            return JSONUtil.toBean(strMap,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * base64加密
     * @param content 待加密内容
     * @return byte[]
     */
    private static byte[] base64Encrypt(final String content) {
        return Base64.getEncoder().encode(content.getBytes());
    }

    /**
     * base64解密
     * @param encoderContent 已加密内容
     * @return byte[]
     */
    private static byte[] base64Decrypt(final byte[] encoderContent) {
        return Base64.getDecoder().decode(encoderContent);
    }
}
