package vip.xiaonuo.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
/**
 * MultipartFile
 */
public class HttpIOUtil {
    private static Logger log = LoggerFactory.getLogger(HttpIOUtil.class);
    private final static String BOUNDARY = UUID.randomUUID().toString()
            .toLowerCase().replaceAll("-", "");// 边界标识
    private final static String PREFIX = "--";// 必须存在
    private final static String LINE_END = "\r\n";
    /**
     *  POST Multipart Request
     *  @Description: 
     *  @param requestUrl 请求url
     *  @param requestText 请求参数
     *  @param requestFile 请求上传的文件
     *  @return
     *  @throws Exception
     */
    public static String sendRequest(String requestUrl,
            Map<String, String> requestText, MultipartFile requestFile) throws Exception{
        HttpURLConnection conn = null;
        InputStream input = null;
        OutputStream os = null;
        BufferedReader br = null;
        StringBuffer buffer = null;
        try {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            conn.connect();
            // 往服务器端写内容 也就是发起http请求需要带的参数
            os = new DataOutputStream(conn.getOutputStream());
            // 请求参数部分
            writeParams(requestText, os);
            // 请求上传文件部分
            writeFile(requestFile, os);
            // 请求结束标志
            String endTarget = PREFIX + BOUNDARY + PREFIX + LINE_END;
            os.write(endTarget.getBytes());
            os.flush();
            // 读取服务器端返回的内容
            System.out.println("======================响应体=========================");
            System.out.println("ResponseCode:" + conn.getResponseCode()
                    + ",ResponseMessage:" + conn.getResponseMessage());
            if(conn.getResponseCode()==200){
                input = conn.getInputStream();
            }else{
                input = conn.getErrorStream();
            }
            br = new BufferedReader(new InputStreamReader( input, "UTF-8"));        
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
            //......
            System.out.println("返回报文:" + buffer.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
                if (os != null) {
                    os.close();
                    os = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
                throw new Exception(ex);
            }
        }
        return buffer.toString();
    }
    /**
     * 对post参数进行编码处理并写入数据流中
     * @throws Exception 
     * 
     * @throws IOException
     * 
     * */
    private static void writeParams(Map<String, String> requestText,
            OutputStream os) throws Exception {
        try{
            String msg = "请求参数部分:\n";
            if (requestText == null || requestText.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();
                Set<Map.Entry<String, String>> set = requestText.entrySet();
                Iterator<Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Entry<String, String> entry = it.next();
                    requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    requestParams.append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey()).append("\"").append(LINE_END);
                    requestParams.append("Content-Type: text/plain; charset=utf-8")
                            .append(LINE_END);
                    requestParams.append("Content-Transfer-Encoding: 8bit").append(
                            LINE_END);
                    requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                    requestParams.append(entry.getValue());
                    requestParams.append(LINE_END);
                }
                os.write(requestParams.toString().getBytes());
                os.flush();
                msg += requestParams.toString();
            }
            //System.out.println(msg);
        }catch(Exception e){
            log.error("writeParams failed", e);
            throw new Exception(e);
        }
    }
    /**
     * 对post上传的文件进行编码处理并写入数据流中
     * 
     * @throws IOException
     * 
     * */
    private static void writeFile(MultipartFile requestFile,
            OutputStream os) throws Exception {
        InputStream is = null;
        try{
            String msg = "请求上传文件部分:\n";
            if (requestFile == null || requestFile.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();
                requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                requestParams.append("Content-Disposition: form-data; name=\"file\"; filename=\"")
                        .append(requestFile.getOriginalFilename()).append("\"")
                        .append(LINE_END);
                requestParams.append("Content-Type:")
                        .append("application/octet-stream")
                        .append(LINE_END);
                requestParams.append("Content-Transfer-Encoding: 8bit").append(
                        LINE_END);
                requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                os.write(requestParams.toString().getBytes());
                os.write(requestFile.getBytes());
                os.write(LINE_END.getBytes());
                os.flush();
                msg += requestParams.toString();
            }
            //System.out.println(msg);
        }catch(Exception e){
            log.error("writeFile failed", e);
            throw new Exception(e);
        }finally{
            try{
                if(is!=null){
                    is.close();
                }
            }catch(Exception e){
                log.error("writeFile FileInputStream close failed", e);
                throw new Exception(e);
            }
        }
    }


}