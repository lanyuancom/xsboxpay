
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
package vip.xiaonuo.pay.modular.paychannel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.consts.CommonConstant;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.enums.CommonStatusEnum;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.CommonTools;
import vip.xiaonuo.core.util.ResultBean;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchapp.enums.MchAppExceptionEnum;
import vip.xiaonuo.pay.modular.paychannel.entity.PayChannel;
import vip.xiaonuo.pay.modular.paychannel.enums.PayChannelExceptionEnum;
import vip.xiaonuo.pay.modular.paychannel.mapper.PayChannelMapper;
import vip.xiaonuo.pay.modular.paychannel.param.PayChannelParam;
import vip.xiaonuo.pay.modular.paychannel.service.PayChannelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付通道service接口实现类
 *
 * @author abc
 * @date 2022-01-06 14:59:46
 */
@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    @Override
    public PageResult<PayChannel> page(PayChannelParam payChannelParam) {
        QueryWrapper<PayChannel> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(payChannelParam)) {

            // 根据名称 查询
            if (ObjectUtil.isNotEmpty(payChannelParam.getName())) {
                queryWrapper.lambda().like(PayChannel::getName, payChannelParam.getName());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(payChannelParam.getMchNo())) {
                queryWrapper.lambda().eq(PayChannel::getMchNo, payChannelParam.getMchNo());
            }
            // 根据应用状态: （字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(payChannelParam.getStatus())) {
                queryWrapper.lambda().eq(PayChannel::getStatus, payChannelParam.getStatus());
            }
        }
        Page<PayChannel> page = this.page(PageFactory.defaultPage(), queryWrapper);
        List<PayChannel> list = page.getRecords();
        if(CollUtil.isNotEmpty(list)){
            for (PayChannel ch:list) {
                Map map = JSONUtil.toBean(ch.getIfParams(),Map.class);
                if(map.containsKey("wxService") && "1".equals(map.get("wxService")+"")){
                    ch.setWxService(true);
                }else{
                    ch.setWxService(false);
                }
            }
        }
        return new PageResult<>(page);
    }

    @Override
    public List<PayChannel> list(PayChannelParam payChannelParam) {
        return this.list();
    }

    @Override
    public void add(PayChannelParam payChannelParam) {
        PayChannel payChannel = new PayChannel();
        BeanUtil.copyProperties(payChannelParam, payChannel);
        this.save(payChannel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<PayChannelParam> payChannelParamList) {
        payChannelParamList.forEach(payChannelParam -> {
        PayChannel payChannel = this.queryPayChannel(payChannelParam);
            this.removeById(payChannel.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(PayChannelParam payChannelParam) {
        PayChannel payChannel = this.queryPayChannel(payChannelParam);
        BeanUtil.copyProperties(payChannelParam, payChannel);
        this.updateById(payChannel);
    }

    @Override
    public PayChannel detail(PayChannelParam payChannelParam) {
        return this.queryPayChannel(payChannelParam);
    }

    @Override
    public String upload(MultipartFile file) {
        String xsboxpay = ConstantContextHolder.getSysConfigWithDefault("XSBOX_PAY_URL",String.class,"");
        String url  = xsboxpay+"/xsbox-pay/payChannel/uploadFile";
        String test = CommonTools.sendPostWithFile(url,file,null);
        ResultBean rb = JSONUtil.toBean(test, ResultBean.class);
        if(rb.getCode() == 200){
            return rb.getData()+"";
        }else{
            throw new ServiceException(PayChannelExceptionEnum.NOT_EXIST.getCode(),rb.getMsg());
        }
    }

    /**
     * 获取支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    private PayChannel queryPayChannel(PayChannelParam payChannelParam) {
        PayChannel payChannel = this.getById(payChannelParam.getId());
        if (ObjectUtil.isNull(payChannel)) {
            throw new ServiceException(PayChannelExceptionEnum.NOT_EXIST);
        }
        return payChannel;
    }
}
