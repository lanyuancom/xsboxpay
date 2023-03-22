
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
package vip.xiaonuo.pay.modular.mchinfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchapp.enums.MchAppExceptionEnum;
import vip.xiaonuo.pay.modular.mchapp.param.MchAppParam;
import vip.xiaonuo.pay.modular.mchapp.service.MchAppService;
import vip.xiaonuo.pay.modular.mchchannel.entity.MchChannel;
import vip.xiaonuo.pay.modular.mchchannel.service.MchChannelService;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.enums.MchInfoExceptionEnum;
import vip.xiaonuo.pay.modular.mchinfo.mapper.MchInfoMapper;
import vip.xiaonuo.pay.modular.mchinfo.param.MchInfoParam;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;
import vip.xiaonuo.sys.modular.role.entity.SysRole;
import vip.xiaonuo.sys.modular.role.service.SysRoleService;
import vip.xiaonuo.sys.modular.user.entity.SysUser;
import vip.xiaonuo.sys.modular.user.entity.SysUserRole;
import vip.xiaonuo.sys.modular.user.service.SysUserRoleService;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商户信息service接口实现类
 *
 * @author abc
 * @date 2022-01-01 23:22:45
 */
@Service
public class MchInfoServiceImpl extends ServiceImpl<MchInfoMapper, MchInfo> implements MchInfoService {

    @Resource
    SysUserService sysUserService;
    @Resource
    SysRoleService sysRoleService;
    @Resource
    SysUserRoleService sysUserRoleService;
    @Resource
    MchAppService mchAppService;

