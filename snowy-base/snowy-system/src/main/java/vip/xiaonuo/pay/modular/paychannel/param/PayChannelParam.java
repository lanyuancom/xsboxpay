
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
package vip.xiaonuo.pay.modular.paychannel.param;

import vip.xiaonuo.core.pojo.base.param.BaseParam;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
* 支付通道参数类
 *
 * @author abc
 * @date 2022-01-06 14:59:46
*/
@Data
public class PayChannelParam extends BaseParam {

    /**
     * 
     */
    @NotNull(message = "不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class})
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空，请检查name参数", groups = {add.class, edit.class})
    private String name;
    /**
     * 微信支付  0 不支持 1 支持
     */
    private boolean wxPay;
    /**
     * 支付宝支付  0 不支持 1 支持
     */
    private boolean aliPay;

    private boolean wxService;
    /**
     * 通道编码
     */
    @NotBlank(message = "通道编码不能为空，请检查channelCode参数", groups = {add.class, edit.class})
    private String channelCode;
    /**
     * 通道标识
     */
    @NotBlank(message = "通道标识不能为空，请检查channelNo参数", groups = {add.class, edit.class})
    private String channelNo;

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空，请检查mchNo参数", groups = {add.class, edit.class})
    private String mchNo;

    /**
     * 公钥
     */
    @NotBlank(message = "公钥不能为空，请检查publicKey参数", groups = {add.class, edit.class})
    private String publicKey;

    /**
     * 私钥
     */
    @NotBlank(message = "私钥不能为空，请检查privateKey参数", groups = {add.class, edit.class})
    private String privateKey;

    /**
     * 应用状态: （字典 0正常 1停用 2删除）
     */
    private Integer status;

    /**
     * 接口配置参数,json字符串
     */
    private String ifParams;

}
