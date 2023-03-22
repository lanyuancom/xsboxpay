
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
package vip.xiaonuo.pay.modular.payappinfo.service.impl;

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
import vip.xiaonuo.pay.modular.payappinfo.entity.PayAppInfo;
import vip.xiaonuo.pay.modular.payappinfo.enums.PayAppInfoExceptionEnum;
import vip.xiaonuo.pay.modular.payappinfo.mapper.PayAppInfoMapper;
import vip.xiaonuo.pay.modular.payappinfo.param.PayAppInfoParam;
import vip.xiaonuo.pay.modular.payappinfo.service.PayAppInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 转账应用管理service接口实现类
 *
 * @author abc
 * @date 2022-03-03 10:37:10
 */
@Service
public class PayAppInfoServiceImpl extends ServiceImpl<PayAppInfoMapper, PayAppInfo> implements PayAppInfoService {

    @Override
    public PageResult<PayAppInfo> page(PayAppInfoParam payAppInfoParam) {
        QueryWrapper<PayAppInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payAppInfoParam)) {

            // 根据支付宝账号 查询
            if (ObjectUtil.isNotEmpty(payAppInfoParam.getAccount())) {
                queryWrapper.lambda().like(PayAppInfo::getAccount, payAppInfoParam.getAccount());
            }
            // 根据支付宝姓名 查询
            if (ObjectUtil.isNotEmpty(payAppInfoParam.getName())) {
                queryWrapper.lambda().like(PayAppInfo::getName, payAppInfoParam.getName());
            }
            // 根据appid 查询
            if (ObjectUtil.isNotEmpty(payAppInfoParam.getAppId())) {
                queryWrapper.lambda().like(PayAppInfo::getAppId, payAppInfoParam.getAppId());
            }
            // 根据应用名称 查询
            if (ObjectUtil.isNotEmpty(payAppInfoParam.getAppName())) {
                queryWrapper.lambda().like(PayAppInfo::getAppName, payAppInfoParam.getAppName());
            }
            // 根据应用状态: （字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(payAppInfoParam.getStatus())) {
                queryWrapper.lambda().eq(PayAppInfo::getStatus, payAppInfoParam.getStatus());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayAppInfo> list(PayAppInfoParam payAppInfoParam) {
        return this.list();
    }

    @Override
    public void add(PayAppInfoParam payAppInfoParam) {
        PayAppInfo payAppInfo = new PayAppInfo();
        BeanUtil.copyProperties(payAppInfoParam, payAppInfo);
        this.save(payAppInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayAppInfoParam> payAppInfoParamList) {
        payAppInfoParamList.forEach(payAppInfoParam -> {
        PayAppInfo payAppInfo = this.queryPayAppInfo(payAppInfoParam);
            this.removeById(payAppInfo.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayAppInfoParam payAppInfoParam) {
        PayAppInfo payAppInfo = this.queryPayAppInfo(payAppInfoParam);
        BeanUtil.copyProperties(payAppInfoParam, payAppInfo);
        this.updateById(payAppInfo);
    }

    @Override
    public PayAppInfo detail(PayAppInfoParam payAppInfoParam) {
        return this.queryPayAppInfo(payAppInfoParam);
    }

    /**
     * 获取转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    private PayAppInfo queryPayAppInfo(PayAppInfoParam payAppInfoParam) {
        PayAppInfo payAppInfo = this.getById(payAppInfoParam.getId());
        if (ObjectUtil.isNull(payAppInfo)) {
            throw new ServiceException(PayAppInfoExceptionEnum.NOT_EXIST);
        }
        return payAppInfo;
    }
}
