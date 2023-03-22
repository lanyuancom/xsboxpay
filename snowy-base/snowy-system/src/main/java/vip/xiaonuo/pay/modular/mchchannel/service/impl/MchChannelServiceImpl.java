
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
package vip.xiaonuo.pay.modular.mchchannel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.mchchannel.entity.MchChannel;
import vip.xiaonuo.pay.modular.mchchannel.enums.MchChannelExceptionEnum;
import vip.xiaonuo.pay.modular.mchchannel.mapper.MchChannelMapper;
import vip.xiaonuo.pay.modular.mchchannel.param.MchChannelParam;
import vip.xiaonuo.pay.modular.mchchannel.service.MchChannelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;
import vip.xiaonuo.pay.modular.paychannel.entity.PayChannel;
import vip.xiaonuo.pay.modular.paychannel.service.PayChannelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商户开通的渠道service接口实现类
 *
 * @author abc
 * @date 2022-03-09 14:49:50
 */
@Service
public class MchChannelServiceImpl extends ServiceImpl<MchChannelMapper, MchChannel> implements MchChannelService {

    @Resource
    private PayChannelService payChannelService;

    @Resource
    MchInfoService mchInfoService;
    @Override
    public PageResult<MchChannel> page(MchChannelParam mchChannelParam) {
        QueryWrapper<MchChannel> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(mchChannelParam)) {

            // 根据渠道ID 查询
            if (ObjectUtil.isNotEmpty(mchChannelParam.getChannelId())) {
                queryWrapper.lambda().eq(MchChannel::getChannelId, mchChannelParam.getChannelId());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(mchChannelParam.getMchNo())) {
                queryWrapper.lambda().like(MchChannel::getMchNo, mchChannelParam.getMchNo());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<PayChannel> list(MchChannelParam mchChannelParam) {
        List<PayChannel>  cchs =  payChannelService.list();
        if (CollUtil.isEmpty(cchs)||StrUtil.isBlank(mchChannelParam.getMchNo())){
            return cchs;
        }
        if (CollUtil.isNotEmpty(cchs)){
            cchs.forEach(v-> {
                v.setCheck(false);
                v.setAliPay(false);
                v.setWxPay(false);
            });
        }

        LambdaQueryWrapper<MchChannel> mchQuery = new LambdaQueryWrapper<>();
        mchQuery.eq(MchChannel::getMchNo,mchChannelParam.getMchNo());
        List<MchChannel> mchs = this.list(mchQuery);
        if (CollUtil.isNotEmpty(mchs)){
            Map<Long, MchChannel> mchsMap= mchs.stream().collect(Collectors.toMap(MchChannel::getChannelId, e -> e , (oldValue, newValue) -> newValue));
            if (CollUtil.isNotEmpty(cchs)){
                cchs.forEach(v-> {
                    if (mchsMap.containsKey(v.getId())){
                        v.setCheck(true);
                        v.setAliPay(mchsMap.get(v.getId()).isAliPay());
                        v.setWxPay(mchsMap.get(v.getId()).isWxPay());
                    }else{
                        v.setCheck(false);
                        v.setAliPay(false);
                        v.setWxPay(false);
                    }
                });
            }
        }
        return cchs;
    }

    @Override
    public void add(MchChannelParam mchChannelParam) {
        MchChannel mchChannel = new MchChannel();
        BeanUtil.copyProperties(mchChannelParam, mchChannel);
        this.save(mchChannel);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<MchChannelParam> mchChannelParamList) {
        mchChannelParamList.forEach(mchChannelParam -> {
        MchChannel mchChannel = this.queryMchChannel(mchChannelParam);
            this.removeById(mchChannel.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(MchChannelParam mchChannelParam) {
        MchChannel mchChannel = this.queryMchChannel(mchChannelParam);
        BeanUtil.copyProperties(mchChannelParam, mchChannel);
        this.updateById(mchChannel);
    }

    @Override
    public MchChannel detail(MchChannelParam mchChannelParam) {
        return this.queryMchChannel(mchChannelParam);
    }

    @Override
    public void distribution(MchChannelParam mchChannelParam) {
        List<MchChannel> mchs = new ArrayList<>();
        LambdaQueryWrapper<MchChannel> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(MchChannel::getChannelId,mchChannelParam.getChannelId());
        this.remove(queryWrapper);
        List<String> ms = mchChannelParam.getMchNos();
        if (CollUtil.isNotEmpty(ms)) {
            ms.forEach(v -> {
                MchChannel mch= new MchChannel();
                mch.setMchNo(v);
                mch.setChannelId(mchChannelParam.getChannelId());
                mch.setAliPay(mchChannelParam.isAliPay());
                mch.setWxPay(mchChannelParam.isWxPay());
                mch.setStatus(0);
                mchs.add(mch);
            });
            if(CollUtil.isNotEmpty(mchs)){
                this.saveBatch(mchs);
            }
        }
    }

    @Override
    public List<Map> findChannel(MchChannelParam mchChannelParam) {
        List<MchInfo> mchInfos = mchInfoService.list();
        if(CollUtil.isEmpty(mchInfos) || null == mchChannelParam.getChannelId()){
            return null;
        }

        List<Map> maps = JSONUtil.toList(JSONUtil.toJsonStr(mchInfos), Map.class);
        maps.forEach(v -> {
            v.put("check",false);
        });

        LambdaQueryWrapper<MchChannel> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(MchChannel::getChannelId,mchChannelParam.getChannelId());
        List<MchChannel> mchs = this.list(queryWrapper);
        if(CollUtil.isNotEmpty(mchs)){
            Set<String> sets = mchs.stream().map(MchChannel::getMchNo).collect(Collectors.toSet());
            maps.forEach(v -> {
                if(sets.contains(v.get("mchNo")+"")){
                    v.put("check",true);
                }
            });
        }
        return maps;
    }

    /**
     * 获取商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    private MchChannel queryMchChannel(MchChannelParam mchChannelParam) {
        MchChannel mchChannel = this.getById(mchChannelParam.getId());
        if (ObjectUtil.isNull(mchChannel)) {
            throw new ServiceException(MchChannelExceptionEnum.NOT_EXIST);
        }
        return mchChannel;
    }
}
