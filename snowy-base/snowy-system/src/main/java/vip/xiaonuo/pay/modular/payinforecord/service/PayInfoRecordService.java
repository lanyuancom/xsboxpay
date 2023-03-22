
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
package vip.xiaonuo.pay.modular.payinforecord.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.ResultBean;
import vip.xiaonuo.pay.modular.payinforecord.entity.PayInfoRecord;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoParam;
import vip.xiaonuo.pay.modular.payinforecord.param.PayInfoRecordParam;
import java.util.List;

/**
 * 打款记录service接口
 *
 * @author abc
 * @date 2022-03-02 22:43:26
 */
public interface PayInfoRecordService extends IService<PayInfoRecord> {

    /**
     * 查询打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    PageResult<PayInfoRecord> page(PayInfoRecordParam payInfoRecordParam);

    /**
     * 打款记录列表
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    List<PayInfoRecord> list(PayInfoRecordParam payInfoRecordParam);

    /**
     * 添加打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    void add(PayInfoRecordParam payInfoRecordParam);

    /**
     * 删除打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    void delete(List<PayInfoRecordParam> payInfoRecordParamList);

    /**
     * 编辑打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
    void edit(PayInfoRecordParam payInfoRecordParam);

    /**
     * 查看打款记录
     *
     * @author abc
     * @date 2022-03-02 22:43:26
     */
     PayInfoRecord detail(PayInfoRecordParam payInfoRecordParam);

    ResultBean topay(PayInfoParam payInfoParam);
}
