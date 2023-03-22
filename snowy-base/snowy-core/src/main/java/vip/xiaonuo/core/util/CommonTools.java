package vip.xiaonuo.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import org.apache.http.HttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.pojo.base.entity.BaseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class CommonTools {
    public static byte[] AES_KEY = "BOX4ChT08phkz59h".getBytes();

    public static Long getId(BaseEntity entity){
        DefaultIdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
        return identifierGenerator.nextId(entity);
    }
    /**
     * 传入原图名称，，获得一个以时间格式的新名称
     *
     * @param fileName
     *            原图名称
     * @return
     */
    public static String generateFileName(String fileName) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        return formatDate + random + extension;
    }
    /**
     * 获取当前访问URL （含协议、域名、端口号[忽略80端口]、项目名）
     * @return: String
     */
    public static String getServerUrl() {

        HttpServletRequest request = HttpServletUtil.getRequest();
        // 访问协议
        String agreement = request.getScheme();
        // 访问域名
        String serverName = request.getServerName();
        // 访问端口号
        int port = request.getServerPort();
        // 访问项目名
        String contextPath = request.getContextPath();
        String url = "%s://%s%s%s";
        String portStr = "";
        if (port != 80) {
            portStr += ":" + port;
        }
        String curl =  String.format(url, agreement, serverName, portStr, contextPath);
        if("/".equals(curl.substring(url.length()-1))){
            curl = curl.substring(0,url.length()-1);
        }
        return curl;
    }
    /** 拼接url参数 **/
    public static String appendUrlQuery(String url, Map<String, Object> map){

        if(StrUtil.isEmpty(url) || map == null || map.isEmpty()){
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        if(url.indexOf("?") < 0){
            sb.append("?");
        }

        //是否包含query条件
        boolean isHasCondition = url.indexOf("=") >= 0;

        for (String k : map.keySet()) {
            if(k != null && map.get(k) != null){
                if(isHasCondition){
                    sb.append("&"); //包含了查询条件， 那么应当拼接&符号
                }else{
                    isHasCondition = true; //变更为： 已存在query条件
                }
                sb.append(k).append("=").append(URLUtil.encodeQuery(map.get(k).toString()));
            }
        }
        return sb.toString();
    }
    /**
     * post请求 带file,map是其余参数
     */

    public static String sendPostWithFile( String postUrl ,MultipartFile file, Map<String, Object> map) {
        DataOutputStream out = null;
        DataInputStream in = null;
        final String newLine = "\r\n";
        final String prefix = "--";
        String json = null;
        try {
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            String BOUNDARY = "-------"+System.currentTimeMillis();
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());

            // 添加参数file
            // File file = new File(filePath);
            StringBuilder sb1 = new StringBuilder();
            sb1.append(prefix);
            sb1.append(BOUNDARY);
            sb1.append(newLine);
            sb1.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getOriginalFilename() + "\"" + newLine);
            sb1.append("Content-Type:application/octet-stream");
            sb1.append(newLine);
            sb1.append(newLine);
            out.write(sb1.toString().getBytes());
            // in = new DataInputStream(new FileInputStream(file));
            in = new DataInputStream(file.getInputStream());
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            out.write(newLine.getBytes());

            StringBuilder sb = new StringBuilder();
            if(map != null){
                int k = 1;
                for (String key : map.keySet()) {
                    if (k != 1) {
                        sb.append(newLine);
                    }
                    sb.append(prefix);
                    sb.append(BOUNDARY);
                    sb.append(newLine);
                    sb.append("Content-Disposition: form-data;name=" + key + "");
                    sb.append(newLine);
                    sb.append(newLine);
                    sb.append(map.get(key));
                    out.write(sb.toString().getBytes());
                    sb.delete(0, sb.length());
                    k++;
                }
            }
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(end_data);
            out.flush();
// 读取服务器端返回的内容
            System.out.println("======================响应体=========================");
            System.out.println("ResponseCode:" + conn.getResponseCode()
                    + ",ResponseMessage:" + conn.getResponseMessage());
            // 定义BufferedReader输入流来读取URL的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            StringBuffer resultStr = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                resultStr.append(line);
            }
            return resultStr.toString();

        } catch (Exception e) {
            Log.get().error("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

}
