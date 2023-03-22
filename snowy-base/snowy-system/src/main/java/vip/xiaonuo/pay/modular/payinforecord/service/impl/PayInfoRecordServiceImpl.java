
/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-layui
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.pay.modular.payinforecord.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.boot.system.ApplicationHome;
import vip.xiaonuo.core.consts.CommonConstant;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.enums.CommonStatusEnum;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.GoogleGenerator;
import vip.xiaonuo.core.util.MessageCode;
import vip.xiaonuo.core.util.ResultBean;
import vip.xiaonuo.pay.modular.payappinfo.entity.PayAppInfo;
import vip.xiaonuo.pay.modular.payappinfo.service.PayAppInfoService;
import vip.xiaonuo.pay.modular.payinforecord.entity.PayInfoRecord;
import vip.xiaonuo.pay.modular.payinforecord.enums.PayInfoRecordExceptionEnum;
import vip.xiaonuo.pay.modular.payinforecord.mapper.PayInfoRecordMapper;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoParam;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoRecordParam;
import vip.xiaonuo.pay.modular.payinforecord.service.PayInfoRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.sys.core.cache.RedisCache;

import javax.annotation.Resource;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 打款记录service接口实现类
 *
 * @author abc
 * @date 2022-03-02 22:43:26
 */
@Service
public class PayInfoRecordServiceImpl extends ServiceImpl<PayInfoRecordMapper, PayInfoRecord> implements PayInfoRecordService {

    private static final Log log = Log.get();
    // 请求方式 json
    static String FORMAT = "json";
    // 编码格式，目前只支持UTF-8
    static String CHARSET = "UTF-8";
    // 签名方式
    static String SIGN_TYPE = "RSA2";
    @Resource
    PayAppInfoService payAppInfoService;

