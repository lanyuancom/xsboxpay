
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
package vip.xiaonuo.pay.modular.ordernotifyinfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.core.consts.CommonConstant;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.enums.CommonStatusEnum;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;
import vip.xiaonuo.pay.modular.ordernotifyinfo.entity.OrderNotifyInfo;
import vip.xiaonuo.pay.modular.ordernotifyinfo.enums.OrderNotifyInfoExceptionEnum;
import vip.xiaonuo.pay.modular.ordernotifyinfo.mapper.OrderNotifyInfoMapper;
import vip.xiaonuo.pay.modular.ordernotifyinfo.param.OrderNotifyInfoParam;
import vip.xiaonuo.pay.modular.ordernotifyinfo.service.OrderNotifyInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.pay.modular.payorder.entity.PayOrder;
import vip.xiaonuo.pay.modular.payorder.service.PayOrderService;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 通知记录信息service接口实现类
 *
 * @author abc
 * @date 2022-01-20 16:56:48
 */
@Service
public class OrderNotifyInfoServiceImpl extends ServiceImpl<OrderNotifyInfoMapper, OrderNotifyInfo> implements OrderNotifyInfoService {

    @Resource
    MchInfoService mchInfoService;
    @Resource
    PayOrderService payOrderService;

    @Override
    public PageResult<OrderNotifyInfo> page(OrderNotifyInfoParam orderNotifyInfoParam) {
        QueryWrapper<OrderNotifyInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(orderNotifyInfoParam)) {

            // 根据支付订单号 查询
            if (ObjectUtil.isNotEmpty(orderNotifyInfoParam.getPayOrderId())) {
                queryWrapper.lambda().like(OrderNotifyInfo::getPayOrderId, orderNotifyInfoParam.getPayOrderId());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(orderNotifyInfoParam.getMchNo())) {
                queryWrapper.lambda().like(OrderNotifyInfo::getMchNo, orderNotifyInfoParam.getMchNo());
            }
            // 根据状态（字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(orderNotifyInfoParam.getStatus())) {
                queryWrapper.lambda().eq(OrderNotifyInfo::getStatus, orderNotifyInfoParam.getStatus());
            }
        }
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                queryWrapper.lambda().eq(OrderNotifyInfo::getMchNo, mchInfo.getMchNo());
            }else{
                return new PageResult<>();
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<OrderNotifyInfo> list(OrderNotifyInfoParam orderNotifyInfoParam) {
        return this.list();
    }

    @Override
    public void add(OrderNotifyInfoParam orderNotifyInfoParam) {
        OrderNotifyInfo orderNotifyInfo = new OrderNotifyInfo();
        BeanUtil.copyProperties(orderNotifyInfoParam, orderNotifyInfo);
        this.save(orderNotifyInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<OrderNotifyInfoParam> orderNotifyInfoParamList) {
        orderNotifyInfoParamList.forEach(orderNotifyInfoParam -> {
        OrderNotifyInfo orderNotifyInfo = this.queryOrderNotifyInfo(orderNotifyInfoParam);
            this.removeById(orderNotifyInfo.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(OrderNotifyInfoParam orderNotifyInfoParam) {
        OrderNotifyInfo orderNotifyInfo = this.queryOrderNotifyInfo(orderNotifyInfoParam);
        BeanUtil.copyProperties(orderNotifyInfoParam, orderNotifyInfo);
        this.updateById(orderNotifyInfo);
    }

    @Override
    public OrderNotifyInfo detail(OrderNotifyInfoParam orderNotifyInfoParam) {
        return this.queryOrderNotifyInfo(orderNotifyInfoParam);
    }

    @Override
    public void again(OrderNotifyInfoParam orderNotifyInfoParam) {
        OrderNotifyInfo info = this.getById(orderNotifyInfoParam.getId());
        if (ObjectUtil.isNull(info)) {
            throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST);
        }
        try {
            HttpResponse responseBody = HttpRequest.get(info.getNotifyUrl())
                    .charset(Charset.forName("UTF-8"))
                    .timeout(6000)//超时，毫秒
                    .execute();
            String body = responseBody.body();
            info.setHttpStatus(responseBody.getStatus() + "");
            if ("SUCCESS".equals(responseBody)) {
                info.setNotifyStatus(1);
            } else {
                throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST.getCode(),"通知失败，请求返回状态 "+responseBody.getStatus());
            }
            info.setReturnMsg(body);
        }catch (Exception e){
            throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST.getCode(),e.getMessage());
        }
        LambdaUpdateWrapper<PayOrder> oq = new LambdaUpdateWrapper<>();
        oq.set(PayOrder::getNotifyState,1);
        oq.set(PayOrder::getNotifyMsg,info.getReturnMsg());
        oq.ne(PayOrder::getNotifyState,1);
        oq.eq(PayOrder::getPayOrderId,info.getPayOrderId());
        payOrderService.update(oq);
        this.updateById(info);
    }

    /**
     * 获取通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    private OrderNotifyInfo queryOrderNotifyInfo(OrderNotifyInfoParam orderNotifyInfoParam) {
        OrderNotifyInfo orderNotifyInfo = this.getById(orderNotifyInfoParam.getId());
        if (ObjectUtil.isNull(orderNotifyInfo)) {
            throw new ServiceException(OrderNotifyInfoExceptionEnum.NOT_EXIST);
        }
        return orderNotifyInfo;
    }
}
