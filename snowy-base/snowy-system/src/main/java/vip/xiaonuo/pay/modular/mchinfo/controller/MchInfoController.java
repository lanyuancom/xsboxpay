
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
package vip.xiaonuo.pay.modular.mchinfo.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.ui.Model;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.mchinfo.entity.MchInfo;
import vip.xiaonuo.pay.modular.mchinfo.param.MchInfoParam;
import vip.xiaonuo.pay.modular.mchinfo.service.MchInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.xiaonuo.pay.modular.paychannel.entity.PayChannel;
import vip.xiaonuo.pay.modular.paychannel.service.PayChannelService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商户信息控制器
 *
 * @author abc
 * @date 2022-01-01 23:22:45
 */
@Controller
public class MchInfoController {

    private String PATH_PREFIX = "pay/mchInfo/";

    @Resource
    private MchInfoService mchInfoService;

    @Resource
    private PayChannelService payChannelService;

    /**
     * 商户信息页面
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @GetMapping("/mchInfo/index")
    public String index() {
       return PATH_PREFIX + "index.html";
    }

    /**
     * 商户信息表单页面
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @GetMapping("/mchInfo/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    @GetMapping("/mchInfo/info")
    public String info(Model model) {

        List<PayChannel> payChannel = payChannelService.list();
        //如果所以通道都不行。才认为难道不可用。告知商户
        model.addAttribute("channelStatus",1);//默认异常
        if(CollUtil.isNotEmpty(payChannel)){
            payChannel=payChannel.stream().filter(p -> p.getStatus() == 0).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(payChannel)){
                model.addAttribute("channelStatus",0);
            }
        }
        model.addAttribute("mch",mchInfoService.info());
        return PATH_PREFIX + "info.html";
    }
    /**
     * 查询商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchInfo/page")
    @BusinessLog(title = "商户信息_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<MchInfo> page(MchInfoParam mchInfoParam) {
        return mchInfoService.page(mchInfoParam);
    }

    /**
     * 添加商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchInfo/add")
    @BusinessLog(title = "商户信息_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(MchInfoParam.add.class) MchInfoParam mchInfoParam) {
        mchInfoService.add(mchInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 删除商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchInfo/delete")
    @BusinessLog(title = "商户信息_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(MchInfoParam.delete.class) List<MchInfoParam> mchInfoParamList) {
        mchInfoService.delete(mchInfoParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchInfo/edit")
    @BusinessLog(title = "商户信息_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(MchInfoParam.edit.class) MchInfoParam mchInfoParam) {
        mchInfoService.edit(mchInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 查看商户信息
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchInfo/detail")
    @BusinessLog(title = "商户信息_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(MchInfoParam.detail.class) MchInfoParam mchInfoParam) {
        return new SuccessResponseData(mchInfoService.detail(mchInfoParam));
    }

    /**
     * 商户信息列表
     *
     * @author abc
     * @date 2022-01-01 23:22:45
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchInfo/list")
    @BusinessLog(title = "商户信息_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(MchInfoParam mchInfoParam) {
        return new SuccessResponseData(mchInfoService.list(mchInfoParam));
    }

    @ResponseBody
    @PostMapping("/mchInfo/dayTotal")
    public ResponseData dayTotal(MchInfoParam mchInfoParam) {
        return new SuccessResponseData(mchInfoService.dayTotal(mchInfoParam));
    }

}
