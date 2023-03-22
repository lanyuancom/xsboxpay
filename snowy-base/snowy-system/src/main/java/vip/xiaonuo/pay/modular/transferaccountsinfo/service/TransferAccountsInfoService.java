
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
package vip.xiaonuo.pay.modular.transferaccountsinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.pay.modular.transferaccountsinfo.entity.TransferAccountsInfo;
import vip.xiaonuo.pay.modular.transferaccountsinfo.param.TransferAccountsInfoParam;
import java.util.List;
import java.util.Set;

/**
 * 转账账号管理service接口
 *
 * @author kk -- (mmm333zzz520@163.com)
 * @date 2022-09-23 11:24:12
 */
public interface TransferAccountsInfoService extends IService<TransferAccountsInfo> {

    /**
     * 查询转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    PageResult<TransferAccountsInfo> page(TransferAccountsInfoParam transferAccountsInfoParam);

    /**
     * 转账账号管理列表
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    List<TransferAccountsInfo> list(TransferAccountsInfoParam transferAccountsInfoParam);

    /**
     * 添加转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    void add(TransferAccountsInfoParam transferAccountsInfoParam);

    /**
     * 删除转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    void delete(List<TransferAccountsInfoParam> transferAccountsInfoParamList);

    /**
     * 编辑转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
    void edit(TransferAccountsInfoParam transferAccountsInfoParam);

    /**
     * 查看转账账号管理
     *
     * @author kk -- (mmm333zzz520@163.com)
     * @date 2022-09-23 11:24:12
     */
     TransferAccountsInfo detail(TransferAccountsInfoParam transferAccountsInfoParam);


    Set<String> taglist(TransferAccountsInfoParam transferAccountsInfoParam);
}