    @Resource
    RedisCache redisCache;
    @Override
    public PageResult<PayInfoRecord> page(PayInfoRecordParam payInfoRecordParam) {
        QueryWrapper<PayInfoRecord> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payInfoRecordParam)) {

            // 根据账号 查询
            if (ObjectUtil.isNotEmpty(payInfoRecordParam.getAccount())) {
                queryWrapper.lambda().like(PayInfoRecord::getAccount, payInfoRecordParam.getAccount());
            }
            // 根据姓名 查询
            if (ObjectUtil.isNotEmpty(payInfoRecordParam.getName())) {
                queryWrapper.lambda().like(PayInfoRecord::getName, payInfoRecordParam.getName());
            }
            // 根据付款状态   1 成功  2 打款中  3 失败  4 异常 查询
            if (ObjectUtil.isNotEmpty(payInfoRecordParam.getPayStatus())) {
                queryWrapper.lambda().eq(PayInfoRecord::getPayStatus, payInfoRecordParam.getPayStatus());
            }
            // 根据备注 查询
            if (ObjectUtil.isNotEmpty(payInfoRecordParam.getRemark())) {
                queryWrapper.lambda().eq(PayInfoRecord::getRemark, payInfoRecordParam.getRemark());
            }
        }
        queryWrapper.orderByDesc("create_time");
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayInfoRecord> list(PayInfoRecordParam payInfoRecordParam) {
        return this.list();
    }

    @Override
    public void add(PayInfoRecordParam payInfoRecordParam) {
        PayInfoRecord payInfoRecord = new PayInfoRecord();
        BeanUtil.copyProperties(payInfoRecordParam, payInfoRecord);
        this.save(payInfoRecord);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayInfoRecordParam> payInfoRecordParamList) {
        payInfoRecordParamList.forEach(payInfoRecordParam -> {
        PayInfoRecord payInfoRecord = this.queryPayInfoRecord(payInfoRecordParam);
            this.removeById(payInfoRecord.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayInfoRecordParam payInfoRecordParam) {
        PayInfoRecord payInfoRecord = this.queryPayInfoRecord(payInfoRecordParam);
        BeanUtil.copyProperties(payInfoRecordParam, payInfoRecord);
        this.updateById(payInfoRecord);
    }

    @Override
    public PayInfoRecord detail(PayInfoRecordParam payInfoRecordParam) {
        return this.queryPayInfoRecord(payInfoRecordParam);
    }

    @Override
    public ResultBean topay(PayInfoParam payInfoParam) {
        ResultBean rb = new ResultBean();
        try {

            String xsbox_google_code_secret = ConstantContextHolder.getSysConfigWithDefault("XSBOX_GOOGLE_CODE_SECRET",String.class,"");

            GoogleGenerator g = new GoogleGenerator ();
            long time = System.currentTimeMillis ();
            boolean result = g.check_code ( xsbox_google_code_secret,payInfoParam.getCode(),time );
            if(!result){
                return ResultBean.failedMsg("安全码不正确！");
            }
            String payUsers = payInfoParam.getPayUsers();
            if(StrUtil.isEmpty(payUsers)){
                return ResultBean.failedMsg("转账用户不能为空！");
            }
            PayAppInfo info = new PayAppInfo();
            info.setAccount(payInfoParam.getPayAccount());
            info.setAppId(payInfoParam.getAppId());
            info = payAppInfoService.getOne(new QueryWrapper<>(info));
            if (info == null) {
                throw new ServiceException(PayInfoRecordExceptionEnum.NOT_EXIST.getCode(), "付款账号信息不存在，请检查");
            }

            AlipayConfig alipayConfig = new AlipayConfig();
            //设置网关地址
            alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
            //设置应用Id
            alipayConfig.setAppId(info.getAppId());
            //设置应用私钥
            alipayConfig.setPrivateKey(info.getPrivateKey());
            ApplicationHome h = new ApplicationHome(getClass());
            File jarF = h.getSource();
            String path = jarF.getParentFile().toString() + "/upload/";
            alipayConfig.setAppCertPath(path + info.getAppPublicCertPath());
            alipayConfig.setAlipayPublicCertPath(path + info.getAlipayPublicCertPath());
            alipayConfig.setRootCertPath(path + info.getRootCertPath());
            //设置请求格式，固定值json
            alipayConfig.setFormat(FORMAT);
            //设置字符集
            alipayConfig.setCharset(CHARSET);
            //设置签名类型
            alipayConfig.setSignType(SIGN_TYPE);
            // 构造client
            DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);


            List<Map<String,Object>> users = JSONUtil.toBean(payUsers,List.class);
            for (Map<String,Object> map : users){
                AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
                AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
                Participant payerInfo = new Participant();
                String ac = map.get("value")+"";
                String name = map.get("title")+"";
                payerInfo.setIdentity(ac);
                //对方银行卡信息
    /*        BankcardExtInfo bankcardExtInfolBSeP = new BankcardExtInfo();
            bankcardExtInfolBSeP.setAccountType("1");
            bankcardExtInfolBSeP.setInstBranchName("新街口支行");
            bankcardExtInfolBSeP.setInstProvince("江苏省");
            bankcardExtInfolBSeP.setInstName("招商银行");
            bankcardExtInfolBSeP.setBankCode("123456");
            bankcardExtInfolBSeP.setInstCity("南京市");
            payerInfo.setBankcardExtInfo(bankcardExtInfolBSeP);*/
                payerInfo.setIdentityType("ALIPAY_LOGON_ID");
                //payerInfo.setExtInfo("{\"alipay_anonymous_uid\":\"2088123412341234\"}");
                payerInfo.setName(name);
                //payerInfo.setMerchantUserInfo("{\"merchant_user_id\":\"123456\"}");
                model.setPayeeInfo(payerInfo);

                //model.setBusinessParams("{\"sub_biz_scene\":\"REDPACKET\"}");
                model.setProductCode("TRANS_ACCOUNT_NO_PWD");
                //model.setOrderTitle("转账标题");
                model.setBizScene("DIRECT_TRANSFER");
                DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String formatDate = format.format(new Date()) + RandomUtil.randomString(10);
                model.setOutBizNo(formatDate);
                model.setTransAmount(ac);
                request.setBizModel(model);
                AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);

                PayInfoRecordParam payInfoRecordParam = new PayInfoRecordParam();
                Double am = Double.valueOf(payInfoParam.getAmount())*100;
                payInfoRecordParam.setAmount(am.longValue());
                payInfoRecordParam.setPayAccount(info.getAccount());
                payInfoRecordParam.setAppId(info.getAppId());
                payInfoRecordParam.setAccount(ac);
                payInfoRecordParam.setName(name);
                payInfoRecordParam.setRequest(JSONUtil.toJsonStr(request));
                payInfoRecordParam.setRespone(JSONUtil.toJsonStr(response));
                String body = response.getBody();
                log.info("certificateExecute {}"+body);
                if (response.isSuccess()) {
                    payInfoRecordParam.setPayStatus(1);
                } else {
                    payInfoRecordParam.setPayStatus(3);
                    rb.setCode(MessageCode.BIZ_ERROR.getMsgCode());
                    rb.setMsg(response.getSubMsg());
                }
                payInfoRecordParam.setRemark(response.getSubMsg());
                this.add(payInfoRecordParam);
            }
            return rb;
        }catch (Exception e){
            throw new ServiceException(PayInfoRecordExceptionEnum.NOT_EXIST.getCode(), e.getMessage());
        }
    }

    /**
     * 获取打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    private PayInfoRecord queryPayInfoRecord(PayInfoRecordParam payInfoRecordParam) {
        PayInfoRecord payInfoRecord = this.getById(payInfoRecordParam.getId());
        if (ObjectUtil.isNull(payInfoRecord)) {
            throw new ServiceException(PayInfoRecordExceptionEnum.NOT_EXIST);
        }
        return payInfoRecord;
    }
}
