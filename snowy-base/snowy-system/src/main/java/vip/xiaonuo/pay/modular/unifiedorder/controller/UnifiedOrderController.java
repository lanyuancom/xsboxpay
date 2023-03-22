
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
package vip.xiaonuo.pay.modular.unifiedorder.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.unifiedorder.entity.UnifiedOrder;
import vip.xiaonuo.pay.modular.unifiedorder.param.UnifiedOrderParam;
import vip.xiaonuo.pay.modular.unifiedorder.service.UnifiedOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 请求下单控制器
 *
 * @author abc
 * @date 2022-02-14 23:15:40
 */
@Controller
public class UnifiedOrderController {

    private String PATH_PREFIX = "pay/unifiedOrder/";

    @Resource
    private UnifiedOrderService unifiedOrderService;

    /**
     * 请求下单页面
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @GetMapping("/unifiedOrder/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 请求下单表单页面
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @GetMapping("/unifiedOrder/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @GetMapping("/unifiedOrder/page")
    @BusinessLog(title = "请求下单_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<UnifiedOrder> page(UnifiedOrderParam unifiedOrderParam) {
        return unifiedOrderService.page(unifiedOrderParam);
    }

    /**
     * 添加请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @PostMapping("/unifiedOrder/add")
    @BusinessLog(title = "请求下单_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(UnifiedOrderParam.add.class) UnifiedOrderParam unifiedOrderParam) {
        unifiedOrderService.add(unifiedOrderParam);
        return new SuccessResponseData();
    }

    /**
     * 删除请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @PostMapping("/unifiedOrder/delete")
    @BusinessLog(title = "请求下单_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(UnifiedOrderParam.delete.class) List<UnifiedOrderParam> unifiedOrderParamList) {
        unifiedOrderService.delete(unifiedOrderParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @PostMapping("/unifiedOrder/edit")
    @BusinessLog(title = "请求下单_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(UnifiedOrderParam.edit.class) UnifiedOrderParam unifiedOrderParam) {
        unifiedOrderService.edit(unifiedOrderParam);
        return new SuccessResponseData();
    }

    /**
     * 查看请求下单
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @GetMapping("/unifiedOrder/detail")
    @BusinessLog(title = "请求下单_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(UnifiedOrderParam.detail.class) UnifiedOrderParam unifiedOrderParam) {
        return new SuccessResponseData(unifiedOrderService.detail(unifiedOrderParam));
    }

    /**
     * 请求下单列表
     *
     * @author abc
     * @date 2022-02-14 23:15:40
     */
    @Permission
    @ResponseBody
    @GetMapping("/unifiedOrder/list")
    @BusinessLog(title = "请求下单_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(UnifiedOrderParam unifiedOrderParam) {
        return new SuccessResponseData(unifiedOrderService.list(unifiedOrderParam));
    }

}
