
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
package vip.xiaonuo.pay.modular.mchpiecesinfo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.xiaonuo.core.pojo.base.entity.BaseEntity;

/**
 * 商户进件
 *
 * @author abc
 * @date 2022-01-16 21:16:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("xs_mch_pieces_info")
public class MchPiecesInfo extends BaseEntity {

    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 子商户商编 进件之后返回
     */
    private String subUserNo;
    /**
     * 类型
     */
    private String piecesType;
    /**
     * 经营场景
     */
    private String scenario;

    /**
     * 入网类型  PERSON 个人  CORP_CODE 公司
     */
    private String netType;

    /**
     * 法人姓名
     */
    private String legarName;

    /**
     * 个人就是 法人是身份证  企业就是 营业执照号
     */
    private String legarCard;

    /**
     * 法人手机号
     */
    private String legarPhone;

    /**
     * 身份证正面URL
     */
    private String frontCard;

    /**
     * 身份证反面URL
     */
    private String backCard;

    /**
     * 身份证 开始日期 YYYY-MM-dd
     */
    private String cardStart;

    /**
     * 身份证 结束日期 YYYY-MM-dd
     */
    private String cardEnd;
    /**
     * 1、主体类型为企业时，需要填写。其他主体类型，无需上传。
     * 2、请按照身份证住址填写，如广东省深圳市南山区xx路xx号xx室
     */
    private String idCardAddress;

    /**
     * 联系姓名
     */
    private String contactName;

    /**
     * 联系手机号
     */
    private String contactPhone;

    /**
     * 联系邮箱，各子商户邮箱地址唯一
     */
    private String contactEmail;

    /**
     * 客服电话
     */
    private String servePhone;

    /**
     * 经营类别  参考附件 经营类别编码
     */
    private String levelOneNo;

    /**
     * 行业类别  参考附件 行业类别编码
     */
    private String levelTwoNo;

    /**
     * 商户全称
     */
    private String businessFullName;

    /**
     * 商户简称
     */
    private String businessShortName;

    /**
     * 执照编号
     */
    private String businessNo;

    /**
     * 营业执照正面
     */
    private String businessFront;

    /**
     * 开户许可正面
     */
    private String businessAccountLicense;

    /**
     * 开户许可编号
     */
    private String businessAccountLicenseNo;

    /**
     * 营业期限证件是否长期有效  YES NO
     */
    private String orgcodeLong;

    /**
     * 营业期限  开始日期 YYYY-MM-dd
     */
    private String orgcodeStart;

    /**
     * 营业期限  结束日期 YYYY-MM-dd
     */
    private String orgcodeEnd;

    /**
     * 省
     */
    private String subUserProvince;

    /**
     * 市
     */
    private String subUserCity;

    /**
     * 区
     */
    private String subUserDistrict;

    /**
     * 省代码
     */
    private String subUserProvinceCode;

    /**
     * 市代码
     */
    private String subUserCityCode;

    /**
     * 区代码
     */
    private String subUserDistrictCode;

    /**
     * 经营地址详细地址
     */
    private String subUserAddress;

    /**
     * 结算账户类型 对私：PRIVATE 对公：PUBLIC
     */
    private String accountType;

    /**
     * 银行银行户名
     */
    private String bankAccount;

    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行支行全名称
     */
    private String accountBank;
    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 开户省份编码 参考附件 省市区关系及编码表
     */
    private String bankProvinceCode;

    /**
     * 开户城市编码 参考附件 省市区关系及编码表
     */
    private String bankCityCode;

    /**
     * 开户省份
     */
    private String bankProvince;

    /**
     * 开户城市
     */
    private String bankCity;

    /**
     * 开户区
     */
    private String bankDistrict;


    /**
     * 开户区
     */
    private String bankDistrictCode;

    /**
     * 结算方式 自动定期结算：AUTO_TIMING 自助结算：SELF_SERVICE
     */
    private String settType;

    /**
     * 结算周期 自然日隔天结算: D1  工作日隔天结算: T1
     */
    private String settCycle;

    /**
     * 收款上限
     */
    private String maxAmount;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;


    /**
     * 草稿：DRAFT , 待审核：PREAUDIT , 审核中：AUDITING , 审核通过：AUDITED, 审核未通过：UNAUDIT
     */
    private String piecesStatus;

    private String piecesMsg;

    private Long yesToDayAmount;
    private Long toDayAmount;
    private Long countAmount;

}
