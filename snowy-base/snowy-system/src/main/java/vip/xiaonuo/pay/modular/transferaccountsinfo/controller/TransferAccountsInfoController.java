
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
package vip.xiaonuo.pay.modular.transferaccountsinfo.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.transferaccountsinfo.entity.TransferAccountsInfo;
import vip.xiaonuo.pay.modular.transferaccountsinfo.param.TransferAccountsInfoParam;
import vip.xiaonuo.pay.modular.transferaccountsinfo.service.TransferAccountsInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 转账账号管理控制器
 *
 * @author kk -- (mmm333zzz520@163.com)
 * @date 2022-09-23 11:24:12
 */
@Controller
public class TransferAccountsInfoController {

    private String PATH_PREFIX = "pay/transferAccountsInfo/";

    @Resource
    private TransferAccountsInfoService transferAccountsInfoService;

    /**
     * 转账账号管理页面
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @GetMapping("/transferAccountsInfo/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 转账账号管理表单页面
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @GetMapping("/transferAccountsInfo/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @GetMapping("/transferAccountsInfo/page")
    @BusinessLog(title = "转账账号管理_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<TransferAccountsInfo> page(TransferAccountsInfoParam transferAccountsInfoParam) {
        return transferAccountsInfoService.page(transferAccountsInfoParam);
    }

    /**
     * 添加转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @PostMapping("/transferAccountsInfo/add")
    @BusinessLog(title = "转账账号管理_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(TransferAccountsInfoParam.add.class) TransferAccountsInfoParam transferAccountsInfoParam) {
        transferAccountsInfoService.add(transferAccountsInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 删除转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @PostMapping("/transferAccountsInfo/delete")
    @BusinessLog(title = "转账账号管理_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(TransferAccountsInfoParam.delete.class) List<TransferAccountsInfoParam> transferAccountsInfoParamList) {
        transferAccountsInfoService.delete(transferAccountsInfoParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @PostMapping("/transferAccountsInfo/edit")
    @BusinessLog(title = "转账账号管理_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(TransferAccountsInfoParam.edit.class) TransferAccountsInfoParam transferAccountsInfoParam) {
        transferAccountsInfoService.edit(transferAccountsInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 查看转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @GetMapping("/transferAccountsInfo/detail")
    @BusinessLog(title = "转账账号管理_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(TransferAccountsInfoParam.detail.class) TransferAccountsInfoParam transferAccountsInfoParam) {
        return new SuccessResponseData(transferAccountsInfoService.detail(transferAccountsInfoParam));
    }

    /**
     * 转账账号管理列表
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    @Permission
    @ResponseBody
    @GetMapping("/transferAccountsInfo/list")
    @BusinessLog(title = "转账账号管理_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(TransferAccountsInfoParam transferAccountsInfoParam) {
        return new SuccessResponseData(transferAccountsInfoService.list(transferAccountsInfoParam));
    }

    @ResponseBody
    @GetMapping("/transferAccountsInfo/taglist")
    @BusinessLog(title = "转账账号管理_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData taglist(TransferAccountsInfoParam transferAccountsInfoParam) {
        return new SuccessResponseData(transferAccountsInfoService.taglist(transferAccountsInfoParam));
    }

}
