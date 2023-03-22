
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
package vip.xiaonuo.pay.modular.payappinfo.entity;

import com.baomidou.mybatisplus.annotation.*;
import vip.xiaonuo.core.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.*;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 转账应用管理
 *
 * @author abc
 * @date 2022-03-03 10:37:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("xs_pay_app_info")
public class PayAppInfo extends BaseEntity {

    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 支付宝账号
     */
    private String account;

    /**
     * 支付宝姓名
     */
    private String name;

    /**
     * appid
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用私钥
     */
    private String privateKey;

    /**
     * 应用支付宝公钥
     */
    private String publicKey;

    /**
     * 根证书路径
     */
    private String rootCertPath;

    /**
     * 支付宝公钥证书路径
     */
    private String alipayPublicCertPath;

    /**
     * 应用公钥证书路径
     */
    private String appPublicCertPath;

    /**
     * 备注
     */
    private String remark;

    /**
     * 应用状态: （字典 0正常 1停用 2删除）
     */
    private Integer status;

}
