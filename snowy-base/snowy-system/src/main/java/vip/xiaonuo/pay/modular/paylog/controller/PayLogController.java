
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
package vip.xiaonuo.pay.modular.paylog.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.paylog.entity.PayLog;
import vip.xiaonuo.pay.modular.paylog.param.PayLogParam;
import vip.xiaonuo.pay.modular.paylog.service.PayLogService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 订单支付记录控制器
 *
 * @author abc
 * @date 2022-01-05 23:07:24
 */
@Controller
public class PayLogController {

    private String PATH_PREFIX = "pay/payLog/";

    @Resource
    private PayLogService payLogService;

    /**
     * 订单支付记录页面
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @GetMapping("/payLog/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 订单支付记录表单页面
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @GetMapping("/payLog/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @GetMapping("/payLog/page")
    @BusinessLog(title = "订单支付记录_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<PayLog> page(PayLogParam payLogParam) {
        return payLogService.page(payLogParam);
    }

    /**
     * 添加订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @PostMapping("/payLog/add")
    @BusinessLog(title = "订单支付记录_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(PayLogParam.add.class) PayLogParam payLogParam) {
        payLogService.add(payLogParam);
        return new SuccessResponseData();
    }

    /**
     * 删除订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @PostMapping("/payLog/delete")
    @BusinessLog(title = "订单支付记录_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(PayLogParam.delete.class) List<PayLogParam> payLogParamList) {
        payLogService.delete(payLogParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @PostMapping("/payLog/edit")
    @BusinessLog(title = "订单支付记录_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(PayLogParam.edit.class) PayLogParam payLogParam) {
        payLogService.edit(payLogParam);
        return new SuccessResponseData();
    }

    /**
     * 查看订单支付记录
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @GetMapping("/payLog/detail")
    @BusinessLog(title = "订单支付记录_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(PayLogParam.detail.class) PayLogParam payLogParam) {
        return new SuccessResponseData(payLogService.detail(payLogParam));
    }

    /**
     * 订单支付记录列表
     *
     * @author abc
     * @date 2022-01-05 23:07:24
     */
    @Permission
    @ResponseBody
    @GetMapping("/payLog/list")
    @BusinessLog(title = "订单支付记录_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(PayLogParam payLogParam) {
        return new SuccessResponseData(payLogService.list(payLogParam));
    }

}
