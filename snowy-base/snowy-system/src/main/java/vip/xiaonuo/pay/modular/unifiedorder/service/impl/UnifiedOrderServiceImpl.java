
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
package vip.xiaonuo.pay.modular.unifiedorder.service.impl;

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
import vip.xiaonuo.pay.modular.unifiedorder.entity.UnifiedOrder;
import vip.xiaonuo.pay.modular.unifiedorder.enums.UnifiedOrderExceptionEnum;
import vip.xiaonuo.pay.modular.unifiedorder.mapper.UnifiedOrderMapper;
import vip.xiaonuo.pay.modular.unifiedorder.param.UnifiedOrderParam;
import vip.xiaonuo.pay.modular.unifiedorder.service.UnifiedOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 请求下单service接口实现类
 *
 * @author abc
 * @date 2022-02-14 23:15:40
 */
@Service
public class UnifiedOrderServiceImpl extends ServiceImpl<UnifiedOrderMapper, UnifiedOrder> implements UnifiedOrderService {

    @Override
    public PageResult<UnifiedOrder> page(UnifiedOrderParam unifiedOrderParam) {
        QueryWrapper<UnifiedOrder> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(unifiedOrderParam)) {

            // 根据应用ID 查询
            if (ObjectUtil.isNotEmpty(unifiedOrderParam.getAppId())) {
                queryWrapper.lambda().like(UnifiedOrder::getAppId, unifiedOrderParam.getAppId());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(unifiedOrderParam.getMchNo())) {
                queryWrapper.lambda().like(UnifiedOrder::getMchNo, unifiedOrderParam.getMchNo());
            }
            // 根据序列ID唯一 查询
            if (ObjectUtil.isNotEmpty(unifiedOrderParam.getUuid())) {
                queryWrapper.lambda().like(UnifiedOrder::getUuid, unifiedOrderParam.getUuid());
            }
            // 根据请求ip 查询
            if (ObjectUtil.isNotEmpty(unifiedOrderParam.getReqIp())) {
                queryWrapper.lambda().like(UnifiedOrder::getReqIp, unifiedOrderParam.getReqIp());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<UnifiedOrder> list(UnifiedOrderParam unifiedOrderParam) {
        return this.list();
    }

    @Override
    public void add(UnifiedOrderParam unifiedOrderParam) {
        UnifiedOrder unifiedOrder = new UnifiedOrder();
        BeanUtil.copyProperties(unifiedOrderParam, unifiedOrder);
        this.save(unifiedOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<UnifiedOrderParam> unifiedOrderParamList) {
        unifiedOrderParamList.forEach(unifiedOrderParam -> {
        UnifiedOrder unifiedOrder = this.queryUnifiedOrder(unifiedOrderParam);
            this.removeById(unifiedOrder.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(UnifiedOrderParam unifiedOrderParam) {
        UnifiedOrder unifiedOrder = this.queryUnifiedOrder(unifiedOrderParam);
        BeanUtil.copyProperties(unifiedOrderParam, unifiedOrder);
        this.updateById(unifiedOrder);
    }

    @Override
    public UnifiedOrder detail(UnifiedOrderParam unifiedOrderParam) {
        return this.queryUnifiedOrder(unifiedOrderParam);
    }

    /**
     * 获取请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    private UnifiedOrder queryUnifiedOrder(UnifiedOrderParam unifiedOrderParam) {
        UnifiedOrder unifiedOrder = this.getById(unifiedOrderParam.getId());
        if (ObjectUtil.isNull(unifiedOrder)) {
            throw new ServiceException(UnifiedOrderExceptionEnum.NOT_EXIST);
        }
        return unifiedOrder;
    }
}
