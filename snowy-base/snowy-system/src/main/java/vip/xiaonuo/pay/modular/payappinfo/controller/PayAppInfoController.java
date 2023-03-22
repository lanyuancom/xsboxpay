
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
package vip.xiaonuo.pay.modular.payappinfo.controller;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.core.util.CommonTools;
import vip.xiaonuo.pay.modular.common.CommonController;
import vip.xiaonuo.pay.modular.payappinfo.entity.PayAppInfo;
import vip.xiaonuo.pay.modular.payappinfo.param.PayAppInfoParam;
import vip.xiaonuo.pay.modular.payappinfo.service.PayAppInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 转账应用管理控制器
 *
 * @author abc
 * @date 2022-03-03 10:37:10
 */
@Controller
public class PayAppInfoController extends CommonController {

    private String PATH_PREFIX = "pay/payAppInfo/";

    @Resource
    private PayAppInfoService payAppInfoService;


    @ResponseBody
    @RequestMapping("/payAppInfo/uploadFile")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        ApplicationHome h = new ApplicationHome(getClass());
        File jarF = h.getSource();
        String path = jarF.getParentFile().toString()+"/upload";
        jarF = new File(path);
        if(!jarF.exists()){
            jarF.mkdirs();
        }
        String name = CommonTools.generateFileName(file.getOriginalFilename());
        super.upload(file,name,path);
        return new SuccessResponseData(name);
    }
    /**
     * 转账应用管理页面
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @GetMapping("/payAppInfo/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 转账应用管理表单页面
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @GetMapping("/payAppInfo/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @GetMapping("/payAppInfo/page")
    @BusinessLog(title = "转账应用管理_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<PayAppInfo> page(PayAppInfoParam payAppInfoParam) {
        return payAppInfoService.page(payAppInfoParam);
    }

    /**
     * 添加转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @PostMapping("/payAppInfo/add")
    @BusinessLog(title = "转账应用管理_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(PayAppInfoParam.add.class) PayAppInfoParam payAppInfoParam) {
        payAppInfoService.add(payAppInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 删除转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @PostMapping("/payAppInfo/delete")
    @BusinessLog(title = "转账应用管理_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(PayAppInfoParam.delete.class) List<PayAppInfoParam> payAppInfoParamList) {
        payAppInfoService.delete(payAppInfoParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @PostMapping("/payAppInfo/edit")
    @BusinessLog(title = "转账应用管理_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(PayAppInfoParam.edit.class) PayAppInfoParam payAppInfoParam) {
        payAppInfoService.edit(payAppInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 查看转账应用管理
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @GetMapping("/payAppInfo/detail")
    @BusinessLog(title = "转账应用管理_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(PayAppInfoParam.detail.class) PayAppInfoParam payAppInfoParam) {
        return new SuccessResponseData(payAppInfoService.detail(payAppInfoParam));
    }

    /**
     * 转账应用管理列表
     *
     * @author abc
     * @date 2022-03-03 10:37:10
     */
    @Permission
    @ResponseBody
    @GetMapping("/payAppInfo/list")
    @BusinessLog(title = "转账应用管理_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(PayAppInfoParam payAppInfoParam) {
        return new SuccessResponseData(payAppInfoService.list(payAppInfoParam));
    }

}
