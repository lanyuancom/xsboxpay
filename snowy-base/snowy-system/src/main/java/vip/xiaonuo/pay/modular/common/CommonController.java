package vip.xiaonuo.pay.modular.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.util.GoogleGenerator;
import vip.xiaonuo.sys.core.cache.RedisCache;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class CommonController {

    @Resource
    RedisCache redisCache;

    @GetMapping("/generateQRCode/imgs")
    public void generateQRCode(HttpServletResponse response) throws IOException {
        String account = LoginContextHolder.me().getSysLoginUser().getAccount();
        String secret = redisCache.get(account);
        if(StrUtil.isBlank(secret)){
            secret = GoogleGenerator.generateSecretKey ();
            redisCache.put(account,secret,60*60*24l);
        }
        String qrCode = GoogleGenerator.getQRBarcode ( account,secret );
        QrCodeUtil.generate(qrCode, 250, 250,"png", response.getOutputStream());
    }
    /**
     *
     * @param file
     * @param fileName
     * @param savePath
     * @throws Exception
     */
    public void upload(MultipartFile file,String fileName,String savePath) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new Exception("未选择需上传文件");
        }

        File fileUpload = new File(savePath);
        if (!fileUpload.exists()) {
            fileUpload.mkdirs();
        }

        fileUpload = new File(savePath, fileName);
        if (fileUpload.exists()) {
            fileUpload.delete();
        }
        try {
            file.transferTo(fileUpload);
        } catch (IOException e) {
            throw new Exception("上传文件到服务器失败：" + e.toString());
        }
    }
}
