
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
package vip.xiaonuo.pay.modular.payinforecord.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ErrorResponseData;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.core.util.ResultBean;
import vip.xiaonuo.pay.modular.payinforecord.entity.PayInfoRecord;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoParam;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoRecordParam;
import vip.xiaonuo.pay.modular.payinforecord.service.PayInfoRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 打款记录控制器
 *
 * @author abc
 * @date 2022-03-02 22:43:26
 */
@Controller
public class PayInfoRecordController {

    private String PATH_PREFIX = "pay/payInfoRecord/";

    @Resource
    private PayInfoRecordService payInfoRecordService;
    /**
     * 查询打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @ResponseBody
    @PostMapping("/payInfoRecord/topay")
    public ResponseData topay(@RequestBody PayInfoParam payInfoParam) {
        ResultBean rb = payInfoRecordService.topay(payInfoParam);
        if(rb.getCode() == 200){
            return new SuccessResponseData();
        }else{
            return new ErrorResponseData(rb.getCode(),rb.getMsg());
        }

    }
    /**
     * 打款记录页面
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @GetMapping("/payInfoRecord/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 打款记录表单页面
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @GetMapping("/payInfoRecord/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @GetMapping("/payInfoRecord/page")
    @BusinessLog(title = "打款记录_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<PayInfoRecord> page(PayInfoRecordParam payInfoRecordParam) {
        return payInfoRecordService.page(payInfoRecordParam);
    }

    /**
     * 添加打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @PostMapping("/payInfoRecord/add")
    @BusinessLog(title = "打款记录_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(PayInfoRecordParam.add.class) PayInfoRecordParam payInfoRecordParam) {
        payInfoRecordService.add(payInfoRecordParam);
        return new SuccessResponseData();
    }

    /**
     * 删除打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @PostMapping("/payInfoRecord/delete")
    @BusinessLog(title = "打款记录_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(PayInfoRecordParam.delete.class) List<PayInfoRecordParam> payInfoRecordParamList) {
        payInfoRecordService.delete(payInfoRecordParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @PostMapping("/payInfoRecord/edit")
    @BusinessLog(title = "打款记录_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(PayInfoRecordParam.edit.class) PayInfoRecordParam payInfoRecordParam) {
        payInfoRecordService.edit(payInfoRecordParam);
        return new SuccessResponseData();
    }

    /**
     * 查看打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @GetMapping("/payInfoRecord/detail")
    @BusinessLog(title = "打款记录_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(PayInfoRecordParam.detail.class) PayInfoRecordParam payInfoRecordParam) {
        return new SuccessResponseData(payInfoRecordService.detail(payInfoRecordParam));
    }

    /**
     * 打款记录列表
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    @Permission
    @ResponseBody
    @GetMapping("/payInfoRecord/list")
    @BusinessLog(title = "打款记录_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(PayInfoRecordParam payInfoRecordParam) {
        return new SuccessResponseData(payInfoRecordService.list(payInfoRecordParam));
    }

}
