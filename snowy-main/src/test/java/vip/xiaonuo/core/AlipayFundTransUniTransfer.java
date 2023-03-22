package vip.xiaonuo.core;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.AlipayConfig;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.domain.SignData;
import com.alipay.api.domain.BankcardExtInfo;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.domain.MutipleCurrencyDetail;
import com.alipay.api.FileItem;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;

import java.util.*;

public class AlipayFundTransUniTransfer {


    static String PRIVATE_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKB9IGua6FKVeS5/ZfJhgy51MXWHmBE4+FeEG/UtJ7h6fEETzU6VX2OldwT4FUThlfrObvEJJWiMDqEYrRvoLQi8twCidMWGwCpdMWsyBpdf0oHGpb36oTMAf203Js9aFQJR0c1O8aToG+LVTn6esphqRQ/27xPDEgbI21K/OweuOk9W/FisOs+btzZ8SYQh78y1T/9RV6qxwPY7DN7eMLvnPXo3D9u1hmvXuG2eAP1zuwC9Lg7/Q17I99XyQPOIOwpFpNAtz4JpTq+AOLDpizPfBCdSs4qIQaWOkf0fRf1+G6KLnU7ybqSfcLAYtd57LFtEvpMHbL4vriT/llyY65AgMBAAECggEAdZG/tXbHTikurLTjx5vdFM6wmbwEkoskvlz2M47KNeoQXhMIq5msXLTI8aLm1/ZF6paNhaep3gdRuo4CnjEVn6jTObWMT50pMwAn0rf0rZ+QaG/7sBHM/db1kan9Vw3Jv0g9ieddWhzwqhpWrcszQQWsREmuJ9BLUADmmyC6yuA04nPleS8Z8VAekylSs8lbDU3Qy+6QUe1ccnYE2q0tUjdC/dtkG0YOfSzEIOVkwSs1zScyaxGqscm9EGxP2xVa2tT9YcREE5vmaYowuljBo1XaNpQCR3pgb/w4PLkUXIfy3ZkenVLxfdjROSeAXDpGNF+oY/CvLepNAktre0tueQKBgQD4k2PYhC5c8o7t1BOhSouNcxC32zCtAEwDgQYQUIMG+85v0WYp9SkWpLFx7e32qWjfS6NuMw/DRLcXj5FFl+LjUinJZRaRBI3CEc2gz3B6L9pTk/lNjgetTMwe1Ugc6l22d3w5BHZcYcubnZZV6u4AJ0OhMujEbIaa7NTtkn4QywKBgQCOJzOwKVrrXzXfPr9zeJU3GVTGy31jWT5eVBNRhAVoc6vctT+uQuxDTWtNGtq78OkIOBDSJVvw596pIHklV083KkzHQ9Zd+/r0cqy8sINBIxzjg9nwdAGFiYlXKz8mI2CLjzk4HW1SiDnmyX5HlVu8QXbMpnsefUqfbS6+yU/CCwKBgQCEJRa3S82569i/5jZpUE/P6pPAqe2+iqHeWwD2O1bsp4CfQRqmw4YMW5FZl7shnf36dyNyugJvNGCBTflUneIoJs3AGtVcUqdpvD0seBq7WiUeuVOXPyIlpuX8njWv6YfYML0BMrYZdoPfPm9lTXco7bauQ3oqseb9BVpM0cvi8QKBgC/XMaYPxPGB0XeYfoY2BsHMXKOqyEAvQLYuRXIhpFBK3YQh25SiITFlVL9WUlb99O83LzIKMK8a2ezBaQQQJjDCo6RMSFDJBORP06KNJR8+2fIX5bju0bRHXVlgzJfhQBS31uHzxV8JSbNKDJ26vmzU+05UmQrhz8Nn01uSFK2RAoGBAM2OCfCtj7Rj9qgMDzYwrmkinrtkHf/QhJCPrHYk5aIf2CjNSKUuteFzv8m8q+L8afzIsKncg61khnGxWexHoFU2L6v5X5usobEUxOKeVJTGiB9gqBzj/Ndo5vt6eQOW71JkBgTUXgnDEBzsVuHd1WIeBgH5axzzqAmAO7I94VQb";

    public static void main(String[] args) throws AlipayApiException {

        AlipayConfig alipayConfig = new AlipayConfig();
//设置网关地址
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
//设置应用Id
        alipayConfig.setAppId("2021002199667891");
//设置应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
//设置请求格式，固定值json
        alipayConfig.setFormat("json");
//设置字符集
        alipayConfig.setCharset("UTF-8");
//设置签名类型
        alipayConfig.setSignType("RSA2");
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map bizContent = new HashMap();
        bizContent.put("out_trade_no", "20220309155931019125400370011");
        bizContent.put("refund_amount", 0.01);
        bizContent.put("out_request_no", "HZ01RF001");

//// 返回参数选项，按需传入
//JSONArray queryOptions = new JSONArray();
//queryOptions.add("refund_detail_item_list");
//bizContent.put("query_options", queryOptions);

        request.setBizContent(JSONUtil.toJsonStr(bizContent));
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }
}