    @Resource
    private MchChannelService mchChannelService;
    @Override
    public PageResult<MchInfo> page(MchInfoParam mchInfoParam) {
        QueryWrapper<MchInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(mchInfoParam)) {

            // 根据商户号 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getMchNo())) {
                queryWrapper.lambda().like(MchInfo::getMchNo, mchInfoParam.getMchNo());
            }
            // 根据商户名称 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getMchName())) {
                queryWrapper.lambda().like(MchInfo::getMchName, mchInfoParam.getMchName());
            }
            // 根据商户简称 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getMchShortName())) {
                queryWrapper.lambda().like(MchInfo::getMchShortName, mchInfoParam.getMchShortName());
            }
            // 根据类型: 1-普通商户, 2-特约商户(服务商模式) 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getType())) {
                queryWrapper.lambda().eq(MchInfo::getType, mchInfoParam.getType());
            }
            // 根据商户备注 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getRemark())) {
                queryWrapper.lambda().eq(MchInfo::getRemark, mchInfoParam.getRemark());
            }
            // 根据状态 查询
            if (ObjectUtil.isNotEmpty(mchInfoParam.getStatus())) {
                queryWrapper.lambda().eq(MchInfo::getStatus, mchInfoParam.getStatus());
            }
        }
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<MchInfo> list(MchInfoParam mchInfoParam) {
        return this.list();
    }

    @Override
    public void add(MchInfoParam mchInfoParam) {
        MchInfo mchInfo = new MchInfo();
        BeanUtil.copyProperties(mchInfoParam, mchInfo);

        MchInfo info = new MchInfo();
        info.setMchNo(mchInfo.getMchNo());
        List infos = this.list(new QueryWrapper<MchInfo>(info));
        if(CollUtil.isNotEmpty(infos)){
            throw new ServiceException(MchAppExceptionEnum.NOT_EXIST.getCode(),"商户号已经存在，请检查！");
        }

        SysUser sysUser = new SysUser();
        sysUser.setName(mchInfo.getMchName());
        sysUser.setNickName(mchInfo.getMchName());
        sysUser.setAccount(mchInfo.getMchNo()+"_"+RandomUtil.randomString(10));
        sysUser.setEmail("123@qq.com");
        sysUser.setPassword(BCrypt.hashpw(ConstantContextHolder.getDefaultPassWord(), BCrypt.gensalt()));
        sysUserService.save(sysUser);
        SysRole role = new SysRole();
        role.setCode("pay_mch");
        role = sysRoleService.getOne(new QueryWrapper<>(role));
        if(null != role){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(sysUser.getId());
            sysUserRole.setRoleId(role.getId());
            sysUserRoleService.save(sysUserRole);
        }
        mchInfo.setUserId(sysUser.getId());
        this.save(mchInfo);

        List<MchChannel> cs = mchInfoParam.getChannels();
        if(CollUtil.isNotEmpty(cs)){
            List<MchChannel> mcs = new ArrayList<>();
            cs.forEach(v -> {
                if(v.getChannelId() != null){
                    v.setStatus(0);
                    v.setMchNo(mchInfo.getMchNo());
                    MchChannel em = new MchChannel();
                    BeanUtil.copyProperties(v, em);
                    mcs.add(em);
                }
            });
            if(CollUtil.isNotEmpty(mcs)){
                mchChannelService.saveBatch(mcs);
            }
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<MchInfoParam> mchInfoParamList) {
        mchInfoParamList.forEach(mchInfoParam -> {
        MchInfo mchInfo = this.queryMchInfo(mchInfoParam);
            this.removeById(mchInfo.getId());
            MchAppParam mchApp = new MchAppParam();
            mchApp.setMchNo(mchInfo.getMchNo());
            List<MchApp> mchApps = mchAppService.list(mchApp);
            if(CollUtil.isNotEmpty(mchApps)){
                List<MchAppParam> params = new ArrayList<>();
                mchApps.forEach(v -> {
                    MchAppParam mc = new MchAppParam();
                    BeanUtil.copyProperties(v, mc);
                    params.add(mc);
                });
                mchAppService.delete(params);
            }
            if(mchInfo.getUserId() != null){
                sysUserService.removeById(mchInfo.getUserId());
            }
            LambdaQueryWrapper<MchChannel> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(MchChannel::getMchNo,mchInfo.getMchNo());
            mchChannelService.remove(queryWrapper);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(MchInfoParam mchInfoParam) {
        mchInfoParam.setMchNo(null);
        MchInfo mchInfo = this.queryMchInfo(mchInfoParam);
        String mchno = mchInfo.getMchNo();
        BeanUtil.copyProperties(mchInfoParam, mchInfo);
        this.updateById(mchInfo);

        SysUser sysUser = new SysUser();
        sysUser.setId(mchInfo.getUserId());
        sysUser.setName(mchInfo.getMchName());
        sysUser.setNickName(mchInfo.getMchName());
        sysUserService.updateById(sysUser);

        List<MchChannel> cs = mchInfoParam.getChannels();
        if(CollUtil.isNotEmpty(cs)){
            List<MchChannel> mcs = new ArrayList<>();
            cs.forEach(v -> {
                if(v.getChannelId() != null){
                    v.setStatus(0);
                    v.setMchNo(mchno);
                    MchChannel em = new MchChannel();
                    BeanUtil.copyProperties(v, em);
                    mcs.add(em);
                }
            });
            if(CollUtil.isNotEmpty(mcs)){
                LambdaQueryWrapper<MchChannel> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(MchChannel::getMchNo,mchno);
                mchChannelService.remove(queryWrapper);
                mchChannelService.saveBatch(mcs);
            }
        }

    }

    @Override
    public MchInfo detail(MchInfoParam mchInfoParam) {
        return this.queryMchInfo(mchInfoParam);
    }

    @Override
    public MchInfo info() {
        MchInfo mchInfo = new MchInfo();
        mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
        mchInfo =  this.getOne(new QueryWrapper<>(mchInfo));
        if(mchInfo == null){
            mchInfo = new MchInfo();
        }
        return mchInfo;
    }

    /**
     * 获取商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    private MchInfo queryMchInfo(MchInfoParam mchInfoParam) {
        MchInfo mchInfo = this.getById(mchInfoParam.getId());
        if (ObjectUtil.isNull(mchInfo)) {
            throw new ServiceException(MchInfoExceptionEnum.NOT_EXIST);
        }
        return mchInfo;
    }
    @Override
    public Map<String,Object> dayTotal(MchInfoParam mchInfoParam){
        Map<String,Object>  maps = new HashMap<>();
        QueryWrapper<MchInfo> queryWrapperMchInfo = new QueryWrapper();
        if(StrUtil.isBlank(mchInfoParam.getSTime())){
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:01");
                String today = format.format(new Date());
                mchInfoParam.setSTime(today);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        queryWrapperMchInfo.le(StrUtil.isNotBlank(mchInfoParam.getETime()),"create_time",mchInfoParam.getETime()+" 23:59:59");
        queryWrapperMchInfo.ge(StrUtil.isNotBlank(mchInfoParam.getSTime()),"create_time",mchInfoParam.getSTime()+" 00:00:01");
        maps.put("count_mch",this.count());
        maps.put("day_count_mch",this.count(queryWrapperMchInfo));
        return maps;
    }
}
