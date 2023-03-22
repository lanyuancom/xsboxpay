
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
package vip.xiaonuo.pay.modular.mchpiecesinfo.controller;

import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.annotion.Permission;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.pay.modular.mchpiecesinfo.entity.MchPiecesInfo;
import vip.xiaonuo.pay.modular.mchpiecesinfo.param.MchPiecesInfoParam;
import vip.xiaonuo.pay.modular.mchpiecesinfo.service.MchPiecesInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * 商户进件控制器
 *
 * @author abc
 * @date 2022-01-16 21:16:18
 */
@Controller
public class MchPiecesInfoController {

    private String PATH_PREFIX = "pay/mchPiecesInfo/";

    @Resource
    private MchPiecesInfoService mchPiecesInfoService;

    /**
     * 商户进件页面
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @GetMapping("/mchPiecesInfo/index")
    public String index() {
        return PATH_PREFIX + "index.html";
    }

    /**
     * 商户进件表单页面
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @GetMapping("/mchPiecesInfo/form")
    public String form() {
        return PATH_PREFIX + "form.html";
    }

    /**
     * 查询商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchPiecesInfo/page")
    @BusinessLog(title = "商户进件_查询", opType = LogAnnotionOpTypeEnum.QUERY)
    public PageResult<MchPiecesInfo> page(MchPiecesInfoParam mchPiecesInfoParam) {
        return mchPiecesInfoService.page(mchPiecesInfoParam);
    }

    /**
     * 添加商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchPiecesInfo/add")
    @BusinessLog(title = "商户进件_增加", opType = LogAnnotionOpTypeEnum.ADD)
    public ResponseData add(@RequestBody @Validated(MchPiecesInfoParam.add.class) MchPiecesInfoParam mchPiecesInfoParam) {
        mchPiecesInfoService.add(mchPiecesInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 删除商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchPiecesInfo/delete")
    @BusinessLog(title = "商户进件_删除", opType = LogAnnotionOpTypeEnum.DELETE)
    public ResponseData delete(@RequestBody @Validated(MchPiecesInfoParam.delete.class) List<MchPiecesInfoParam> mchPiecesInfoParamList) {
        mchPiecesInfoService.delete(mchPiecesInfoParamList);
        return new SuccessResponseData();
    }

    /**
     * 编辑商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @PostMapping("/mchPiecesInfo/edit")
    @BusinessLog(title = "商户进件_编辑", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData edit(@RequestBody @Validated(MchPiecesInfoParam.edit.class) MchPiecesInfoParam mchPiecesInfoParam) {
        mchPiecesInfoService.edit(mchPiecesInfoParam);
        return new SuccessResponseData();
    }

    @ResponseBody
    @PostMapping("/mchPiecesInfo/copy")
    @BusinessLog(title = "商户进件_复用", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData copy(@RequestBody MchPiecesInfoParam mchPiecesInfoParam) {
        mchPiecesInfoService.copy(mchPiecesInfoParam);
        return new SuccessResponseData();
    }

    /**
     * 查看商户进件
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchPiecesInfo/detail")
    @BusinessLog(title = "商户进件_查看", opType = LogAnnotionOpTypeEnum.DETAIL)
    public ResponseData detail(@Validated(MchPiecesInfoParam.detail.class) MchPiecesInfoParam mchPiecesInfoParam) {
        return new SuccessResponseData(mchPiecesInfoService.detail(mchPiecesInfoParam));
    }

    /**
     * 商户进件列表
     *
     * @author abc
     * @date 2022-01-16 21:16:18
     */
    @Permission
    @ResponseBody
    @GetMapping("/mchPiecesInfo/list")
    @BusinessLog(title = "商户进件_列表", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData list(MchPiecesInfoParam mchPiecesInfoParam) {
        return new SuccessResponseData(mchPiecesInfoService.list(mchPiecesInfoParam));
    }

    @ResponseBody
    @PostMapping("/mchPiecesInfo/findBankCode")
    public ResponseData findBankCode(String name) {
        return new SuccessResponseData(mchPiecesInfoService.findBankCode(name));
    }

    @ResponseBody
    @GetMapping("/mchPiecesInfo/piecesStatusRefresh")
    public ResponseData piecesStatusRefresh(String id, String subUserNo) {
        return new SuccessResponseData(mchPiecesInfoService.piecesStatusRefresh(id,subUserNo));
    }

    @ResponseBody
    @PostMapping("/mchPiecesInfo/update")
    @BusinessLog(title = "商户进件_更新", opType = LogAnnotionOpTypeEnum.EDIT)
    public ResponseData update(@RequestBody MchPiecesInfoParam mchPiecesInfoParam) {
        mchPiecesInfoService.update(mchPiecesInfoParam);
        return new SuccessResponseData();
    }
}
