
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
package vip.xiaonuo.pay.modular.paylog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.core.consts.CommonConstant;
import vip.xiaonuo.core.enums.CommonStatusEnum;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.paylog.entity.PayLog;
import vip.xiaonuo.pay.modular.paylog.enums.PayLogExceptionEnum;
import vip.xiaonuo.pay.modular.paylog.mapper.PayLogMapper;
import vip.xiaonuo.pay.modular.paylog.param.PayLogParam;
import vip.xiaonuo.pay.modular.paylog.service.PayLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 订单支付记录service接口实现类
 *
 * @author abc
 * @date 2022-01-05 23:07:24
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Override
    public PageResult<PayLog> page(PayLogParam payLogParam) {
        QueryWrapper<PayLog> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payLogParam)) {

            // 根据订单ID 查询
            if (ObjectUtil.isNotEmpty(payLogParam.getPayOrderId())) {
                queryWrapper.lambda().like(PayLog::getPayOrderId, payLogParam.getPayOrderId());
            }
            // 根据应用ID 查询
            if (ObjectUtil.isNotEmpty(payLogParam.getAppId())) {
                queryWrapper.lambda().like(PayLog::getAppId, payLogParam.getAppId());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(payLogParam.getMchNo())) {
                queryWrapper.lambda().like(PayLog::getMchNo, payLogParam.getMchNo());
            }
        }
        queryWrapper.orderByDesc("create_time");
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayLog> list(PayLogParam payLogParam) {
        return this.list();
    }

    @Override
    public void add(PayLogParam payLogParam) {
        PayLog payLog = new PayLog();
        BeanUtil.copyProperties(payLogParam, payLog);
        this.save(payLog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayLogParam> payLogParamList) {
        payLogParamList.forEach(payLogParam -> {
        PayLog payLog = this.queryPayLog(payLogParam);
            this.removeById(payLog.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayLogParam payLogParam) {
        PayLog payLog = this.queryPayLog(payLogParam);
        BeanUtil.copyProperties(payLogParam, payLog);
        this.updateById(payLog);
    }

    @Override
    public PayLog detail(PayLogParam payLogParam) {
        return this.queryPayLog(payLogParam);
    }

    /**
     * 获取订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    private PayLog queryPayLog(PayLogParam payLogParam) {
        PayLog payLog = this.getById(payLogParam.getId());
        if (ObjectUtil.isNull(payLog)) {
            throw new ServiceException(PayLogExceptionEnum.NOT_EXIST);
        }
        return payLog;
    }
}
