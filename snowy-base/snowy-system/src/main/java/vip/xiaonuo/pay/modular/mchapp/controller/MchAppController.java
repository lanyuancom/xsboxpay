
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
package vip.xiaonuo.pay.modular.mchapp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.ui.Model;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.mchapp.entity.MchApp;
import vip.xiaonuo.pay.modular.mchapp.param.MchAppParam;
import vip.xiaonuo.pay.modular.mchapp.service.MchAppService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商户应用表控制器
 *
 * @author abc
 * @date 2022-01-02 23:18:03
 */
@Controller
public class MchAppController {

    private String PATH_PREFIX = "pay/mchApp/";

    @Resource
    MchInfoService mchInfoService;
    @Resource
    private MchAppService mchAppService;

    /**
     * 商户应用表页面
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @GetMapping("/mchApp/index")
    public String index(String infoMchNo,Model model) {
        Boolean superAdmin = LoginContextHolder.me().isSuperAdmin();
        MchInfo mchInfo = new MchInfo();
        if(!superAdmin){
            mchInfo.setUserId(LoginContextHolder.me().getSysLoginUserId());
            mchInfo = mchInfoService.getOne(new QueryWrapper<MchInfo>(mchInfo));
            if(mchInfo != null){
                model.addAllAttributes(JSONUtil.toBean(JSONUtil.toJsonStr(mchInfo), Map.class));
            }
        }else{
            mchInfo.setMchNo("");
            model.addAllAttributes(JSONUtil.toBean(JSONUtil.toJsonStr(mchInfo), Map.class));
        }
        model.addAttribute("infoMchNo", StrUtil.isBlank(infoMchNo) ? "":infoMchNo);
        return PATH_PREFIX + "index.html";
    }

    /**
     * 商户应用表表单页面
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @GetMapping("/mchApp/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchApp/page")
    @BusinessLog(title = "商户应用表_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<MchApp> page(MchAppParam mchAppParam) {
        return mchAppService.page(mchAppParam);
    }

    /**
     * 添加商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchApp/add")
    @BusinessLog(title = "商户应用表_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(MchAppParam.add.class) MchAppParam mchAppParam) {
        mchAppService.add(mchAppParam);
        return new SuccessResponseData();
    }

    /**
     * 删除商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchApp/delete")
    @BusinessLog(title = "商户应用表_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(MchAppParam.delete.class) List<MchAppParam> mchAppParamList) {
        mchAppService.delete(mchAppParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchApp/edit")
    @BusinessLog(title = "商户应用表_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(MchAppParam.edit.class) MchAppParam mchAppParam) {
        mchAppService.edit(mchAppParam);
        return new SuccessResponseData();
    }

    /**
     * 查看商户应用表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchApp/detail")
    @BusinessLog(title = "商户应用表_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(MchAppParam.detail.class) MchAppParam mchAppParam) {
        return new SuccessResponseData(mchAppService.detail(mchAppParam));
    }

    /**
     * 商户应用表列表
     *
     * @author abc
     * @date 2022-01-02 23:18:03
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchApp/list")
    @BusinessLog(title = "商户应用表_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(MchAppParam mchAppParam) {
        return new SuccessResponseData(mchAppService.list(mchAppParam));
    }

}
