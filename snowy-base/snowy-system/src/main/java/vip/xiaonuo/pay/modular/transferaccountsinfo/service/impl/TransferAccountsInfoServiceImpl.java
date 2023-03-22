
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
package vip.xiaonuo.pay.modular.transferaccountsinfo.service.impl;

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
import vip.xiaonuo.pay.modular.transferaccountsinfo.entity.TransferAccountsInfo;
import vip.xiaonuo.pay.modular.transferaccountsinfo.enums.TransferAccountsInfoExceptionEnum;
import vip.xiaonuo.pay.modular.transferaccountsinfo.mapper.TransferAccountsInfoMapper;
import vip.xiaonuo.pay.modular.transferaccountsinfo.param.TransferAccountsInfoParam;
import vip.xiaonuo.pay.modular.transferaccountsinfo.service.TransferAccountsInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 转账账号管理service接口实现类
 *
 * @author kk -- (mmm333zzz520@163.com)
 * @date 2022-09-23 11:24:12
 */
@Service
public class TransferAccountsInfoServiceImpl extends ServiceImpl<TransferAccountsInfoMapper, TransferAccountsInfo> implements TransferAccountsInfoService {

    @Override
    public PageResult<TransferAccountsInfo> page(TransferAccountsInfoParam transferAccountsInfoParam) {
        QueryWrapper<TransferAccountsInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(transferAccountsInfoParam)) {

            // 根据支付宝账号 查询
            if (ObjectUtil.isNotEmpty(transferAccountsInfoParam.getAccount())) {
                queryWrapper.lambda().like(TransferAccountsInfo::getAccount, transferAccountsInfoParam.getAccount());
            }
            // 根据支付宝姓名 查询
            if (ObjectUtil.isNotEmpty(transferAccountsInfoParam.getName())) {
                queryWrapper.lambda().like(TransferAccountsInfo::getName, transferAccountsInfoParam.getName());
            }
            // 根据标签 查询
            if (ObjectUtil.isNotEmpty(transferAccountsInfoParam.getTags())) {
                queryWrapper.lambda().like(TransferAccountsInfo::getTags, transferAccountsInfoParam.getTags());
            }
            // 根据应用状态: （字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(transferAccountsInfoParam.getStatus())) {
                queryWrapper.lambda().eq(TransferAccountsInfo::getStatus, transferAccountsInfoParam.getStatus());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<TransferAccountsInfo> list(TransferAccountsInfoParam transferAccountsInfoParam) {
// 根据标签 查询
        QueryWrapper<TransferAccountsInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotEmpty(transferAccountsInfoParam.getTags())) {
            transferAccountsInfoParam.setTags(","+transferAccountsInfoParam.getTags()+",");
            queryWrapper.lambda().like(TransferAccountsInfo::getTags, transferAccountsInfoParam.getTags());
        }
        return this.list(queryWrapper);
    }

    @Override
    public void add(TransferAccountsInfoParam transferAccountsInfoParam) {
        TransferAccountsInfo transferAccountsInfo = new TransferAccountsInfo();
        BeanUtil.copyProperties(transferAccountsInfoParam, transferAccountsInfo);
        String tags = transferAccountsInfo.getTags();
        tags=tags.replaceAll(",,",",");
        if(!tags.startsWith(",")){
            tags = ","+tags;
        }
        if(!tags.endsWith(",")){
            tags = tags+",";
        }
        transferAccountsInfo.setTags(tags);
        this.save(transferAccountsInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<TransferAccountsInfoParam> transferAccountsInfoParamList) {
        transferAccountsInfoParamList.forEach(transferAccountsInfoParam -> {
        TransferAccountsInfo transferAccountsInfo = this.queryTransferAccountsInfo(transferAccountsInfoParam);
            this.removeById(transferAccountsInfo.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(TransferAccountsInfoParam transferAccountsInfoParam) {
        TransferAccountsInfo transferAccountsInfo = this.queryTransferAccountsInfo(transferAccountsInfoParam);
        BeanUtil.copyProperties(transferAccountsInfoParam, transferAccountsInfo);
        String tags = transferAccountsInfo.getTags();
        tags=tags.replaceAll(",,",",");
        if(!tags.startsWith(",")){
            tags = ","+tags;
        }
        if(!tags.endsWith(",")){
            tags = tags+",";
        }
        transferAccountsInfo.setTags(tags);
        this.updateById(transferAccountsInfo);
    }

    @Override
    public TransferAccountsInfo detail(TransferAccountsInfoParam transferAccountsInfoParam) {
        return this.queryTransferAccountsInfo(transferAccountsInfoParam);
    }

    @Override
    public Set<String> taglist(TransferAccountsInfoParam transferAccountsInfoParam) {
        List<TransferAccountsInfo> list = this.list();
        String str = list.stream().map(TransferAccountsInfo::getTags).collect(Collectors.joining(","));
        return Arrays.asList(str.split(",")).stream().collect(Collectors.toSet());
    }

    /**
     * 获取转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    private TransferAccountsInfo queryTransferAccountsInfo(TransferAccountsInfoParam transferAccountsInfoParam) {
        TransferAccountsInfo transferAccountsInfo = this.getById(transferAccountsInfoParam.getId());
        if (ObjectUtil.isNull(transferAccountsInfo)) {
            throw new ServiceException(TransferAccountsInfoExceptionEnum.NOT_EXIST);
        }
        return transferAccountsInfo;
    }
}
