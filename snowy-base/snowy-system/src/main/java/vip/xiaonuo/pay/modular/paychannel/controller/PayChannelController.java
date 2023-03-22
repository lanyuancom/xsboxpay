
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
package vip.xiaonuo.pay.modular.paychannel.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.paychannel.entity.PayChannel;
import vip.xiaonuo.pay.modular.paychannel.param.PayChannelParam;
import vip.xiaonuo.pay.modular.paychannel.service.PayChannelService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 支付通道控制器
 *
 * @author abc
 * @date 2022-01-06 14:59:46
 */
@Controller
public class PayChannelController {

    private String PATH_PREFIX = "pay/payChannel/";

    @Resource
    private PayChannelService payChannelService;

    /**
     * 支付通道页面
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @GetMapping("/payChannel/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }


    /**
     * 上传文件
     *
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    @ResponseBody
    @PostMapping("/payChannel/upload")
    public ResponseData upload(@RequestPart("file") MultipartFile file) {
        return new SuccessResponseData(payChannelService.upload(file));
    }
    /**
     * 支付通道表单页面
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @GetMapping("/payChannel/form")
    public String form(Model model) {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/payChannel/page")
    @BusinessLog(title = "支付通道_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<PayChannel> page(PayChannelParam payChannelParam) {
        return payChannelService.page(payChannelParam);
    }

    /**
     * 添加支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @PostMapping("/payChannel/add")
    @BusinessLog(title = "支付通道_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody Map<String,Object> jsonMap) {
        String pa = JSONUtil.toJsonStr(jsonMap);
        PayChannelParam payChannelParam = JSONUtil.toBean(pa,PayChannelParam.class);
        payChannelParam.setIfParams(pa);
        payChannelService.add(payChannelParam);
        return new SuccessResponseData();
    }

    /**
     * 删除支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @PostMapping("/payChannel/delete")
    @BusinessLog(title = "支付通道_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(PayChannelParam.delete.class) List<PayChannelParam> payChannelParamList) {
        payChannelService.delete(payChannelParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @PostMapping("/payChannel/edit")
    @BusinessLog(title = "支付通道_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody Map<String,Object> jsonMap) {
        String pa = JSONUtil.toJsonStr(jsonMap);
        PayChannelParam payChannelParam = JSONUtil.toBean(pa,PayChannelParam.class);
        payChannelParam.setIfParams(pa);
        payChannelService.edit(payChannelParam);
        return new SuccessResponseData();
    }

    /**
     * 查看支付通道
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/payChannel/detail")
    @BusinessLog(title = "支付通道_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(PayChannelParam.detail.class) PayChannelParam payChannelParam) {
        return new SuccessResponseData(payChannelService.detail(payChannelParam));
    }

    /**
     * 支付通道列表
     *
     * @author abc
     * @date 2022-01-06 14:59:46
     */
    @Permission
    @ResponseBody
    @GetMapping("/payChannel/list")
    @BusinessLog(title = "支付通道_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(PayChannelParam payChannelParam) {
        return new SuccessResponseData(payChannelService.list(payChannelParam));
    }

    @GetMapping("/payChannel/distribution_index")
    public String distribution_index() {
        return PATH_PREFIX + "distribution_index.html";
    }
}
