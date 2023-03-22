
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
package vip.xiaonuo.pay.modular.mchapp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchapp.enums.MchAppExceptionEnum;
import vip.xiaonuo.pay.modular.mchapp.mapper.MchAppMapper;
import vip.xiaonuo.pay.modular.mchapp.param.MchAppParam;
import vip.xiaonuo.pay.modular.mchapp.service.MchAppService;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 商户应用表service接口实现类
 *
 * @author abc
 * @date 2022-01-02 23:18:03
 */
@Service
public class MchAppServiceImpl extends ServiceImpl<MchAppMapper, MchApp> implements MchAppService {

    @Resource
    MchInfoService mchInfoService;

    @Override
    public PageResult<MchApp> page(MchAppParam mchAppParam) {
        QueryWrapper<MchApp> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(mchAppParam)) {

            // 根据应用名称 查询
            if (ObjectUtil.isNotEmpty(mchAppParam.getAppName())) {
                queryWrapper.lambda().like(MchApp::getAppName, mchAppParam.getAppName());
            }
            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(mchAppParam.getMchNo())) {
                queryWrapper.lambda().eq(MchApp::getMchNo, mchAppParam.getMchNo());
            }
            // 根据应用状态: （字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(mchAppParam.getStatus())) {
                queryWrapper.lambda().eq(MchApp::getStatus, mchAppParam.getStatus());
            }
        }

        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                queryWrapper.lambda().eq(MchApp::getMchNo, mchInfo.getMchNo());
            }else{
                return new PageResult<>();
            }
        }

        Page<MchApp> page = this.page(PageFactory.defaultPage(), queryWrapper);
        return new PageResult<>(page);
    }

    @Override
    public List<MchApp> list(MchAppParam mchAppParam) {
        List<MchApp> apps = new ArrayList<>();
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                mchAppParam.setMchNo(mchInfo.getMchNo());
            }
        }
        QueryWrapper<MchApp> queryWrapper = new QueryWrapper<>();
        MchApp mchApp= new MchApp();
        BeanUtil.copyProperties(mchAppParam, mchApp);
        queryWrapper.setEntity(mchApp);
        return this.list(queryWrapper);
    }

    @Override
    public void add(MchAppParam mchAppParam) {
        MchApp mchApp = new MchApp();
        BeanUtil.copyProperties(mchAppParam, mchApp);
        mchApp.setAppId(UUID.randomUUID().toString().replaceAll("-",""));
        mchApp.setAppSecret(makeRandom(128));
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                mchApp.setMchNo(mchInfo.getMchNo());
            }
        }else{
            MchInfo mchInfo = new MchInfo();
            mchInfo.setMchNo(mchAppParam.getMchNo());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo == null){
                throw new ServiceException(MchAppExceptionEnum.NOT_EXIST.getCode(),"商户号错误，找不到该商户信息，请检查！");
            }
        }
        this.save(mchApp);
    }
    public String makeRandom(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<MchAppParam> mchAppParamList) {
        mchAppParamList.forEach(mchAppParam -> {
        MchApp mchApp = this.queryMchApp(mchAppParam);
            this.removeById(mchApp.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(MchAppParam mchAppParam) {
        MchApp mchApp = this.queryMchApp(mchAppParam);
        String appId = mchApp.getAppId();
        BeanUtil.copyProperties(mchAppParam, mchApp);
        mchApp.setAppId(null);
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        if(!superAdmin){
            MchInfo mchInfo = new MchInfo();
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                mchApp.setMchNo(mchInfo.getMchNo());
            }
        }else{
            MchInfo mchInfo = new MchInfo();
            mchInfo.setMchNo(mchAppParam.getMchNo());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo == null){
                throw new ServiceException(MchAppExceptionEnum.NOT_EXIST.getCode(),"商户号错误，找不到该商户信息，请检查！");
            }
        }
        this.updateById(mchApp);
    }

    @Override
    public MchApp detail(MchAppParam mchAppParam) {
        return this.queryMchApp(mchAppParam);
    }

    /**
     * 获取商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    private MchApp queryMchApp(MchAppParam mchAppParam) {
        MchApp mchApp = this.getById(mchAppParam.getId());
        if (ObjectUtil.isNull(mchApp)) {
            throw new ServiceException(MchAppExceptionEnum.NOT_EXIST);
        }
        return mchApp;
    }
}
