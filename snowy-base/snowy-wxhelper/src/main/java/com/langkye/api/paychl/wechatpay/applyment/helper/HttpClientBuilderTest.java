package com.langkye.api.paychl.wechatpay.applyment.helper;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;

/**
 * 基于apache-httpclient封装的微信支付接口调用Demo程序，Maven依赖（https://mvnrepository.com/artifact/com.github.wechatpay-apiv3/wechatpay-apache-httpclient/0.2.1）：
 *         <dependency>
 *             <groupId>com.github.wechatpay-apiv3</groupId>
 *             <artifactId>wechatpay-apache-httpclient</artifactId>
 *             <version>0.2.1</version>
 *         </dependency>
 */
public class HttpClientBuilderTest {

  /**
   * 商户号
   */
  private static String mchId = "";

  /**
   * 商户证书序列号
   * openssl x509 -in apiclient_cert.pem -noout -serial
   */
  private static String mchSerialNo = "451AB61DE33A7BC1195B8F9B2AFF94539E533EF3"; // 商户证书序列号

  /**
   * 微信平台证书序列号
   *  openssl x509 -in wechatpay_2D13E8A8B40760C6F84E236A1AF162FAD601C3F4.pem -noout -serial
   */
  private static String wechatPaySerial = "2D13E8A8B40760C6F84E236A1AF162FAD601C3F4";

  private CloseableHttpClient httpClient;

