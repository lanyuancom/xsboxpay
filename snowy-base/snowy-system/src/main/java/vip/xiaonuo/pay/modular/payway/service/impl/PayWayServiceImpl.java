
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
package vip.xiaonuo.pay.modular.payway.service.impl;

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
import vip.xiaonuo.pay.modular.payway.entity.PayWay;
import vip.xiaonuo.pay.modular.payway.enums.PayWayExceptionEnum;
import vip.xiaonuo.pay.modular.payway.mapper.PayWayMapper;
import vip.xiaonuo.pay.modular.payway.param.PayWayParam;
import vip.xiaonuo.pay.modular.payway.service.PayWayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 支付方式表service接口实现类
 *
 * @author abc
 * @date 2022-01-01 23:53:12
 */
@Service
public class PayWayServiceImpl extends ServiceImpl<PayWayMapper, PayWay> implements PayWayService {

    @Override
    public PageResult<PayWay> page(PayWayParam payWayParam) {
        QueryWrapper<PayWay> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payWayParam)) {

            // 根据支付方式 查询
            if (ObjectUtil.isNotEmpty(payWayParam.getWayCode())) {
                queryWrapper.lambda().like(PayWay::getWayCode, payWayParam.getWayCode());
            }
            // 根据方式名称 查询
            if (ObjectUtil.isNotEmpty(payWayParam.getWayName())) {
                queryWrapper.lambda().like(PayWay::getWayName, payWayParam.getWayName());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayWay> list(PayWayParam payWayParam) {
        return this.list();
    }

    @Override
    public void add(PayWayParam payWayParam) {
        PayWay payWay = new PayWay();
        BeanUtil.copyProperties(payWayParam, payWay);
        this.save(payWay);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayWayParam> payWayParamList) {
        payWayParamList.forEach(payWayParam -> {
        PayWay payWay = this.queryPayWay(payWayParam);
            this.removeById(payWay.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayWayParam payWayParam) {
        PayWay payWay = this.queryPayWay(payWayParam);
        BeanUtil.copyProperties(payWayParam, payWay);
        this.updateById(payWay);
    }

    @Override
    public PayWay detail(PayWayParam payWayParam) {
        return this.queryPayWay(payWayParam);
    }

    /**
     * 获取支付方式表
     *
     * @author abc
     * @date 2022-01-01 23:53:12
     */
    private PayWay queryPayWay(PayWayParam payWayParam) {
        PayWay payWay = this.getById(payWayParam.getId());
        if (ObjectUtil.isNull(payWay)) {
            throw new ServiceException(PayWayExceptionEnum.NOT_EXIST);
        }
        return payWay;
    }
}
