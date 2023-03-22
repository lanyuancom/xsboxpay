
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
package vip.xiaonuo.pay.modular.payorder.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.exception.enums.ServerExceptionEnum;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.*;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchapp.service.MchAppService;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;
import vip.xiaonuo.pay.modular.ordernotifyinfo.enums.OrderNotifyInfoExceptionEnum;
import vip.xiaonuo.pay.modular.payorder.entity.PayOrder;
import vip.xiaonuo.pay.modular.payorder.enums.PayOrderExceptionEnum;
import vip.xiaonuo.pay.modular.payorder.mapper.PayOrderMapper;
import vip.xiaonuo.pay.modular.payorder.param.PayOrderParam;
import vip.xiaonuo.pay.modular.payorder.param.PayTestParam;
import vip.xiaonuo.pay.modular.payorder.service.PayOrderService;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支付订单表service接口实现类
 *
 * @author abc
 * @date 2022-01-04 20:17:35
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    private static final Log log = Log.get();
    @Resource
    MchAppService mchAppService;
    @Resource
    MchInfoService mchInfoService;
    @Override
    public PageResult<PayOrder> page(PayOrderParam payOrderParam) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payOrderParam)) {

            // 根据支付订单号 查询
            if (ObjectUtil.isNotEmpty(payOrderParam.getPayOrderId())) {
                queryWrapper.lambda().like(PayOrder::getPayOrderId, payOrderParam.getPayOrderId());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(payOrderParam.getMchNo())) {
                queryWrapper.lambda().like(PayOrder::getMchNo, payOrderParam.getMchNo());
            }
            // 根据商户订单号 查询
            if (ObjectUtil.isNotEmpty(payOrderParam.getMchOrderNo())) {
                queryWrapper.lambda().like(PayOrder::getMchOrderNo, payOrderParam.getMchOrderNo());
            }
            // 根据三位货币代码,人民币:cny 查询
            if (ObjectUtil.isNotEmpty(payOrderParam.getCurrency())) {
                queryWrapper.lambda().eq(PayOrder::getCurrency, payOrderParam.getCurrency());
            }
            // 根据支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-退款中, 6-已退款, 7-退款失败, 8-订单关闭 查询
            if (ObjectUtil.isNotEmpty(payOrderParam.getState())) {
                queryWrapper.lambda().eq(PayOrder::getState, payOrderParam.getState());
            }
        }

        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                queryWrapper.lambda().eq(PayOrder::getMchNo, mchInfo.getMchNo());
            }
        }
        queryWrapper.orderByDesc("create_time");
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayOrder> list(PayOrderParam payOrderParam) {
        return this.list();
    }

    @Override
    public void add(PayOrderParam payOrderParam) {
        PayOrder payOrder = new PayOrder();
        BeanUtil.copyProperties(payOrderParam, payOrder);
        this.save(payOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayOrderParam> payOrderParamList) {
        payOrderParamList.forEach(payOrderParam -> {
        PayOrder payOrder = this.queryPayOrder(payOrderParam);
            this.removeById(payOrder.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayOrderParam payOrderParam) {
        PayOrder payOrder = this.queryPayOrder(payOrderParam);
        BeanUtil.copyProperties(payOrderParam, payOrder);
        this.updateById(payOrder);
    }

    @Override
    public PayOrder detail(PayOrderParam payOrderParam) {
        return this.queryPayOrder(payOrderParam);
    }

    @Override
    public ResultBean paytest(PayTestParam param) {
        ResultBean rb = new ResultBean();
        if (StrUtil.isNotBlank(param.getAppId())){
            MchApp mchApp = new MchApp();
            mchApp.setAppId(param.getAppId());
            mchApp = mchAppService.getOne(new QueryWrapper<>(mchApp));
            if(mchApp == null){
                throw  new ServiceException(ServerExceptionEnum.CONSTANT_EMPTY.getCode(),"应用不存在，请刷新页面重试！");
            }

            Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
            if(!superAdmin) {
                MchInfo mchInfo = new MchInfo();
                mchInfo.setMchNo(mchApp.getMchNo());
                mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
                mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
                if (mchInfo == null) {
                    throw  new ServiceException(ServerExceptionEnum.CONSTANT_EMPTY.getCode(),"应用不属于当前登录商户，请重新选择");
                }
            }

            String url = ConstantContextHolder.getSysConfigWithDefault("XSBOX_PAY_URL",String.class,"");
            Map<String,Object> paramMap = JSONUtil.toBean(JSONUtil.toJsonStr(mchApp),Map.class);
            paramMap.put("amount", AmountUtil.convertDollar2Cent(param.getAmount().toString()));
            paramMap.put("subject",param.getSubject());
            paramMap.put("body",param.getSubject());
            paramMap.put("payWay",param.getPayWay());
            paramMap.put("payType",param.getPayType());
            paramMap.put("mchOrderNo",param.getMchOrderNo());
            paramMap.put("notifyUrl","");
            paramMap.put("returnUrl","");
            paramMap.put("clientIp",IpAddressUtil.getIp(HttpServletUtil.getRequest()));
            try {
                HttpRequest req = HttpRequest.post(url+"/xsbox-pay/api/pay/subOrder")
                        .form(paramMap);//表单内容
                String result = req.timeout(50000)//超时，毫秒
                        .execute().body();
                log.info(" HttpRequest "+req.getUrl());
                log.info(" result "+result);
                rb = JSONUtil.toBean(result,ResultBean.class);
                if(rb.getCode() != 200){
                    throw new ServiceException(ServerExceptionEnum.SERVER_ERROR.getCode(),rb.getMsg());
                }
            }catch (Exception e){
                throw new ServiceException(ServerExceptionEnum.SERVER_ERROR.getCode(),e.getMessage());
            }

        }else{
            throw new ServiceException(ServerExceptionEnum.SERVER_ERROR.getCode(),"应用不能为空");
        }
        return rb;
    }

    @Override
    public Map<String,Object> totalPay(String createTime) {
        String yesterday = null;
        String today = null;
        if(StrUtil.isBlank(createTime)){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            today = format.format(new Date());
            DateTime start = DateUtil.offsetDay(new Date(),-1);
            yesterday = format.format(start);
        }

        List<Map<String,Object>> maps = this.baseMapper.totalDayGe(createTime,today,yesterday);
        List<Map<String,Object>> channels = new ArrayList<>();
        for (Map<String, Object> channel : maps) {
            channels.add(new HashMap<>(channel));
        }
        channels=channels.stream()
                // 表示id为key， 接着如果有重复的，那么从Invoice对象o1与o2中筛选出一个，这里选择o1，
                // 并把id重复，需要将nums和sums与o1进行合并的o2, 赋值给o1，最后返回o1
                .collect(Collectors.toMap(p -> p.get("name")+"", a -> a, (o1, o2) -> {
                    Double sum = NumberUtil.add(Double.valueOf(o1.get("success_amount")+""),Double.valueOf(o2.get("success_amount")+""));
                    o1.put("success_amount",sum);
                    sum = NumberUtil.add(Double.valueOf(o1.get("today_succ_amount")+""),Double.valueOf(o2.get("today_succ_amount")+""));
                    o1.put("today_succ_amount",sum);
                    sum = NumberUtil.add(Double.valueOf(o1.get("yesterday_succ_amount")+""),Double.valueOf(o2.get("yesterday_succ_amount")+""));
                    o1.put("yesterday_succ_amount",sum);
                    return o1;
                })).values().stream().collect(Collectors.toList());
        Map<String,Object> totalPayMap = new HashMap<>();
        totalPayMap.put("channel",channels);

        Collection<Map<String,Object>> CollectionMchs = maps.stream()
                // 表示id为key， 接着如果有重复的，那么从Invoice对象o1与o2中筛选出一个，这里选择o1，
                // 并把id重复，需要将nums和sums与o1进行合并的o2, 赋值给o1，最后返回o1
                .collect(Collectors.toMap(p -> p.get("mch_no")+"", a -> a, (o1, o2) -> {
                    Double sum = NumberUtil.add(Double.valueOf(o1.get("success_amount")+""),Double.valueOf(o2.get("success_amount")+""));
                    o1.put("success_amount",sum);
                    sum = NumberUtil.add(Double.valueOf(o1.get("today_succ_amount")+""),Double.valueOf(o2.get("today_succ_amount")+""));
                    o1.put("today_succ_amount",sum);
                    sum = NumberUtil.add(Double.valueOf(o1.get("yesterday_succ_amount")+""),Double.valueOf(o2.get("yesterday_succ_amount")+""));
                    o1.put("yesterday_succ_amount",sum);
                    return o1;
                })).values();
        totalPayMap.put("mchs",CollectionMchs);
        return totalPayMap;
    }

    @Override
    public Map<String,Object> dayTotal(PayOrderParam payOrderParam){
        String day = payOrderParam.getSTime();
        String yesday = "";
        if(StrUtil.isBlank(day)){
            Date date=new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            day=(format.format(date));
            date=DateUtil.offsetDay(date,-1);
            format = new SimpleDateFormat("yyyy-MM-dd");
            yesday=(format.format(date));
        }else{
            day = day+" 00:00:01";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date=format.parse(day);
                date=DateUtil.offsetDay(date,-1);
                format = new SimpleDateFormat("yyyy-MM-dd");
                yesday=(format.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper();
        String selectSql = "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' THEN 1 END ) AS day_count," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND state = 2 THEN 1 END ) AS day_succ_count," +
                "ROUND( IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' THEN amount END )/ 100, 0 ), 2 ) AS day_count_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND state = 2 THEN amount END ), 0 )/ 100," +
                "2 " +
                ") day_succ_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND division_state = 3 THEN division_amount END ), 0 )/ 100," +
                "2 " +
                ") day_succ_division_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND division_state != 3 THEN division_amount END ), 0 )/ 100," +
                "2 " +
                ") day_err_division_amount," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND division_state = 3 THEN 1 END ) day_succ_division," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+day+"' AND division_state != 3 THEN 1 END ) day_err_division," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' THEN 1 END ) AS yesday_count," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND state = 2 THEN 1 END ) AS yesday_succ_count," +
                "ROUND( IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' THEN amount END )/ 100, 0 ), 2 ) AS yesday_count_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND state = 2 THEN amount END ), 0 )/ 100," +
                "2 " +
                ") yesday_succ_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND division_state = 3 THEN division_amount END ), 0 )/ 100," +
                "2 " +
                ") yesday_succ_division_amount," +
                "ROUND(" +
                "IFNULL( SUM( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND division_state != 3 THEN division_amount END ), 0 )/ 100," +
                "2 " +
                ") yesday_err_division_amount," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND division_state = 3 THEN 1 END ) yesday_succ_division," +
                "COUNT( CASE WHEN DATE_FORMAT( create_time, '%Y-%m-%d' ) = '"+yesday+"' AND division_state != 3 THEN 1 END ) yesday_err_division," +
                "COUNT( 1 ) AS count," +
                "IFNULL( ROUND(( COUNT( CASE WHEN state = 2 THEN 1 END )/ COUNT( 1 ))* 100, 2 ), 0 ) rate," +
                "ROUND( SUM( amount )/ 100, 2 ) AS count_amount," +
                "ROUND( IFNULL( SUM( CASE WHEN state = 2 THEN amount END ), 0 )/ 100, 2 ) succ_amount," +
                "ROUND( IFNULL( SUM( CASE WHEN state != 2 THEN amount END ), 0 )/ 100, 2 ) err_amount," +
                "ROUND( IFNULL( SUM( CASE WHEN division_state = 3 THEN division_amount END ), 0 )/ 100, 2 ) succ_division_amount," +
                "ROUND( IFNULL( SUM( CASE WHEN division_state != 3 THEN division_amount END ), 0 )/ 100, 2 ) err_division_amount," +
                "COUNT( CASE WHEN division_state = 3 THEN 1 END ) succ_division," +
                "COUNT( CASE WHEN division_state != 3 THEN 1 END ) err_division," +
                "COUNT( CASE WHEN state = 2 THEN 1 END ) succ," +
                "COUNT( CASE WHEN state != 2 THEN 1 END ) err ";
        queryWrapper.select(selectSql);
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            queryWrapper.eq("mch_no",mchInfo.getMchNo());
        }
        return this.getMap(queryWrapper);
    }

    @Override
    public Map<String,Object> minuteRate(PayOrderParam payOrderParam) {
        QueryWrapper queryWrapper = new QueryWrapper();
        String sql ="COUNT(1) AS count," +
                "COUNT(CASE WHEN state = 2 THEN amount END ) succ," +
                "COUNT(CASE WHEN state != 2 THEN amount END ) err," +
                "IFNULL(ROUND((COUNT(CASE WHEN state = 2 THEN amount END )/COUNT(1))*100,2),0) rate";
        queryWrapper.select(sql);

        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            queryWrapper.eq("mch_no",mchInfo.getMchNo());
        }

        queryWrapper.ge("UNIX_TIMESTAMP(create_time)",(System.currentTimeMillis()/1000)-60);
        return this.getMap(queryWrapper);
    }


    @Override
    public List<Map<String,Object>> minute(PayOrderParam payOrderParam,Integer tag) {
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:01");
        String tagStr = tag == null || tag == 0 ? "%Y-%m-%d %H:%i 分":tag == 1 ?"%Y-%m-%d %H 时":tag == 2 ? "%Y-%m-%d":"参数错误"; //默认是分钟统计
        QueryWrapper queryWrapper = new QueryWrapper();
        String sql = "DATE_FORMAT(create_time, \""+tagStr+"\") create_time," +
                "DATE_FORMAT(create_time, \""+tagStr+"\") minute_time," +
                "COUNT(1) AS count," +
                "ROUND(SUM(amount)/100,2) AS count_amount," +
                "ROUND(IFNULL(SUM(CASE WHEN state = 2 THEN amount END ),0)/100,2) succ_amount ," +
                "ROUND(IFNULL(SUM(CASE WHEN state != 2 THEN amount END ),0)/100,2) err_amount ," +
                "ROUND(IFNULL(SUM(CASE WHEN division_state = 3 THEN division_amount END ),0)/100,2) division_amount_succ ," +
                "ROUND(IFNULL(SUM(CASE WHEN division_state != 3 THEN division_amount END ),0)/100,2) division_amount_err ," +
                "COUNT(CASE WHEN division_state = 3 THEN 1 END ) division_succ ," +
                "COUNT(CASE WHEN division_state != 3 THEN 1 END ) division_err ," +
                "COUNT(CASE WHEN state = 2 THEN 1 END ) succ ," +
                "COUNT(CASE WHEN state != 2 THEN 1 END ) err";
        queryWrapper.select(sql);
        queryWrapper.groupBy("DATE_FORMAT(create_time, \""+tagStr+"\")");

        if(StrUtil.isBlank(payOrderParam.getSTime())){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            payOrderParam.setSTime(format.format(new Date())+" 00:00:01");
        }else{
            payOrderParam.setSTime(payOrderParam.getSTime()+" 00:00:01");
        }
        if(StrUtil.isBlank(payOrderParam.getETime())){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            payOrderParam.setETime(format.format(new Date())+" 23:59:59");
        }else{
            payOrderParam.setETime(payOrderParam.getETime()+" 23:59:59");
        }
        queryWrapper.between("create_time",payOrderParam.getSTime(),payOrderParam.getETime());
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            queryWrapper.eq("mch_no",mchInfo.getMchNo());
        }
        List<Map<String,Object>> maps = this.listMaps(queryWrapper);
        return maps;
    }

    @Override
    public void refund(List<PayOrderParam> payOrderParamList) {
        String url = ConstantContextHolder.getSysConfigWithDefault("XSBOX_PAY_URL",String.class,"");
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("orderIds",payOrderParamList.stream().map(PayOrderParam::getPayOrderId).collect(Collectors.joining(",")));
        HttpRequest req = HttpRequest.post(url+"/xsbox-pay/api/pay/refundOrder")
                .form(paramMap);//表单内容
        String result = req.timeout(50000)//超时，毫秒
                .execute().body();
        log.info(" HttpRequest "+req.getUrl());
        log.info(" result "+result);
        ResultBean rb = JSONUtil.toBean(result,ResultBean.class);
        if(rb.getCode() != 200){
            throw new ServiceException(ServerExceptionEnum.SERVER_ERROR.getCode(),rb.getMsg());
        }
    }

    /**
     * 获取支付订单表
     *
     * @author abc
     * @date 2022-01-04 20:17:35
     */
    private PayOrder queryPayOrder(PayOrderParam payOrderParam) {
        PayOrder payOrder = this.getById(payOrderParam.getId());
        if (ObjectUtil.isNull(payOrder)) {
            throw new ServiceException(PayOrderExceptionEnum.NOT_EXIST);
        }
        return payOrder;
    }

    @Override
    public void supplement(PayOrderParam payOrderParam) {
        PayOrder info = this.getById(payOrderParam.getId());
        try {

            HttpResponse responseBody = HttpRequest.get(info.getNotifyUrl())
                    .charset(Charset.forName("UTF-8"))
                    .timeout(6000)//超时，毫秒
                    .execute();
            String body = responseBody.body();
            info.setNotifyMsg(responseBody.getStatus() + "");
            if ("SUCCESS".equals(responseBody)) {
                info.setNotifyState(1);
            } else {
                throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST.getCode(),"通知失败，请求返回状态 "+responseBody.getStatus());
            }
        }catch (Exception e){
            throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST.getCode(),e.getMessage());
        }
        LambdaUpdateWrapper<PayOrder> oq = new LambdaUpdateWrapper<>();
        oq.set(PayOrder::getNotifyState,1);
        oq.set(PayOrder::getNotifyMsg,info.getNotifyMsg());
        oq.ne(PayOrder::getNotifyState,1);
        oq.eq(PayOrder::getPayOrderId,info.getPayOrderId());
        this.update(oq);
    }
}