  private static String reqdata = "{\n"
      + "    \"stock_id\": \"9433645\",\n"
      + "    \"stock_creator_mchid\": \"1900006511\",\n"
      + "    \"out_request_no\": \"20190522_001中文11\",\n"
      + "    \"appid\": \"wxab8acb865bb1637e\"\n"
      + "}";
  private static String reqdata2 = "{\n" +
          "  \"business_code\": \"1900013511_10000\",\n" +
          "  \"contact_info\": {\n" +
          "    \"contact_name\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu9Gmsu\n" +
          "\tgMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5APomty\n" +
          "\t7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\",\n" +
          "    \"contact_id_number\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu\n" +
          "\t9GmsugMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5A\n" +
          "\tPomty7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\",\n" +
          "    \"openid\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu9GmsugMIA9r\n" +
          "\t3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5APomty7/mYN\n" +
          "\txLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg== 字段加密: 使用APIv3定义的方式加密\",\n" +
          "    \"mobile_phone\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu9Gms\n" +
          "\tugMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5APo\n" +
          "\tmty7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\",\n" +
          "    \"contact_email\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu9G\n" +
          "\tmsugMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5\n" +
          "\tAPomty7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\"\n" +
          "  },\n" +
          "  \"subject_info\": {\n" +
          "    \"subject_type\": \"SUBJECT_TYPE_ENTERPRISE\",\n" +
          "    \"business_license_info\": {\n" +
          "      \"license_copy\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "      \"license_number\": \"123456789012345678\",\n" +
          "      \"merchant_name\": \"腾讯科技有限公司\",\n" +
          "      \"legal_person\": \"张三\"\n" +
          "    },\n" +
          "    \"certificate_info\": {\n" +
          "      \"cert_copy\": \"0P3ng6KTIW4-Q_l2FjKLZuhHjBWoMAjmVtCz7ScmhEIThCaV-4BBgVwtNkCHO_XXqK5dE5YdOmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo\",\n" +
          "      \"cert_type\": \"CERTIFICATE_TYPE_2388\",\n" +
          "      \"cert_number\": \"111111111111\",\n" +
          "      \"merchant_name\": \"xx公益团体\",\n" +
          "      \"company_address\": \"广东省深圳市南山区xx路xx号\",\n" +
          "      \"legal_person\": \"李四\",\n" +
          "      \"period_begin\": \"2019-08-01\",\n" +
          "      \"period_end\": \"2029-08-01\"\n" +
          "    },\n" +
          "    \"organization_info\": {\n" +
          "      \"organization_copy\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "      \"organization_code\": \"123456789-A\",\n" +
          "      \"org_period_begin\": \"2019-08-01\",\n" +
          "      \"org_period_end\": \"2029-08-01\"\n" +
          "    },\n" +
          "    \"certificate_letter_copy\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "    \"identity_info\": {\n" +
          "      \"id_doc_type\": \"IDENTIFICATION_TYPE_IDCARD\",\n" +
          "      \"id_card_info\": {\n" +
          "        \"id_card_copy\": \"jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ\",\n" +
          "        \"id_card_national\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "        \"id_card_name\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTEu9G\n" +
          "\t\tmsugMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriudjD5\n" +
          "\t\tAPomty7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\",\n" +
          "        \"id_card_number\": \"AOZdYGISxo4y44/UgZ69bdu9X+tfMUJ9dl+LetjM45/zMbrYu+wWZ8gn4CTdo+D/m9MrPg+V4sm73oxqdQu/hj7aWyDl4GQtPXVdaztB9jVb\n" +
          "\t\tVZh3QFzV+BEmytMNQp9dt1uWJktlfdDdLR3AMWyMB377xd+m9bSr/ioDTzagEcGe+vLYiKrzcroQv3OR0p3ppFYoQ3IfYeU/04S4t9rNFL+kyblK2FCCqQ11NdbbHoC\n" +
          "\t\trJc7NV4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==\",\n" +
          "        \"card_period_begin\": \"2026-06-06\",\n" +
          "        \"card_period_end\": \"2026-06-06\"\n" +
          "      },\n" +
          "      \"id_doc_info\": {\n" +
          "        \"id_doc_copy\": \"jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ\",\n" +
          "        \"id_doc_name\": \"pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPROvwia4ibL+F6mfjbzQIzfb3HHLEjZ4YiR/cJiCrZxnAqi+pjeKIEdkwzXRAI7FUhrfPK3SNjaBTE\n" +
          "\t\tu9GmsugMIA9r3x887Q+ODuC8HH2nzAn7NGpE/e3yiHgWhk0ps5k5DP/2qIdGdONoDzZelrxCl/NWWNUyB93K9F+jC1JX2IMttdY+aQ6zBlw0xnOiNW6Hzy7UtC+\n" +
          "\t\txriudjD5APomty7/mYNxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==\",\n" +
          "        \"id_doc_number\": \"AOZdYGISxo4y44/UgZ69bdu9X+tfMUJ9dl+LetjM45/zMbrYu+wWZ8gn4CTdo+D/m9MrPg+V4sm73oxqdQu/hj7aWyDl4GQtPXVdaztB9j\n" +
          "\t\tVbVZh3QFzV+BEmytMNQp9dt1uWJktlfdDdLR3AMWyMB377xd+m9bSr/ioDTzagEcGe+vLYiKrzcroQv3OR0p3ppFYoQ3IfYeU/04S4t9rNFL+kyblK2FCCqQ11Nd\n" +
          "\t\tbbHoCrJc7NV4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==\",\n" +
          "        \"doc_period_begin\": \"2019-06-06\",\n" +
          "        \"doc_period_end\": \"2026-06-06\"\n" +
          "      },\n" +
          "      \"owner\": true\n" +
          "    },\n" +
          "    \"ubo_info\": {\n" +
          "      \"id_type\": \"IDENTIFICATION_TYPE_IDCARD\",\n" +
          "      \"id_card_copy\": \"jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ\",\n" +
          "      \"id_card_national\": \"jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ\",\n" +
          "      \"id_doc_copy\": \"jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ\",\n" +
          "      \"name\": \"AOZdYGISxo4y44/Ug4P4TG5xzchG/5IL9DBd+Z0zZXkw==\",\n" +
          "      \"id_number\": \"AOZdYGISxo4y44/UgZ69bdu9X+tfMUJ9dl+LetjM45/zMbrYu+wWZ8gn4CTdo+D/m9MrPg+V4sm73oxqdQu/hj7aWyDl4GQtPXVdaztB9jVbVZh3\n" +
          "\t  QFzV+BEmytMNQp9dt1uWJktlfdDdLR3AMWyMB377xd+m9bSr/ioDTzagEcGe+vLYiKrzcroQv3OR0p3ppFYoQ3IfYeU/04S4t9rNFL+kyblK2FCCqQ11NdbbHoCrJc\n" +
          "\t  7NV4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==\",\n" +
          "      \"id_period_begin\": \"2019-06-06\",\n" +
          "      \"id_period_end\": \"2026-06-06\"\n" +
          "    }\n" +
          "  },\n" +
          "  \"business_info\": {\n" +
          "    \"merchant_shortname\": \"张三餐饮店\",\n" +
          "    \"service_phone\": \"0758XXXXX\",\n" +
          "    \"sales_info\": {\n" +
          "      \"sales_scenes_type\": [\n" +
          "        \"SALES_SCENES_STORE\"\n" +
          "      ],\n" +
          "      \"biz_store_info\": {\n" +
          "        \"biz_store_name\": \"大郎烧饼\",\n" +
          "        \"biz_address_code\": \"440305\",\n" +
          "        \"biz_store_address\": \"南山区xx大厦x层xxxx室\",\n" +
          "        \"store_entrance_pic\": [\n" +
          "          \"0P3ng6KTIW4-Q_l2FjKLZuhHjBWoMAjmVtCz7ScmhEIThCaV-4BBgVwtNkCHO_XXqK5dE5YdOmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo\"\n" +
          "        ],\n" +
          "        \"indoor_pic\": [\n" +
          "          \"0P3ng6KTIW4-Q_l2FjKLZuhHjBWoMAjmVtCz7ScmhEIThCaV-4BBgVwtNkCHO_XXqK5dE5YdOmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo\"\n" +
          "        ],\n" +
          "        \"biz_sub_appid\": \"wx1234567890123456\"\n" +
          "      },\n" +
          "      \"mp_info\": {\n" +
          "        \"mp_appid\": \"wx1234567890123456\",\n" +
          "        \"mp_sub_appid\": \"wx1234567890123456\",\n" +
          "        \"mp_pics\": [\n" +
          "          \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "        ]\n" +
          "      },\n" +
          "      \"mini_program_info\": {\n" +
          "        \"mini_program_appid\": \"wx1234567890123456\",\n" +
          "        \"mini_program_sub_appid\": \"wx1234567890123456\",\n" +
          "        \"mini_program_pics\": [\n" +
          "          \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "        ]\n" +
          "      },\n" +
          "      \"app_info\": {\n" +
          "        \"app_appid\": \"wx1234567890123456\",\n" +
          "        \"app_sub_appid\": \"wx1234567890123456\",\n" +
          "        \"app_pics\": [\n" +
          "          \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "        ]\n" +
          "      },\n" +
          "      \"web_info\": {\n" +
          "        \"domain\": \"http://www.qq.com\",\n" +
          "        \"web_authorisation\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "        \"web_appid\": \"wx1234567890123456\"\n" +
          "      },\n" +
          "      \"wework_info\": {\n" +
          "        \"corp_id\": \"wx1234567890123456\",\n" +
          "        \"sub_corp_id\": \"wx1234567890123456\",\n" +
          "        \"wework_pics\": [\n" +
          "          \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "        ]\n" +
          "      }\n" +
          "    }\n" +
          "  },\n" +
          "  \"settlement_info\": {\n" +
          "    \"settlement_id\": \"719\",\n" +
          "    \"qualification_type\": \"餐饮\",\n" +
          "    \"qualifications\": [\n" +
          "      \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "    ],\n" +
          "    \"activities_id\": \"20191030111cff5b5e\",\n" +
          "    \"activities_rate\": \"0.6\",\n" +
          "    \"activities_additions\": [\n" +
          "      \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "    ]\n" +
          "  },\n" +
          "  \"bank_account_info\": {\n" +
          "    \"bank_account_type\": \"BANK_ACCOUNT_TYPE_CORPORATE\",\n" +
          "    \"account_name\": \"AOZdYGISxo4y44/UgZ69bdu9X+tfMUJ9dl+LetjM45/zMbrYu+wWZ8gn4CTdo+D/m9MrPg+V4sm73oxqdQu/hj7aWyDl4GQtPXVdaztB9jV\n" +
          "\tbVZh3QFzV+BEmytMNQp9dt1uWJktlfdDdLR3AMWyMB377xd+m9bSr/ioDTzagEcGe+vLYiKrzcroQv3OR0p3ppFYoQ3IfYeU/04S4t9rNFL+kyblK2FCCqQ11Ndb\n" +
          "\tbHoCrJc7NV4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==\",\n" +
          "    \"account_bank\": \"工商银行\",\n" +
          "    \"bank_address_code\": \"110000\",\n" +
          "    \"bank_branch_id\": \"402713354941\",\n" +
          "    \"bank_name\": \"施秉县农村信用合作联社城关信用社\",\n" +
          "    \"account_number\": \"d+xT+MQCvrLHUVDWv/8MR/dB7TkXM2YYZlokmXzFsWs35NXUot7C0NcxIrUF5FnxqCJHkNgKtxa6RxEYyba1+VBRLnqKG2fSy/Y5qDN08\n" +
          "\tEj9zHCwJjq52Wg1VG8MRugli9YMI1fI83KGBxhuXyemgS/hqFKsfYGiOkJqjTUpgY5VqjtL2N4l4z11T0ECB/aSyVXUysOFGLVfSrUxMPZy6jWWYGvT1+4P633f+\n" +
          "\tR+ki1gT4WF/2KxZOYmli385ZgVhcR30mr4/G3HBcxi13zp7FnEeOsLlvBmI1PHN4C7Rsu3WL8sPndjXTd75kPkyjqnoMRrEEaYQE8ZRGYoeorwC+w==\"\n" +
          "  },\n" +
          "  \"addition_info\": {\n" +
          "    \"legal_person_commitment\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "    \"legal_person_video\": \"47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4\",\n" +
          "    \"business_addition_pics\": [\n" +
          "      \"ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBD\"\n" +
          "    ],\n" +
          "    \"business_addition_msg\": \"特殊情况，说明原因\"\n" +
          "  }" +
          "}";

  // 你的商户私钥 1606010980_20210223_cert/apiclient_key.pem
  private static String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
          "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCygWjLFHhs1Nb1\n" +
          "UeAlJyAYG6U1CmjW5m99EJav1j7g1XlUwUZ5Pf7daReuvD9DLXaIfSqjInghrFVd\n" +
          "Js+SJcPkrFsqF5Rtqn5za2BF0I9JjcJU+Y52aymdgmczUSeb6ChpXKpHUwhMNHI6\n" +
          "SN/3rZC/SA1YaPQYabX0aTWrr0j8z/+SVn/lMmHo2bpftOHa9kbROBUL62VW+gbL\n" +
          "ZVO06zXz5dIaN6fWhQboQNfRucIOQFLaocEA+/nndjesFy79QvacSx9n98BIbNxW\n" +
          "LEEPjkopjsv2eXfNrdGCCPkqm1wdDasT2eoCphweiHkz7MwzvFaLc+CE6pARtGGx\n" +
          "BCSfLKdhAgMBAAECggEAew2OqgAwjAWlMXQBqlrwWu4j0w6b+xjbXkGjPw/YRLGk\n" +
          "dHLQHCBKo2Mg+km26p5aifiO/epzGfNs3Uw15y+1fNv79GZQ4dCYXwR0V2xQeooI\n" +
          "nv9x39w70eLlNZEKVwWRZiCFGan5nF7DJ7649kpl8BbBbGLdcj7dOeGVKAcrr2qk\n" +
          "jE5wCkXDKei/Q3TVBMjgjTWgQlRnIkrsJnbTvF4o+as1uTZBVZBfc6KrntCi4L/2\n" +
          "L/ryKerFxHwMoKA3N9oofM6Gttjx5RZb2DfHY250hAiklYDpsZNX2PqayMxLZx78\n" +
          "7H/+aAHCbHRHINXki7ykWWrmL0mgE2l+tavERxoHgQKBgQDcXVZXSmtookTXoKWu\n" +
          "P77SO5AbUJjSw+R/Vpd/cMZqHqpw4lFvQuA9Shr6NcB0BzlF6M942HEZNy0a1qJ0\n" +
          "aJEOGP6gBDKlglejE6whE+4l0ZJAna9fPEivljs8sPt+NpCXg731I4WzAyU4rU1T\n" +
          "drMNZ7twmEjmZU2p9lQrbgnauQKBgQDPXzAG60a7t4yG1b9V6c+sCUYeVmBPpkgj\n" +
          "utMkp8W0haO8rEE7mokp1Z88BAJFWZJt4CtqKeWq8M4cHbLhLjQK5yXPRJVmZfph\n" +
          "yHsSmmmS+SJtR5mm8830SBAJCzxFjghelCCQXBJJiyNQXO8/bgcNeKGUtcUPJ3Tz\n" +
          "gQLlmoq96QKBgEyPPP0MxO5QWhyUGpvEV9aQjQuAX+fx2QQIoI/NLZIW315isqo2\n" +
          "ENQaV+8QF7H4BITuUo6InBqHmesWINDSt3qvYl9TtgjclPf6ULYZR4bqHabHYfNz\n" +
          "bUlbp275ihaafHe3dJqjYcME17yuMQxLU//XRz7iGYm6LlZbV3jTPSc5AoGAQ189\n" +
          "j0HpHRupADV5kEQCQHZS3ZBaggYX8ePErWgEh33TkHy2RNMUNQ8+SJVU14seU6w8\n" +
          "7qTmBP9vNxs+/oGwTsqCMjCqCTagfhxQP98C5ckjMjXM6bzT8MVo0EdiMYbPmUqu\n" +
          "ubfwCQy3UGihIg5xJCNI+7OCbkX3bRVBB6+J2+ECgYEAlQu+WjfDEwCnkiVKEu2m\n" +
          "1g6Z3ey2VABfGh/dg2KYhPcZfrcNFOmXjH3+JCNQmmepXcDWHUo3b2d9b1MzDL9P\n" +
          "I8OdLBayuWoaG+9aFCpkaQbfrutYgJQzBlk0LlXsbWWP8sifjjzu/McclLQehXnP\n" +
          "zWhXBnB9hfQMnFd4gaJVSJQ="+
          "-----END PRIVATE KEY-----\n";

  // 你的微信支付平台证书 微信商户证书/平台证书/wechatpay_2D13E8A8B40760C6F84E236A1AF162FAD601C3F4.pem
  private static String certificate =  "-----BEGIN CERTIFICATE-----\n" +
          "" +
          "-----END CERTIFICATE-----";

  public void setup() throws IOException  {
    PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
        new ByteArrayInputStream(privateKey.getBytes("utf-8")));
    X509Certificate wechatpayCertificate = PemUtil.loadCertificate(
        new ByteArrayInputStream(certificate.getBytes("utf-8")));

    ArrayList<X509Certificate> listCertificates = new ArrayList<>();
    listCertificates.add(wechatpayCertificate);

    httpClient = WechatPayHttpClientBuilder.create()
        .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
        .withWechatpay(listCertificates)
        .build();
  }

  public void after() throws IOException {
    httpClient.close();
  }

  public void getCertificateTest() throws Exception {


    try {
      // 加载商户私钥（privateKey：私钥字符串）
      PrivateKey merchantPrivateKey = PemUtil
              .loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

      // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
      AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
              new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),"7f8ddadbe4e91911a58ed525b3510749".getBytes("utf-8"));

      // 初始化httpClient
      httpClient = WechatPayHttpClientBuilder.create()
              .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
              .withValidator(new WechatPay2Validator(verifier)).build();
    } finally {
    }
  }

  public void getCertificatesWithoutCertTest() throws Exception {
    PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
        new ByteArrayInputStream(privateKey.getBytes("utf-8")));

    httpClient = WechatPayHttpClientBuilder.create()
        .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
        .withValidator(response -> true)
        .build();

    getCertificateTest();
  }

  public void postNonRepeatableEntityTest() throws IOException {
    HttpPost httpPost = new HttpPost(
        "https://api.mch.weixin.qq.com/v3/marketing/favor/users/oHkLxt_htg84TUEbzvlMwQzVDBqo/coupons");


    InputStream stream = new ByteArrayInputStream(reqdata.getBytes("utf-8"));
    InputStreamEntity reqEntity = new InputStreamEntity(stream);
    reqEntity.setContentType("application/json");
    httpPost.setEntity(reqEntity);
    httpPost.addHeader("Accept", "application/json");

    CloseableHttpResponse response = httpClient.execute(httpPost);
    //assertTrue(response.getStatusLine().getStatusCode() != 401);
    System.out.println(response.getStatusLine().getStatusCode());
    try {
      HttpEntity entity2 = response.getEntity();
      // do something useful with the response body
      // and ensure it is fully consumed
      EntityUtils.consume(entity2);
    } finally {
      response.close();
    }
  }

  public void postRepeatableEntityTest() throws IOException {
    HttpPost httpPost = new HttpPost(
        "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/");
        //  "https://api.mch.weixin.qq.com/v3/marketing/favor/users/oHkLxt_htg84TUEbzvlMwQzVDBqo/coupons");

    // NOTE: 建议指定charset=utf-8。低于4.4.6版本的HttpCore，不能正确的设置字符集，可能导致签名错误
    StringEntity reqEntity = new StringEntity(
        reqdata2, ContentType.create("application/json", "utf-8"));
    httpPost.setEntity(reqEntity);
    httpPost.addHeader("Accept", "application/json");
    httpPost.addHeader("Wechatpay-Serial", "2D13E8A8B40760C6F84E236A1AF162FAD601C3F4");

    CloseableHttpResponse response = httpClient.execute(httpPost);
    //assertTrue(response.getStatusLine().getStatusCode() != 401);
    System.out.println(response.getStatusLine().getStatusCode());
    System.out.println();
    HttpEntity entity = response.getEntity();
    String responseBody = EntityUtils.toString(entity);
    System.out.println(responseBody);
    try {
      HttpEntity entity2 = response.getEntity();
      // do something useful with the response body
      // and ensure it is fully consumed
      EntityUtils.consume(entity2);
    } finally {
      response.close();
    }
  }

  public static String rsaEncryptOAEP(String message)
          throws Exception {
    X509Certificate wechatpayCertificate = PemUtil.loadCertificate(
            new ByteArrayInputStream(certificate.getBytes("utf-8")));
    try {
      Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
      cipher.init(Cipher.ENCRYPT_MODE, wechatpayCertificate.getPublicKey());

      byte[] data = message.getBytes("utf-8");
      byte[] cipherdata = cipher.doFinal(data);
      return Base64.getEncoder().encodeToString(cipherdata);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
      throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
    } catch (InvalidKeyException e) {
      throw new IllegalArgumentException("无效的证书", e);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
      throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
    }
  }
  public static void main(String[] args) {
    try {
      HttpClientBuilderTest httpClientBuilderTest = new HttpClientBuilderTest();
      httpClientBuilderTest.setup();
      httpClientBuilderTest.postRepeatableEntityTest();
      httpClientBuilderTest.after();
      //System.out.println(rsaEncryptOAEP("123"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}