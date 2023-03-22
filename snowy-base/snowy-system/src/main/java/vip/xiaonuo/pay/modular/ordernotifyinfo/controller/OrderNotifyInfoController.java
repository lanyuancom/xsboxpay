
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
package vip.xiaonuo.pay.modular.ordernotifyinfo.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.ordernotifyinfo.entity.OrderNotifyInfo;
import vip.xiaonuo.pay.modular.ordernotifyinfo.param.OrderNotifyInfoParam;
import vip.xiaonuo.pay.modular.ordernotifyinfo.service.OrderNotifyInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 通知记录信息控制器
 *
 * @author abc
 * @date 2022-01-20 16:56:48
 */
@Controller
public class OrderNotifyInfoController {

    private String PATH_PREFIX = "pay/orderNotifyInfo/";

    @Resource
    private OrderNotifyInfoService orderNotifyInfoService;

    /**
     * 通知记录信息页面
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @Permission
    @GetMapping("/orderNotifyInfo/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 通知记录信息表单页面
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @GetMapping("/orderNotifyInfo/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @Permission
    @ResponseBody
    @GetMapping("/orderNotifyInfo/page")
    @BusinessLog(title = "通知记录信息_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<OrderNotifyInfo> page(OrderNotifyInfoParam orderNotifyInfoParam) {
        return orderNotifyInfoService.page(orderNotifyInfoParam);
    }

    /**
     * 添加通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
   /* @Permission
    @ResponseBody
    @PostMapping("/orderNotifyInfo/add")
    @BusinessLog(title = "通知记录信息_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(OrderNotifyInfoParam.add.class) OrderNotifyInfoParam orderNotifyInfoParam) {
        orderNotifyInfoService.add(orderNotifyInfoParam);
        return new SuccessResponseData();
    }*/

    /**
     * 删除通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
   /* @Permission
    @ResponseBody
    @PostMapping("/orderNotifyInfo/delete")
    @BusinessLog(title = "通知记录信息_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(OrderNotifyInfoParam.delete.class) List<OrderNotifyInfoParam> orderNotifyInfoParamList) {
        orderNotifyInfoService.delete(orderNotifyInfoParamList);
        return new SuccessResponseData();
    }*/

    /**
     * 编辑通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @Permission
    @ResponseBody
    @PostMapping("/orderNotifyInfo/edit")
    @BusinessLog(title = "通知记录信息_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(OrderNotifyInfoParam.edit.class) OrderNotifyInfoParam orderNotifyInfoParam) {
        orderNotifyInfoService.edit(orderNotifyInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 查看通知记录信息
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @Permission
    @ResponseBody
    @GetMapping("/orderNotifyInfo/detail")
    @BusinessLog(title = "通知记录信息_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(OrderNotifyInfoParam.detail.class) OrderNotifyInfoParam orderNotifyInfoParam) {
        return new SuccessResponseData(orderNotifyInfoService.detail(orderNotifyInfoParam));
    }

    /**
     * 通知记录信息列表
     *
     * @author abc
     * @date 2022-01-20 16:56:48
     */
    @Permission
    @ResponseBody
    @GetMapping("/orderNotifyInfo/list")
    @BusinessLog(title = "通知记录信息_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(OrderNotifyInfoParam orderNotifyInfoParam) {
        return new SuccessResponseData(orderNotifyInfoService.list(orderNotifyInfoParam));
    }

    @GetMapping("/orderNotifyInfo/again")
    public ResponseData again(OrderNotifyInfoParam orderNotifyInfoParam) {
        orderNotifyInfoService.again(orderNotifyInfoParam);
        return new SuccessResponseData();
    }

}
