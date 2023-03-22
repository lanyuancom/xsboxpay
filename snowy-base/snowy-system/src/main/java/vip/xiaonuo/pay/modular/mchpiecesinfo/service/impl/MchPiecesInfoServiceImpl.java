
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
package vip.xiaonuo.pay.modular.mchpiecesinfo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.langkye.api.paychl.wechatpay.applyment.bean.Root;
import com.langkye.api.paychl.wechatpay.applyment.common.ApplymentQueryType;
import com.langkye.api.paychl.wechatpay.applyment.common.FileLocation;
import com.langkye.api.paychl.wechatpay.applyment.helper.BeanHelper;
import com.langkye.api.paychl.wechatpay.applyment.utils.WeChatPayIncomingApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.file.FileOperator;
import vip.xiaonuo.core.file.common.exp.FileServiceException;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.CommonTools;
import vip.xiaonuo.pay.modular.mchpiecesinfo.entity.MchPiecesInfo;
import vip.xiaonuo.pay.modular.mchpiecesinfo.enums.MchPiecesInfoExceptionEnum;
import vip.xiaonuo.pay.modular.mchpiecesinfo.mapper.MchPiecesInfoMapper;
import vip.xiaonuo.pay.modular.mchpiecesinfo.param.MchPiecesInfoParam;
import vip.xiaonuo.pay.modular.mchpiecesinfo.service.MchPiecesInfoService;
import vip.xiaonuo.pay.modular.mchpiecesinfo.utils.ApiPiecesRegister;
import vip.xiaonuo.pay.modular.mchpiecesinfo.utils.PayUtils;
import vip.xiaonuo.pay.modular.paychannel.entity.PayChannel;
import vip.xiaonuo.pay.modular.paychannel.service.PayChannelService;
import vip.xiaonuo.sys.config.FileConfig;
import vip.xiaonuo.sys.modular.file.entity.SysFileInfo;
import vip.xiaonuo.sys.modular.file.service.SysFileInfoService;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商户进件service接口实现类
 *
 * @author abc
 * @date 2022-01-16 21:16:18
 */
@Service
public class MchPiecesInfoServiceImpl extends ServiceImpl<MchPiecesInfoMapper, MchPiecesInfo> implements MchPiecesInfoService {

    private Log log = Log.get();

    @Resource
    PayChannelService payChannelService;

    @Resource
    private FileOperator fileOperator;

    @Resource
    SysFileInfoService sysFileInfoService;

