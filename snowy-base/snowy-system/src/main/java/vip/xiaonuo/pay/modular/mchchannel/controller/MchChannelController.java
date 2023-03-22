
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
package vip.xiaonuo.pay.modular.mchchannel.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.mchchannel.entity.MchChannel;
import vip.xiaonuo.pay.modular.mchchannel.param.MchChannelParam;
import vip.xiaonuo.pay.modular.mchchannel.service.MchChannelService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 商户开通的渠道控制器
 *
 * @author abc
 * @date 2022-03-09 14:49:50
 */
@Controller
public class MchChannelController {

    private String PATH_PREFIX = "mchChannel/";

    @Resource
    private MchChannelService mchChannelService;

    /**
     * 商户开通的渠道页面
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @GetMapping("/mchChannel/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 商户开通的渠道表单页面
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @GetMapping("/mchChannel/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchChannel/page")
    @BusinessLog(title = "商户开通的渠道_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<MchChannel> page(MchChannelParam mchChannelParam) {
        return mchChannelService.page(mchChannelParam);
    }

    /**
     * 添加商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchChannel/add")
    @BusinessLog(title = "商户开通的渠道_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(MchChannelParam.add.class) MchChannelParam mchChannelParam) {
        mchChannelService.add(mchChannelParam);
        return new SuccessResponseData();
    }

    /**
     * 删除商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchChannel/delete")
    @BusinessLog(title = "商户开通的渠道_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(MchChannelParam.delete.class) List<MchChannelParam> mchChannelParamList) {
        mchChannelService.delete(mchChannelParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchChannel/edit")
    @BusinessLog(title = "商户开通的渠道_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(MchChannelParam.edit.class) MchChannelParam mchChannelParam) {
        mchChannelService.edit(mchChannelParam);
        return new SuccessResponseData();
    }

    /**
     * 查看商户开通的渠道
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchChannel/detail")
    @BusinessLog(title = "商户开通的渠道_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(MchChannelParam.detail.class) MchChannelParam mchChannelParam) {
        return new SuccessResponseData(mchChannelService.detail(mchChannelParam));
    }

    /**
     * 商户开通的渠道列表
     *
     * @author abc
     * @date 2022-03-09 14:49:50
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchChannel/list")
    @BusinessLog(title = "商户开通的渠道_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(MchChannelParam mchChannelParam) {
        return new SuccessResponseData(mchChannelService.list(mchChannelParam));
    }

    @ResponseBody
    @GetMapping("/mchChannel/findChannel")
    public ResponseData findChannel(MchChannelParam mchChannelParam) {
        return new SuccessResponseData(mchChannelService.findChannel(mchChannelParam));
    }

    @Permission
    @ResponseBody
    @PostMapping("/mchChannel/distribution")
    @BusinessLog(title = "支付通道_分配商户", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData distribution(@RequestBody MchChannelParam mchChannelParam) {
        mchChannelService.distribution(mchChannelParam);
        return new SuccessResponseData();
    }
}
