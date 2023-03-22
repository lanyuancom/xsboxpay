
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
package vip.xiaonuo.pay.modular.mchinfo.param;

import vip.xiaonuo.core.pojo.base.param.BaseParam;
import lombok.Data;
import vip.xiaonuo.pay.modular.mchchannel.entity.MchChannel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
* 商户信息参数类
 *
 * @author abc
 * @date 2022-01-01 23:22:45
*/
@Data
public class MchInfoParam extends BaseParam {

    /**
     * 
     */
    @NotNull(message = "不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class})
    private Long id;

    /**
     * 商户号
     */
    @NotBlank(message = "商户号不能为空，请检查mchNo参数", groups = {add.class, edit.class})
    private String mchNo;

    /**
     * 商户名称
     */
    @NotBlank(message = "商户名称不能为空，请检查mchName参数", groups = {add.class, edit.class})
    private String mchName;

    /**
     * 商户简称
     */
    @NotBlank(message = "商户简称不能为空，请检查mchShortName参数", groups = {add.class, edit.class})
    private String mchShortName;

    /**
     * 类型: 1-普通商户, 2-特约商户(服务商模式)
     */
    private Integer type;

    /**
     * 服务商号
     */
    private String isvNo;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系人手机号
     */
    private String contactTel;

    /**
     * 联系人邮箱
     */
    private String contactEmail;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 商户备注
     */
    @NotBlank(message = "商户备注不能为空，请检查remark参数", groups = {add.class, edit.class})
    private String remark;

    /**
     * 初始用户ID（创建商户时，允许商户登录的用户）
     */
    private Long userId;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = {add.class, edit.class})
    private Integer status;

    private List<MchChannel> channels;



    /**
     * 开始时间
     * */
    private String sTime;
    /**
     * 结束时间
     * */
    private String eTime;

}