    @Override
    public PageResult<MchPiecesInfo> page(MchPiecesInfoParam mchPiecesInfoParam) {
        QueryWrapper<MchPiecesInfo> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(mchPiecesInfoParam)) {

            // 根据法人姓名 查询
            if (ObjectUtil.isNotEmpty(mchPiecesInfoParam.getLegarName())) {
                queryWrapper.lambda().like(MchPiecesInfo::getLegarName, mchPiecesInfoParam.getLegarName());
            }
            // 根据法人手机号 查询
            if (ObjectUtil.isNotEmpty(mchPiecesInfoParam.getLegarPhone())) {
                queryWrapper.lambda().like(MchPiecesInfo::getLegarPhone, mchPiecesInfoParam.getLegarPhone());
            }
            // 根据状态（字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(mchPiecesInfoParam.getStatus())) {
                queryWrapper.lambda().eq(MchPiecesInfo::getStatus, mchPiecesInfoParam.getStatus());
            }
        }
        queryWrapper.orderByDesc("create_time");
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<MchPiecesInfo> list(MchPiecesInfoParam mchPiecesInfoParam) {
        return this.list();
    }

    @Override
    public void add(MchPiecesInfoParam mchPiecesInfoParam) {
        MchPiecesInfo mchPiecesInfo = new MchPiecesInfo();
        BeanUtil.copyProperties(mchPiecesInfoParam, mchPiecesInfo);
        Long id = CommonTools.getId(mchPiecesInfo);
        mchPiecesInfo.setId(id);

        if("wx".equals(mchPiecesInfoParam.getPiecesType())){



            Map<String,Object> mapScenari =  new HashMap<>();
            mapScenari.put("mpAppid",mchPiecesInfoParam.getMpAppid());
            mapScenari.put("mpPics",mchPiecesInfoParam.getMpPics());
            mchPiecesInfo.setScenario(JSONUtil.toJsonStr(mapScenari));

            Map<String,Object> map = JSONUtil.toBean(JSONUtil.toJsonStr(mchPiecesInfoParam),Map.class);
            map.put("id",id);
            // 判断文件存在不存在

            List<String> fileIds = new ArrayList<>();
            fileIds.add(mchPiecesInfoParam.getFrontCard());
            fileIds.add(mchPiecesInfoParam.getBackCard());
            fileIds.add(mchPiecesInfoParam.getMpPics());
            fileIds.add(mchPiecesInfoParam.getBusinessFront());
            List<SysFileInfo> sysFiles = sysFileInfoService.listByIds(fileIds);
            Map<String, SysFileInfo> sysFilesMap = sysFiles.stream().collect(Collectors.toMap(p -> p.getId().toString(), e -> e , (oldValue, newValue) -> newValue));

            SysFileInfo fileInfo = sysFilesMap.get(mchPiecesInfo.getBusinessFront());
            String filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();

            String media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);
            map.put("licenseCopy",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfo.getFrontCard());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            map.put("idCardCopy",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfo.getBackCard());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            //身份证国徽面照片	id_card_national	string[1,256]	是
            map.put("idCardNational",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfoParam.getMpPics());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            map.put("pics",media1);

            Root root = BeanHelper.toConvert(map);
            log.info("root = " + root);
            String applyment = weChatPayIncomingApi.applyment(root);
            log.info("applyment = " + applyment);
            Map applyMap = JSONUtil.toBean(applyment,Map.class);
            if(applyMap.containsKey("code") && "PARAM_ERROR".equals(applyMap.get("code"))){
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), applyMap.get("message")+"");
            }
            mchPiecesInfo.setPiecesMsg(applyment);
        }else{
            PayChannel pc = new PayChannel();
            pc.setChannelCode("baofu");
            pc = payChannelService.getOne(new QueryWrapper<PayChannel>(pc));
            if(pc == null){
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"通道不存在，请联系管理员");
            }
            try{
                JSONObject object = ApiPiecesRegister.register(mchPiecesInfo,pc.getPrivateKey(),pc.getChannelNo());
                if("000000".equals(object.getString("code"))){
                    object = object.getJSONObject("data");
                    mchPiecesInfo.setSubUserNo(object.getString("subUserNo"));
                    object = ApiPiecesRegister.registerquery(mchPiecesInfo.getSubUserNo(),pc.getPrivateKey(),pc.getChannelNo());
                    if("000000".equals(object.getString("code"))){
                        object = object.getJSONObject("data");
                        mchPiecesInfo.setPiecesStatus(object.getString("status"));

                        List<String> fileIds = new ArrayList<>();
                        fileIds.add(mchPiecesInfoParam.getFrontCard());
                        fileIds.add(mchPiecesInfoParam.getBackCard());
                        fileIds.add(mchPiecesInfoParam.getBusinessAccountLicense());
                        fileIds.add(mchPiecesInfoParam.getBusinessFront());
                        List<SysFileInfo> sysFiles = sysFileInfoService.listByIds(fileIds);
                        Map<String, SysFileInfo> sysFilesMap = sysFiles.stream().collect(Collectors.toMap(p -> p.getId().toString(), e -> e , (oldValue, newValue) -> newValue));
                        SysFileInfo info = sysFilesMap.get(mchPiecesInfo.getFrontCard());
                        byte[] bytes = fileOperator.getFileBytes(info.getFileBucket(),info.getFileObjectName());
                        object = ApiPiecesRegister.registerFile(info.getId()+"",mchPiecesInfo.getSubUserNo(),"FRONT_OF_ID_CARD",bytes,info.getFileObjectName(),pc.getPrivateKey(),pc.getChannelNo());
                        if(!"000000".equals(object.getString("code"))){
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件上传身份证正面异常: "+object.getString("msg"));
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBackCard());
                        object = ApiPiecesRegister.registerFile(info.getId()+"",mchPiecesInfo.getSubUserNo(),"BACK_OF_ID_CARD",bytes,info.getFileObjectName(),pc.getPrivateKey(),pc.getChannelNo());
                        if(!"000000".equals(object.getString("code"))){
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件上传身份证反面异常: "+object.getString("msg"));
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBusinessFront());
                        if(info != null){
                            object = ApiPiecesRegister.registerFile(info.getId()+"",mchPiecesInfo.getSubUserNo(),"BUSINESS_LICENSE",bytes,info.getFileObjectName(),pc.getPrivateKey(),pc.getChannelNo());
                            if(!"000000".equals(object.getString("code"))){
                                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件上传营业执照异常: "+object.getString("msg"));
                            }
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBusinessAccountLicense());
                        if(info != null){
                            object = ApiPiecesRegister.registerFile(info.getId()+"",mchPiecesInfo.getSubUserNo(),"PERMIT_FOR_BANK_ACCOUNT",bytes,info.getFileObjectName(),pc.getPrivateKey(),pc.getChannelNo());
                            if(!"000000".equals(object.getString("code"))){
                                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件上传开户许可异常: "+object.getString("msg"));
                            }
                        }
                        object = ApiPiecesRegister.confirm(mchPiecesInfo.getSubUserNo(),pc.getPrivateKey(),pc.getChannelNo());
                        if(!"000000".equals(object.getString("code"))){
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件确认异常: "+object.getString("msg"));
                        }
                        mchPiecesInfo.setPiecesStatus("AUDITING");
                    }else{
                        throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件查询异常: "+object.getString("msg"));
                    }
                }else{
                    throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件注册异常: "+object.getString("msg"));
                }
            }catch (Exception e){
                log.error(e);
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),e.getMessage());
            }
        }
        this.save(mchPiecesInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<MchPiecesInfoParam> mchPiecesInfoParamList) {
        mchPiecesInfoParamList.forEach(mchPiecesInfoParam -> {
        MchPiecesInfo mchPiecesInfo = this.queryMchPiecesInfo(mchPiecesInfoParam);
            this.removeById(mchPiecesInfo.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(MchPiecesInfoParam mchPiecesInfoParam) {

        MchPiecesInfo mchPiecesInfo = this.queryMchPiecesInfo(mchPiecesInfoParam);
        BeanUtil.copyProperties(mchPiecesInfoParam, mchPiecesInfo);
        if("wx".equals(mchPiecesInfoParam.getPiecesType())){

            Map<String,Object> mapScenari =  new HashMap<>();
            mapScenari.put("mpAppid",mchPiecesInfoParam.getMpAppid());
            mapScenari.put("mpPics",mchPiecesInfoParam.getMpPics());
            mchPiecesInfo.setScenario(JSONUtil.toJsonStr(mapScenari));

            Map<String,Object> map = JSONUtil.toBean(JSONUtil.toJsonStr(mchPiecesInfoParam),Map.class);
            map.put("id",mchPiecesInfoParam.getId());
            // 判断文件存在不存在

            List<String> fileIds = new ArrayList<>();
            fileIds.add(mchPiecesInfoParam.getFrontCard());
            fileIds.add(mchPiecesInfoParam.getBackCard());
            fileIds.add(mchPiecesInfoParam.getMpPics());
            fileIds.add(mchPiecesInfoParam.getBusinessFront());
            List<SysFileInfo> sysFiles = sysFileInfoService.listByIds(fileIds);
            Map<String, SysFileInfo> sysFilesMap = sysFiles.stream().collect(Collectors.toMap(p -> p.getId().toString(), e -> e , (oldValue, newValue) -> newValue));

            SysFileInfo fileInfo = sysFilesMap.get(mchPiecesInfo.getBusinessFront());
            String filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();

            String media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);
            map.put("licenseCopy",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfo.getFrontCard());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            map.put("idCardCopy",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfo.getBackCard());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            //身份证国徽面照片	id_card_national	string[1,256]	是
            map.put("idCardNational",media1);

            fileInfo = sysFilesMap.get(mchPiecesInfoParam.getMpPics());
            filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
            media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

            map.put("pics",media1);

            Root root = BeanHelper.toConvert(map);
            log.info("root = " + root);
            String applyment = weChatPayIncomingApi.applyment(root);
            log.info("applyment = " + applyment);
            Map applyMap = JSONUtil.toBean(applyment,Map.class);
            if(applyMap.containsKey("code") && "PARAM_ERROR".equals(applyMap.get("code"))){
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), applyMap.get("message")+"");
            }
            mchPiecesInfo.setPiecesMsg(applyment);
            this.updateById(mchPiecesInfo);
        }else {
            Long oid = mchPiecesInfo.getId();

            Long id = CommonTools.getId(mchPiecesInfo);
            mchPiecesInfo.setId(id);

            PayChannel pc = new PayChannel();
            pc.setChannelCode("baofu");
            pc = payChannelService.getOne(new QueryWrapper<PayChannel>(pc));
            if (pc == null) {
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "通道不存在，请联系管理员");
            }
            try {
                JSONObject object = ApiPiecesRegister.register(mchPiecesInfo, pc.getPrivateKey(), pc.getChannelNo());
                if ("000000".equals(object.getString("code"))) {
                    object = object.getJSONObject("data");
                    mchPiecesInfo.setSubUserNo(object.getString("subUserNo"));
                    object = ApiPiecesRegister.registerquery(mchPiecesInfo.getSubUserNo(), pc.getPrivateKey(), pc.getChannelNo());
                    if ("000000".equals(object.getString("code"))) {
                        object = object.getJSONObject("data");
                        mchPiecesInfo.setPiecesStatus(object.getString("status"));

                        List<String> fileIds = new ArrayList<>();
                        fileIds.add(mchPiecesInfoParam.getFrontCard());
                        fileIds.add(mchPiecesInfoParam.getBackCard());
                        fileIds.add(mchPiecesInfoParam.getBusinessAccountLicense());
                        fileIds.add(mchPiecesInfoParam.getBusinessFront());
                        List<SysFileInfo> sysFiles = sysFileInfoService.listByIds(fileIds);
                        Map<String, SysFileInfo> sysFilesMap = sysFiles.stream().collect(Collectors.toMap(p -> p.getId().toString(), e -> e, (oldValue, newValue) -> newValue));
                        SysFileInfo info = sysFilesMap.get(mchPiecesInfo.getFrontCard());
                        byte[] bytes = fileOperator.getFileBytes(info.getFileBucket(), info.getFileObjectName());
                        object = ApiPiecesRegister.registerFile(info.getId() + "", mchPiecesInfo.getSubUserNo(), "FRONT_OF_ID_CARD", bytes, info.getFileObjectName(), pc.getPrivateKey(), pc.getChannelNo());
                        if (!"000000".equals(object.getString("code"))) {
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件上传身份证正面异常: " + object.getString("msg"));
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBackCard());
                        bytes = fileOperator.getFileBytes(info.getFileBucket(), info.getFileObjectName());
                        object = ApiPiecesRegister.registerFile(info.getId() + "", mchPiecesInfo.getSubUserNo(), "BACK_OF_ID_CARD", bytes, info.getFileObjectName(), pc.getPrivateKey(), pc.getChannelNo());
                        if (!"000000".equals(object.getString("code"))) {
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件上传身份证反面异常: " + object.getString("msg"));
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBusinessFront());
                        if (info != null) {
                            bytes = fileOperator.getFileBytes(info.getFileBucket(), info.getFileObjectName());
                            object = ApiPiecesRegister.registerFile(info.getId() + "", mchPiecesInfo.getSubUserNo(), "BUSINESS_LICENSE", bytes, info.getFileObjectName(), pc.getPrivateKey(), pc.getChannelNo());
                            if (!"000000".equals(object.getString("code"))) {
                                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件上传营业执照异常: " + object.getString("msg"));
                            }
                        }
                        info = sysFilesMap.get(mchPiecesInfo.getBusinessAccountLicense());
                        if (info != null) {
                            bytes = fileOperator.getFileBytes(info.getFileBucket(), info.getFileObjectName());
                            object = ApiPiecesRegister.registerFile(info.getId() + "", mchPiecesInfo.getSubUserNo(), "PERMIT_FOR_BANK_ACCOUNT", bytes, info.getFileObjectName(), pc.getPrivateKey(), pc.getChannelNo());
                            if (!"000000".equals(object.getString("code"))) {
                                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件上传开户许可异常: " + object.getString("msg"));
                            }
                        }
                        object = ApiPiecesRegister.confirm(mchPiecesInfo.getSubUserNo(), pc.getPrivateKey(), pc.getChannelNo());
                        if (!"000000".equals(object.getString("code"))) {
                            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件确认异常: " + object.getString("msg"));
                        }
                        mchPiecesInfo.setPiecesStatus("AUDITING");
                    } else {
                        throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件查询异常: " + object.getString("msg"));
                    }
                } else {
                    throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), "进件注册异常: " + object.getString("msg"));
                }
            } catch (Exception e) {
                log.error(e);
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), e.getMessage());
            }

            this.save(mchPiecesInfo);
            this.removeById(oid);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copy(MchPiecesInfoParam mchPiecesInfoParam) {

        MchPiecesInfo mchPiecesInfo = this.queryMchPiecesInfo(mchPiecesInfoParam);
        //BeanUtil.copyProperties(mchPiecesInfoParam, mchPiecesInfo);
        mchPiecesInfo.setCreateTime(null);
        mchPiecesInfo.setUpdateTime(null);
        Long id = CommonTools.getId(mchPiecesInfo);
        mchPiecesInfo.setId(id);

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getLegarPhone())){
            mchPiecesInfo.setLegarPhone(mchPiecesInfoParam.getLegarPhone());
        }

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getContactPhone())){
            mchPiecesInfo.setContactPhone(mchPiecesInfoParam.getContactPhone());
        }

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getBusinessShortName())){
            mchPiecesInfo.setBusinessShortName(mchPiecesInfoParam.getBusinessShortName());
        }

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getServePhone())){
            mchPiecesInfo.setServePhone(mchPiecesInfoParam.getServePhone());
        }

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getContactName())){
            mchPiecesInfo.setContactName(mchPiecesInfoParam.getContactName());

        }

        if(StrUtil.isNotBlank(mchPiecesInfoParam.getContactEmail())){
            mchPiecesInfo.setLegarPhone(mchPiecesInfoParam.getContactEmail());
        }

        Map<String,Object> Scenariomap = JSONUtil.toBean(mchPiecesInfo.getScenario(),Map.class);
        Map<String,Object> map = JSONUtil.toBean(JSONUtil.toJsonStr(mchPiecesInfo),Map.class);
        map.putAll(Scenariomap);
        mchPiecesInfoParam.setMpPics(map.get("mpPics")+"");
        // 判断文件存在不存在

        List<String> fileIds = new ArrayList<>();
        fileIds.add(mchPiecesInfo.getFrontCard());
        fileIds.add(mchPiecesInfo.getBackCard());
        fileIds.add(mchPiecesInfoParam.getMpPics());
        fileIds.add(mchPiecesInfo.getBusinessFront());
        List<SysFileInfo> sysFiles = sysFileInfoService.listByIds(fileIds);
        Map<String, SysFileInfo> sysFilesMap = sysFiles.stream().collect(Collectors.toMap(p -> p.getId().toString(), e -> e , (oldValue, newValue) -> newValue));

        SysFileInfo fileInfo = sysFilesMap.get(mchPiecesInfo.getBusinessFront());
        String filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();

        String media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);
        map.put("licenseCopy",media1);


        fileInfo = sysFilesMap.get(mchPiecesInfo.getFrontCard());
        filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
        media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

        map.put("idCardCopy",media1);

        fileInfo = sysFilesMap.get(mchPiecesInfo.getBackCard());
        filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
        media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

        //身份证国徽面照片	id_card_national	string[1,256]	是
        map.put("idCardNational",media1);

        fileInfo = sysFilesMap.get(mchPiecesInfoParam.getMpPics());
        filePath = fileOperator.getLocalFilePath(fileInfo.getFileBucket(),fileInfo.getFileObjectName());
        media1 = weChatPayIncomingApi.uploadPic(filePath, FileLocation.LOCALHOST);

        map.put("pics",media1);

        Root root = BeanHelper.toConvert(map);
        log.info("root = " + root);
        String applyment = weChatPayIncomingApi.applyment(root);
        log.info("applyment = " + applyment);
        Map applyMap = JSONUtil.toBean(applyment,Map.class);
        if(applyMap.containsKey("code") && "PARAM_ERROR".equals(applyMap.get("code"))){
            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(), applyMap.get("message")+"");
        }
        mchPiecesInfo.setPiecesMsg(applyment);
        this.save(mchPiecesInfo);

    }

    @Override
    public MchPiecesInfo detail(MchPiecesInfoParam mchPiecesInfoParam) {
        return this.queryMchPiecesInfo(mchPiecesInfoParam);
    }

    /**
     * 获取商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    private MchPiecesInfo queryMchPiecesInfo(MchPiecesInfoParam mchPiecesInfoParam) {
        MchPiecesInfo mchPiecesInfo = this.getById(mchPiecesInfoParam.getId());
        if (ObjectUtil.isNull(mchPiecesInfo)) {
            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST);
        }
        return mchPiecesInfo;
    }

    @Override
    public List<Map<String,String>> findBankCode(String name){
        return this.baseMapper.findBankCode(name);
    }

    @Override
    public Map<String,Object> piecesStatusRefresh(String id, String subUserNo) {
        final WeChatPayIncomingApi weChatPayIncomingApi = new WeChatPayIncomingApi();
        String applymentStatus = weChatPayIncomingApi.applymentStatus(id, ApplymentQueryType.BUSINESS_CODE);
        System.out.println("applymentStatus = " + applymentStatus);
        Map map = JSONUtil.toBean(applymentStatus,Map.class);

        MchPiecesInfo mc = new MchPiecesInfo();
        if("APPLYMENT_STATE_FINISHED".equals(map.get("applyment_state")+"")){
            mc.setSubUserNo(map.get("sub_mchid")+"");
        }
        mc.setId(Long.valueOf(id));
        mc.setPiecesStatus(map.get("applyment_state")+"");
        mc.setPiecesMsg(applymentStatus);
        this.updateById(mc);
        map.put("piecesStatus",mc.getPiecesStatus());
        map.put("remark",mc.getPiecesMsg());
        return map;
        /*PayChannel pc = new PayChannel();
        pc.setChannelCode("baofu");
        pc = payChannelService.getOne(new QueryWrapper<PayChannel>(pc));
        if(pc == null){
            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"通道不存在，请联系管理员");
        }
        try{
            JSONObject object = ApiPiecesRegister.registerquery(subUserNo,pc.getPrivateKey(),pc.getChannelNo());
            if("000000".equals(object.getString("code"))) {
                object = object.getJSONObject("data");
                MchPiecesInfo mc = new MchPiecesInfo();
                mc.setId(Long.valueOf(id));
                mc.setPiecesStatus(object.getString("status"));
                mc.setPiecesMsg(object.getString("remark"));
                this.updateById(mc);
                return object.toJavaObject(Map.class);
            }else{
                throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件查询异常 -> "+ object.getString("msg"));
            }
        }catch (Exception e){
            log.error(e);
            throw new ServiceException(MchPiecesInfoExceptionEnum.NOT_EXIST.getCode(),"进件查询异常");
        }*/
    }

    @Override
    public void update(MchPiecesInfoParam mchPiecesInfoParam) {
        MchPiecesInfo mchPiecesInfo = this.queryMchPiecesInfo(mchPiecesInfoParam);
        BeanUtil.copyProperties(mchPiecesInfoParam, mchPiecesInfo);
        this.updateById(mchPiecesInfo);
    }
}
