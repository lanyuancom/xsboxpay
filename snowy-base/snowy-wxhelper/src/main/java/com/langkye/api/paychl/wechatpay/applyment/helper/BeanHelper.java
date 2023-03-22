package com.langkye.api.paychl.wechatpay.applyment.helper;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.langkye.api.paychl.wechatpay.applyment.bean.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 转换bean 示例
 * @author langkye
 */
public class BeanHelper {
    private static final Log logger = Log.get();

    public static void main(String[] args) {
        /**
         * 示例 bean
         */
        Root root = toConvert(null);
        String rootJson = JSONObject.toJSONString(root);
        logger.warn("生成的商户信息：" + rootJson);

        /**
         * 将对象的驼峰字段转换为下划线Json字符串
         */
        SerializeConfig config = new SerializeConfig();
        config.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        String json = JSON.toJSONString(root, config);

        System.out.println("json = " + json);
    }


    public static Root toConvert(Map<String,Object> map) {
        Root root = new Root();

        /**
         * 业务申请编号	business_code	string[1,128]	是	body
         * 1、服务商自定义的唯一编号。
         * 2、每个编号对应一个申请单，每个申请单审核通过后会生成一个微信支付商户号。
         * 3、若申请单被驳回，可填写相同的“业务申请编号”，即可覆盖修改原申请单信息。
         * 示例值：APPLYMENT_00000000001
         */
        //String businessCode = "APPLYMENT_" + DateUtil.format(new Date(), "yyyyMMddhhmmss");
        String businessCode = map.get("id")+"";
        root.setBusinessCode(businessCode);

        /**
         * 超级管理员信息	contact_info	object	是	body 超级管理员需在开户后进行签约，并接收日常重要管理信息和进行资金操作，请确定其为商户法定代表人或负责人。
         */
        ContactInfo contactInfo = defaultContactInfo(map);
        root.setContactInfo(contactInfo);

        /**
         * 主体资料	subject_info	object	是	body 请填写商家的营业执照/登记证书、经营者/法人的证件等信息。
         */
        SubjectInfo subjectInfo = defaultSubjectInfo(map);
        root.setSubjectInfo(subjectInfo);

        /**
         * 经营资料	business_info	object	是	body 请填写商家的经营业务信息、售卖商品/提供服务场景信息。
         */
        BusinessInfo businessInfo = defaultBusinessInfo(map);
        root.setBusinessInfo(businessInfo);

        /**
         * 结算规则	settlement_info	object	是	body 请填写商家的结算费率规则、特殊资质等信息。
         */
        SettlementInfo settlementInfo = defaultSettlementInfo(map);
        root.setSettlementInfo(settlementInfo);

        /**
         * 结算银行账户	bank_account_info	object	条件选填	body
         *      1、请填写商家提现收款的银行账户信息。
         *      2、若结算规则id为“719、721、716、717、730、739、727、738、726”，可选填结算账户。
         */
        BankAccountInfo bankAccountInfo = defaultBankAccountInfo(map);
        root.setBankAccountInfo(bankAccountInfo);


        /**
         * 补充材料	addition_info	object	否	body 根据实际审核情况，额外要求商家提供指定的补充资料。
         */
        AdditionInfo additionInfo = defaultAdditionInfo();
        //root.setAdditionInfo(additionInfo);

        return root;
    }

    public static ModifySettlement convertModifySettlement(){
        final ModifySettlement settlement = new ModifySettlement();
        settlement.setSubMchid("1511101111");
        settlement.setAccountType("ACCOUNT_TYPE_BUSINESS");
        settlement.setAccountBank("工商银行");
        settlement.setBankAddressCode("110000");
        settlement.setBankName("施秉县农村信用合作联社城关信用社");
        settlement.setBankBranchId("402713354941");
        settlement.setAccountNumber("6217888899923334599");
        return settlement;
    }

    /**
     * 补充材料	addition_info	object	否	body 根据实际审核情况，额外要求商家提供指定的补充资料。
     */
    private static AdditionInfo defaultAdditionInfo() {
        AdditionInfo additionInfo = new AdditionInfo();
        //法人开户承诺函	legal_person_commitment	string	否
        //      1、请上传法定代表人或负责人亲笔签署的开户承诺函扫描件（下载模板）。亲笔签名承诺函内容清晰可见，不得有涂污，破损，字迹不清晰现象。
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值：47ZC6GC-vnrIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
        additionInfo.setLegalPersonCommitment("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //法人开户意愿视频	legal_person_video	string	否
        //      1、建议法人按如下话术录制“法人开户意愿视频”: 我是#公司全称#的法定代表人（或负责人），特此证明本公司申请的商户号为我司真实意愿开立且用于XX业务（或XX服务）。我司现有业务符合法律法规及腾讯的相关规定。
        //      2、支持上传20M内的视频，格式可为avi、wmv、mpeg、mp4、mov、mkv、flv、f4v、m4v、rmvb。
        //      3、请填写通过《视频上传API》预先上传视频生成好的MediaID。
        //  示例值：47ZC6GC-NIO4kqg05InE4d2I6_H7I4
        additionInfo.setLegalPersonVideo("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //补充材料	business_addition_pics	array	否
        //      1、根据驳回要求提供额外信息，如：
        //          （1）业务模式不清晰时，需详细描述支付场景或提供相关材料（如业务说明/门店照/ 手持证件照等）；
        //          （2）特殊业务要求提供相关的协议材料等；
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        List<String> pics = new ArrayList<>();
        pics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        additionInfo.setBusinessAdditionPics(pics);

        //补充说明	business_addition_msg	string[1,512]	否	根据驳回要求提供额外信息，如：业务模式不清晰时，请详细描述支付场景。
        //  示例值：特殊情况，说明原因
        additionInfo.setBusinessAdditionMsg("说明原因");

        return additionInfo;
    }

    /**
     * 结算银行账户	bank_account_info	object	条件选填	body
     * 1、请填写商家提现收款的银行账户信息。
     * 2、若结算规则id为“719、721、716、717、730、739、727、738、726”，可选填结算账户。
     */
    private static BankAccountInfo defaultBankAccountInfo(Map<String,Object> map) {
        BankAccountInfo bankAccountInfo = new BankAccountInfo();
        //账户类型	bank_account_type	string[1,27]	是
        //      1、若主体为企业/党政、机关及事业单位/其他组织，可填写：对公银行账户。
        //      2、若主体为个体户，可选择填写：对公银行账户或经营者个人银行卡。
        //  枚举值：
        //      BANK_ACCOUNT_TYPE_CORPORATE：对公银行账户
        //      BANK_ACCOUNT_TYPE_PERSONAL：经营者个人银行卡
        //  示例值：BANK_ACCOUNT_TYPE_CORPORATE
        bankAccountInfo.setBankAccountType("BANK_ACCOUNT_TYPE_CORPORATE");

        //开户名称	account_name	string[1,2048]	是
        //      1、选择“经营者个人银行卡”时，开户名称必须与“经营者证件姓名”一致。
        //      2、选择“对公银行账户”时，开户名称必须与营业执照/登记证书的“商户名称”一致。
        //      3、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：AOZdYGISxo4y4+kyblK2FCCqQJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==
        //bankAccountInfo.setAccountName("AOZdYGISxo4y4+kyblK2FCCqQJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==");
        bankAccountInfo.setAccountName(map.get("bankAccount")+"");

        //开户银行	account_bank	string[1,128]	是	开户银行，详细参见《开户银行对照表》。
        //  示例值：工商银行
        bankAccountInfo.setAccountBank(map.get("accountBank")+"");

        //开户银行省市编码	bank_address_code	string[1,128]	是	至少精确到市，详细参见《省市区编号对照表》。
        //  示例值：110000
        bankAccountInfo.setBankAddressCode(map.get("bankCityCode")+"");

        //开户银行联行号	bank_branch_id	string[1,128]	二选一
        //      1、17家直连银行无需填写，如为其他银行，则开户银行全称（含支行）和开户银行联行号二选一。
        //      2、详细参见《开户银行全称（含支行）对照表》。
        //  示例值：402713354941
        //bankAccountInfo.setBankBranchId("");

        //开户银行全称（含支行]	bank_name	string[1,128]
        //      1、17家直连银行无需填写，如为其他银行，则开户银行全称（含支行）和 开户银行联行号二选一。
        //      2、需填写银行全称，如"深圳农村商业银行XXX支行"，详细参见《开户银行全称（含支行）对照表》。
        //  示例值：施秉县农村信用合作联社城关信用社
        if(!"null".equals(map.get("bankName")+"") && !"".equals(map.get("bankName")+"")){
            bankAccountInfo.setBankName(map.get("bankName")+"");
        }

        //银行账号	account_number	string[1,2048]	是
        //      1、数字，长度遵循系统支持的卡号长度要求表。
        //      2、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：d+xT+MQCvrLHUVDWC1PHN4C7Rsu3WL8sPndjXTd75kPkyjqnoMRrEEaYQE8ZRGYoeorwC+w==
        //bankAccountInfo.setAccountNumber("d+xT+MQCvrLHUVDWC1PHN4C7Rsu3WL8sPndjXTd75kPkyjqnoMRrEEaYQE8ZRGYoeorwC+w==");
        bankAccountInfo.setAccountNumber(map.get("bankNo")+"");

        return bankAccountInfo;
    }

    /**
     * 结算规则	settlement_info	object	是	body 请填写商家的结算费率规则、特殊资质等信息。
     *
     * 《费率结算规则对照表》https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-7
     */
    private static SettlementInfo defaultSettlementInfo(Map<String,Object> map) {
        SettlementInfo settlementInfo = new SettlementInfo();
        //入驻结算规则ID	settlement_id	string[1,3]	是	请选择结算规则ID，详细参见《费率结算规则对照表》。
        //  示例值：719
        settlementInfo.setSettlementId("716");

        //所属行业	qualification_type	string[1,128]	是	请填写所属行业名称，详细参见《费率结算规则对照表》。
        //  示例值：餐饮
        settlementInfo.setQualificationType("电商平台");

        //特殊资质图片	qualifications	array [1, 1024]	条件选填
        //      1、根据所属行业的特殊资质要求提供，详情查看《费率结算规则对照表》。
        //      2、请提供为“申请商家主体”所属的特殊资质，可授权使用总公司/分公司的特殊资 质；
        //      3、最多可上传5张照片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        ArrayList<String> qualifications = new ArrayList<>();
        qualifications.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        //settlementInfo.setQualifications(qualifications);

        //优惠费率活动ID	activities_id	string[1,32]	条件选填	选择指定活动ID，如果商户有意向报名优惠费率活动，该字段必填。详细参见《优惠费率活动对照表》。
        //  示例值：20191030111cff5b5e
        //settlementInfo.setActivitiesId("20191030111cff5b5e");

        //优惠费率活动值	activities_rate	string[1,50]	条件选填	根据优惠费率活动规则，若填写“优惠费率活动ID”，则该字段必填。由服务商自定义填写，支持两个小数点，需在优惠费率活动ID指定费率范围内，如0.6%（接口无需传%，只需传数字）。
        //  示例值：0.6
        //settlementInfo.setActivitiesRate("0.6");

        //优惠费率活动补充材料	activities_additions	array[1, 1024]	否
        //      1、根据所选优惠费率活动，提供相关材料，详细参见《优惠费率活动对照表》。
        //      2、最多可上传5张照片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        settlementInfo.setActivitiesAdditions(null);
        settlementInfo.setActivitiesId("20191030111cff5b5e");
        settlementInfo.setActivitiesRate("0.2");
        return settlementInfo;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 经营资料	business_info	object	是	body 请填写商家的经营业务信息、售卖商品/提供服务场景信息。
     */
     private static BusinessInfo defaultBusinessInfo(Map<String,Object> map) {
        BusinessInfo businessInfo = new BusinessInfo();
        //商户简称	merchant_shortname	string[1,64]	是
        //      1、请输入2-30个字符，支持中文/字母/数字/特殊符号
        //      2、在支付完成页向买家展示，需与微信经营类目相关；
        //      3、简称要求
        //          （1）不支持单纯以人名来命名，若为个体户经营，可用“个体户+经营者名称”或“经营者名 称+业务”命名，如“个体户张三”或“张三餐饮店”；
        //          （2）不支持无实际意义的文案，如“XX特约商户”、“800”、“XX客服电话XXX”；
        //示例值：张三餐饮店
        businessInfo.setMerchantShortname(map.get("businessShortName")+"");

        //客服电话	service_phone	string[1,32]	是
        //      1、请填写真实有效的客服电话，将在交易记录中向买家展示，提供咨询服务；
        //      2、请确保电话畅通，以便入驻后平台回拨确认。
        //示例值：0758XXXXX
        businessInfo.setServicePhone(map.get("servePhone")+"");

        /**
         * 经营场景	sales_info	object	是	请根据实际经营情况，填写经营场景
         */
        SalesInfo salesInfo = defaultSalesInfo(map);
        businessInfo.setSalesInfo(salesInfo);

        return businessInfo;
    }

    /**
     * 经营场景	sales_info	object	是	请根据实际经营情况，填写经营场景
     */
    private static SalesInfo defaultSalesInfo(Map<String,Object> map) {
        SalesInfo salesInfo = new SalesInfo();
        //经营场景类型	sales_scenes_type	array	是
        //      1、请勾选实际售卖商品/提供服务场景（至少一项），以便为你开通需要的支付权限。
        //      2、建议只勾选目前必须的场景，以便尽快通过入驻审核，其他支付权限可在入驻后再根据实际需要发起申请。
        //  枚举值：
        //     SALES_SCENES_STORE：线下门店
        //     SALES_SCENES_MP：公众号
        //     SALES_SCENES_MINI_PROGRAM：小程序
        //     SALES_SCENES_WEB：互联网
        //     SALES_SCENES_APP：APP
        //     SALES_SCENES_WEWORK：企业微信
        //  示例值： ”SALES_SCENES_STORE”,”SALES_SCENES_MP”
        List<String> types = new ArrayList<>();
        types.add("SALES_SCENES_MP");
        salesInfo.setSalesScenesType(types);

        /**
         * 线下门店场景	biz_store_info	object	条件选填
         * 1、审核通过后，服务商可帮商户发起付款码支付、JSAPI支付。
         * 2、当“经营场景类型“选择“SALES_SCENES_STORE“，该场景资料必填。
         */
        BizStoreInfo bizStoreInfo = defaultBizStoreInfo();
        //salesInfo.setBizStoreInfo(bizStoreInfo);

        /**
         * +公众号场景	mp_info	object	条件选填
         * 1、审核通过后，服务商可帮商家发起JSAPI支付。
         * 2、当“经营场景类型“选择” SALES_SCENES_MP “，该场景资料必填。
         */
        MpInfo mpInfo = defaultMpInfo(map);
        salesInfo.setMpInfo(mpInfo);

        /*** +小程序场景	mini_program_info	object	条件选填
         * 1、审核通过后，服务商可帮商家发起JSAPI支付。
         * 2、当“经营场景类型“选择”SALES_SCENES_MINI_PROGRAM“，该场景资料必填。
         */
        MiniProgramInfo miniProgramInfo = defaultMiniProgramInfo();
        //salesInfo.setMiniProgramInfo(miniProgramInfo);

        /**
         * +APP场景	app_info	object	条件选填
         * 1、审核通过后，服务商可帮商家发起APP支付。
         * 2、当“经营场景类型“选择”SALES_SCENES_APP“，该场景资料必填。
         */
        AppInfo appInfo = defaultAppInfo();
        //salesInfo.setAppInfo(appInfo);

        /**
         * +互联网网站场景	web_info	object	条件选填
         * 1、审核通过后，服务商可帮商家发起JSAPI支付、Native支付。
         * 2、当“经营场景类型“选择”SALES_SCENES_WEB“，该场景资料必填。
         */
        WebInfo webInfo = defaultWebInfo();
        //salesInfo.setWebInfo(webInfo);

        /**
         * +企业微信场景	wework_info	object	条件选填
         * 1、审核通过后，商家可自行发起企业微信支付。
         * 2、当“经营场景类型“选择”SALES_SCENES_WEWORK“，该场景资料必填。
         */
        WeworkInfo weworkInfo = defaultWeworkInfo();
        //salesInfo.setWeworkInfo(weworkInfo);

        return salesInfo;
    }

    /**
     * +企业微信场景	wework_info	object	条件选填
     * 1、审核通过后，商家可自行发起企业微信支付。
     * 2、当“经营场景类型“选择”SALES_SCENES_WEWORK“，该场景资料必填。
     */
    private static WeworkInfo defaultWeworkInfo() {
        WeworkInfo weworkInfo = new WeworkInfo();
        //商家企业微信CorpID	sub_corp_id	string[1,256]	是
        //      1、可填写与商家主体一致且已认证的企业微信CorpID。
        //      2、审核通过后，系统将为商家开通企业微信专区的自有交易权限，并完成商家商户号与该APPID的绑定，商家可自行发起交易。
        //示例值：wx1234567890123456
        weworkInfo.setCorpId("wx1234567890123456");

        //企业微信页面截图	wework_pics	array[1, 1024]	是
        //      1、最多可上传5张照片。
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        ArrayList<String> pics = new ArrayList<>();
        pics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        weworkInfo.setWeworkPics(pics);

        return weworkInfo;
    }

    /**
     * +互联网网站场景	web_info	object	条件选填
     * 1、审核通过后，服务商可帮商家发起JSAPI支付、Native支付。
     * 2、当“经营场景类型“选择”SALES_SCENES_WEB“，该场景资料必填。
     */
    private static WebInfo defaultWebInfo() {
        WebInfo webInfo = new WebInfo();
        //互联网网站域名	domain	string[1,1024]	是
        //      1、如为PC端商城、智能终端等场景，可上传官网链接。
        //      2、网站域名需ICP备案，若备案主体与申请主体不同，请上传加盖公章的网站授权函。
        //示例值：http://www.qq.com
        webInfo.setDomain("http://www.qq.com");

        //网站授权函	web_authorisation	string[1,1024]	否
        //      1、若备案主体与申请主体不同，请务必上传加盖公章的网站授权函。
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值： 47ZC6GC-vnrbEnyVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
        webInfo.setWebAuthorisation("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //互联网网站对应的商家APPID	web_appid	string[1,256]	否
        //      1、可填写已认证的公众号、小程序、应用的APPID，其中公众号APPID需是已认证的服务 号、政府或媒体类型的订阅号；
        //      2、完成进件后，系统发起特约商户号与该AppID的绑定（即配置为sub_appid，可在发起 支付时传入）
        //          （1）若APPID主体与商家主体一致，则直接完成绑定；
        //          （2）若APPID主体与商家主体不一致，则商户签约时显示《联合营运承诺函》，并且 AppID的管理员需登录公众平台确认绑定意愿；（ 暂不支持绑定异主体的应用APPID）。
        //示例值：wx1234567890123456
        webInfo.setWebAppid("wx1234567890123456");

        return webInfo;
    }

    /**
     * +APP场景	app_info	object	条件选填
     * 1、审核通过后，服务商可帮商家发起APP支付。
     * 2、当“经营场景类型“选择”SALES_SCENES_APP“，该场景资料必填。
     */
    private static AppInfo defaultAppInfo() {
        AppInfo appInfo = new AppInfo();
        //服务商应用APPID	app_appid	string[1,256]	二选一
        //      1、服务商应用APPID与商家应用APPID，二选一必填。
        //      2、可填写当前服务商商户号已绑定的应用APPID。
        //示例值：wx1234567890123456
        appInfo.setAppAppid("wx1234567890123456");

        //商家应用APPID	app_sub_appid	string[1,256]
        //      1、服务商应用APPID与商家应用APPID，二选一必填。
        //      2、可填写与商家主体一致且已认证的应用APPID，需是已认证的APP。
        //      3、审核通过后，系统将发起特约商家商户号与该AppID的绑定（即配置为sub_appid），服务商随后可在发起支付时选择传入该appid，以完成支付，并获取sub_openid用于数据统计，营销等业务场景。
        //示例值：wx1234567890123456
        appInfo.setAppSubAppid("wx1234567890123456");

        //APP截图	app_pics	array[1, 1024]	是
        //      1、请提供APP首页截图、尾页截图、应用内截图、支付页截图各1张。
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        ArrayList<String> pics = new ArrayList<>();
        pics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        appInfo.setAppPics(pics);

        return appInfo;
    }

    /*** +小程序场景	mini_program_info	object	条件选填
     * 1、审核通过后，服务商可帮商家发起JSAPI支付。
     * 2、当“经营场景类型“选择”SALES_SCENES_MINI_PROGRAM“，该场景资料必填。
     */
    private static MiniProgramInfo defaultMiniProgramInfo() {
        MiniProgramInfo miniProgramInfo = new MiniProgramInfo();
        //服务商小程序APPID	mini_program_appid	string[1,256]	二选一
        //      1、服务商小程序APPID与商家小程序APPID，二选一必填。
        //      2、可填写当前服务商商户号已绑定的小程序APPID。
        //示例值：wx1234567890123456
        miniProgramInfo.setMiniProgramAppid("wxbc5aeca4287a3b95");

        //商家小程序APPID	mini_program_sub_appid	string[1,256]
        //      1、服务商小程序APPID与商家小程序APPID，二选一必填；
        //      2、请填写已认证的小程序APPID；
        //      3、完成进件后，系统发起特约商户号与该AppID的绑定（即配置为sub_appid可在发起支付时传入）
        //          （1）若APPID主体与商家主体/服务商主体一致，则直接完成绑定；
        //          （2）若APPID主体与商家主体/服务商主体不一致，则商户签约时显示《联合营运承诺 函》，并且AppID的管理员需登录公众平台确认绑定意愿；
        //示例值：wx1234567890123456
        //miniProgramInfo.setMiniProgramSubAppid("wx1234567890123456");

        //小程序截图	mini_program_pics	array[1, 1024]	条件选填
        //      1、请提供展示商品/服务的页面截图/设计稿（最多5张），若小程序未建设完善或未上线 请务必提供；
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        List<String> pics = new ArrayList<>();
        pics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        //miniProgramInfo.setMiniProgramPics(pics);

        return miniProgramInfo;
    }

    /**
     * +公众号场景	mp_info	object	条件选填
     * 1、审核通过后，服务商可帮商家发起JSAPI支付。
     * 2、当“经营场景类型“选择” SALES_SCENES_MP “，该场景资料必填。
     */
    private static MpInfo defaultMpInfo(Map<String,Object> map) {
        MpInfo mpInfo = new MpInfo();
        //服务商公众号APPID	mp_appid	string[1,256]	二选一
        //      1、服务商公众号APPID与商家公众号APPID，二选一必填。
        //      2、可填写当前服务商商户号已绑定的公众号APPID。
        //示例值：wx1234567890123456
        mpInfo.setMpAppid(map.get("mpAppid")+"");

        //商家公众号APPID	mp_sub_appid	string[1,256]
        //      1、服务商公众号APPID与商家公众号APPID，二选一必填。
        //      2、可填写与商家主体一致且已认证的公众号APPID，需是已认证的服务号、政府或媒体类型的订阅号。
        //      3、审核通过后，系统将发起特约商家商户号与该AppID的绑定（即配置为sub_appid），服务商随后可在发起支付时选择传入该appid，以完成支付，并获取sub_openid用于数据统计，营销等业务场景 。
        //示例值：wx1234567890123456
        //mpInfo.setMpSubAppid("wx1234567890123456");

        //公众号页面截图	mp_pics	array[1, 1024]	条件选填
        //      1、请提供展示商品/服务的页面截图/设计稿（最多5张），若公众号未建设完善或未上线请务必提供。
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        ArrayList<String> pics = new ArrayList<>();
        pics.add(map.get("pics")+"");
        mpInfo.setMpPics(pics);

        return mpInfo;
    }

    /**
     * 线下门店场景	biz_store_info	object	条件选填
     * 1、审核通过后，服务商可帮商户发起付款码支付、JSAPI支付。
     * 2、当“经营场景类型“选择“SALES_SCENES_STORE“，该场景资料必填。
     */
    private static BizStoreInfo defaultBizStoreInfo() {
        BizStoreInfo bizStoreInfo = new BizStoreInfo();
        //门店名称	biz_store_name	string[1,128]	是	请填写门店名称。
        //示例值：大郎烧饼
        bizStoreInfo.setBizStoreName("大郎烧饼");

        //门店省市编码	biz_address_code	string[1,128]	是	只能由数字组成，详细参见《微信支付提供的省市对照表》。
        //示例值：440305
        bizStoreInfo.setBizAddressCode("440305");

        //门店地址	biz_store_address	string[1,128]	是	请填写详细的经营场所信息，如有多个场所，选择一个主要场所填写即可。
        //示例值：南山区xx大厦x层xxxx室
        bizStoreInfo.setBizStoreAddress("广东省深圳市福田区卓越世纪中心");

        //门店门头照片	store_entrance_pic	array[1, 1024]	是
        //      1、请上传门店照片（要求门店招牌清晰可见）。若为停车场、售卖机等无固定门头照片 的经营场所，请提供真实的经营现场照片即可；
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值： 0P3ng6KTIW4-QJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        List<String> pics = new ArrayList<>();
        pics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        bizStoreInfo.setStoreEntrancePic(pics);

        //店内环境照片	indoor_pic	array[1,1024]	是
        //      1、请上传门店内部环境照片。若为停车场、售卖机等无固定门头照片的经营场所，请提 供真实的经营现场照片即可；
        //      2、请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //示例值：0P3ng6KTIW4-Q_l2FjmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        List<String> indoorPics = new ArrayList<>();
        indoorPics.add("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");
        bizStoreInfo.setIndoorPic(indoorPics);

        //线下场所对应的商家APPID	biz_sub_appid	string[1,256]	否
        //      1、可填写已认证的公众号、小程序、应用的APPID，其中公众号APPID需是已认证的服务 号、政府或媒体类型的订阅号。
        //      2、完成进件后，系统发起特约商户号与该AppID的绑定（即配置为sub_appid，可在发起 支付时传入）
        //          （1）若APPID主体与商家主体一致，则直接完成绑定；
        //          （2）若APPID主体与商家主体不一致，则商户签约时显示《联合营运承诺函》，并且 AppID的管理员需登录公众平台确认绑定意愿；（ 暂不支持绑定异主体的应用APPID）
        //示例值：wx1234567890123456
        //bizStoreInfo.setBizSubAppid("wx1234567890123456");

        return bizStoreInfo;
    }

    /**
     * 主体资料	subject_info	object	是	body 请填写商家的营业执照/登记证书、经营者/法人的证件等信息。
     */
    private static SubjectInfo defaultSubjectInfo(Map<String,Object> map) {
        SubjectInfo subjectInfo = new SubjectInfo();
        //主体类型	subject_type	string[1,32]	是	主体类型需与营业执照/登记证书上一致，可参考《选择主体指引》
        //      SUBJECT_TYPE_INDIVIDUAL（个体户）：营业执照上的主体类型一般为个体户、个体工商户、个体经营；
        //      SUBJECT_TYPE_ENTERPRISE（企业）：营业执照上的主体类型一般为有限公司、有限责任公司；
        //      SUBJECT_TYPE_INSTITUTIONS（党政、机关及事业单位）：包括国内各级、各类政府机构、事业单位等（如：公安、党团、司法、交通、旅游、工商税务、市政、医疗、教育、学校等机构）；
        //      SUBJECT_TYPE_OTHERS（其他组织）：不属于企业、政府/事业单位的组织机构（如社会团体、民办非企业、基金会），要求机构已办理组织机构代码证。
        //  示例值：SUBJECT_TYPE_ENTERPRISE
        subjectInfo.setSubjectType("SUBJECT_TYPE_ENTERPRISE");


        /**
         * 营业执照	business_license_info	object	条件选填
         *      1、主体为个体户/企业，必填。
         *      2、请上传“营业执照”，需年检章齐全，当年注册除外。
         */
        BusinessLicenseInfo businessLicenseInfo = defaultBusinessLicenseInfo(map);
        subjectInfo.setBusinessLicenseInfo(businessLicenseInfo);

        /**
         * 登记证书	certificate_info	object	条件选填	主体为党政、机关及事业单位/其他组织，必填。
         *      1、党政、机关及事业单位：请上传相关部门颁发的证书，如：事业单位法人证书、统一社会信用代码证书。
         *      2、其他组织：请上传相关部门颁发的证书，如：社会团体法人登记证书、民办非企业单位登记证书、基金会法人登记证书。
         */
        //CertificateInfo certificateInfo = defaultCertificateInfo();
        subjectInfo.setCertificateInfo(null);

        /**
         * 组织机构代码证	organization_info	object	条件选填	主体为企业/党政、机关及事业单位/其他组织，且证件号码不是18位时必填。
         */
        OrganizationInfo organizationInfo = defaultOrganizationInfo(map);
        subjectInfo.setOrganizationInfo(null);

        /**
         * 单位证明函照片	certificate_letter_copy	string[1,255]	条件选填
         *       1、主体类型为党政、机关及事业单位选传;
         *           （1）若上传，则审核通过后即可签约，无需汇款验证。
         *           （2）若未上传，则审核通过后，需汇款验证。
         *       2、主体为个体户、企业、其他组织等，不需要上传本字段。
         *       3、请参照示例图打印单位证明函，全部信息需打印，不支持手写商户信息，并加盖公章。
         *       4、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
         *   示例值： 47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KEIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
         */
        //subjectInfo.setCertificateLetterCopy("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        /**
         * 经营者/法人身份证件	identity_info	object	是	body
         *      1、个体户：请上传经营者的身份证件。
         *      2、企业/党政、机关及事业单位/其他组织：请上传法人的身份证件。
         */
        IdentityInfo identityInfo = defaultIdentityInfo(map);
        subjectInfo.setIdentityInfo(identityInfo);

        /**
         * 最终受益人信息(UBO]	ubo_info	object	条件选填	若经营者/法人不是最终受益所有人，则需提填写受益所有人信息。
         *    根据国家相关法律法规，需要提供公司受益所有人信息，受益所有人需符合至少以下条件之一：
         *      1、直接或者间接拥有超过25%公司股权或者表决权的自然人。
         *      2、通过人事、财务等其他方式对公司进行控制的自然人。
         *      3、公司的高级管理人员，包括公司的经理、副经理、财务负责人、上市公司董事会秘书和公司章程规定的其他人员。
         */
        UboInfo uboInfo = defaultUboInfo();
        //subjectInfo.setUboInfo(uboInfo);

        return subjectInfo;
    }

    /**
     * 最终受益人信息(UBO]	ubo_info	object	条件选填	若经营者/法人不是最终受益所有人，则需提填写受益所有人信息。
     * 根据国家相关法律法规，需要提供公司受益所有人信息，受益所有人需符合至少以下条件之一：
     * 1、直接或者间接拥有超过25%公司股权或者表决权的自然人。
     * 2、通过人事、财务等其他方式对公司进行控制的自然人。
     * 3、公司的高级管理人员，包括公司的经理、副经理、财务负责人、上市公司董事会秘书和公司章程规定的其他人员。
     */
    private static UboInfo defaultUboInfo() {
        UboInfo uboInfo = new UboInfo();
        //证件类型	id_type	string[1,37]	是
        //      填写受益人的证件类型。
        //  枚举值：
        //      IDENTIFICATION_TYPE_IDCARD：中国大陆居民-身份证
        //      IDENTIFICATION_TYPE_OVERSEA_PASSPORT：其他国家或地区居民-护照
        //      IDENTIFICATION_TYPE_HONGKONG_PASSPORT：中国香港居民-来往内地通行证
        //      IDENTIFICATION_TYPE_MACAO_PASSPORT：中国澳门居民-来往内地通行证
        //      IDENTIFICATION_TYPE_TAIWAN_PASSPORT：中国台湾居民-来往大陆通行证
        //  示例值：IDENTIFICATION_TYPE_IDCARD
        uboInfo.setIdType("IDENTIFICATION_TYPE_IDCARD");

        //身份证人像面照片	id_card_copy	string[1,256]	条件选填
        //      1、受益人的证件类型为“身份证”时，上传身份证人像面照片。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //      3、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //  示例值：jTpGmxUX3FBWeFxikqJ5YW6zFQ
        uboInfo.setIdCardCopy("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //身份证国徽面照片	id_card_national	string[1,256]	条件选填
        //      1、受益人的证件类型为“身份证”时，上传身份证国徽面照片。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //      3、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //  示例值：jTpGmxUXqRTvDujqhThn4ReFxikqJ5YW6zFQ
        uboInfo.setIdCardNational("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //证件照片	id_doc_copy	string[1,256]	条件选填
        //      1、受益人的证件类型为“来往内地通行证、来往大陆通行证、护照”时，上传证件照片。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //      3、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //  示例值：jTpGmxUX3FBWVQ5NJTZvvDujqhThn4ReFxikqJ5YW6zFQ
        uboInfo.setIdDocCopy(null);

        //受益人姓名	name	string[1,128]	是
        //      该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：AOZdYGISxo4y44/Ug4P4TG5xzchG/5IL9DBd+Z0zZXkw==
        //uboInfo.setName("AOZdYGISxo4y44/Ug4P4TG5xzchG/5IL9DBd+Z0zZXkw==");
        uboInfo.setName("善筑科技");

        //证件号码	id_number	string[1,128]	是
        //      该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：AOZdYGISxo4y44/Ug4P4TG5xzchG/5IL9DBd+Z0zZXkw==
        //uboInfo.setIdNumber("AOZdYGISxo4y44/Ug4P4TG5xzchG/5IL9DBd+Z0zZXkw==");
        uboInfo.setIdNumber("522530199505033335");

        //证件有效期开始时间	id_period_begin	string[1,128]	是
        //      1、请按照示例值填写。
        //      2、结束时间大于开始时间。
        //  示例值：2019-06-06
        uboInfo.setIdPeriodBegin("2019-06-06");

        //证件有效期结束时间	id_period_end	string[1,128]	是
        //      1、请按照示例值填写，若证件有效期为长期，请填写：长期。
        //      2、结束时间大于开始时间。
        //      3、证件有效期需大于60天。
        //  示例值：2026-06-06
        uboInfo.setIdPeriodEnd("长期");

        return uboInfo;
    }

    /**
     * 经营者/法人身份证件	identity_info	object	是	body
     * 1、个体户：请上传经营者的身份证件。
     * 2、企业/党政、机关及事业单位/其他组织：请上传法人的身份证件。
     */
    private static IdentityInfo defaultIdentityInfo(Map<String,Object> map) {
        IdentityInfo identityInfo = new IdentityInfo();
        //证件类型	id_doc_type	string[1,42]	是
        //      个体户/企业/党政、机关及事业单位/其他组织：可选择任一证件类型。
        //  枚举值：
        //      IDENTIFICATION_TYPE_IDCARD：中国大陆居民-身份证
        //      IDENTIFICATION_TYPE_OVERSEA_PASSPORT：其他国家或地区居民-护照
        //      IDENTIFICATION_TYPE_HONGKONG_PASSPORT：中国香港居民-来往内地通行证
        //      IDENTIFICATION_TYPE_MACAO_PASSPORT：中国澳门居民-来往内地通行证
        //      IDENTIFICATION_TYPE_TAIWAN_PASSPORT：中国台湾居民-来往大陆通行证
        //  示例值：IDENTIFICATION_TYPE_IDCARD
        identityInfo.setIdDocType("IDENTIFICATION_TYPE_IDCARD");

        /**
         * 身份证信息	id_card_info	object	否	证件类型为“身份证”时填写。
         */
        IdCardInfo idCardInfo = defaultIdCardInfo(map);
        identityInfo.setIdCardInfo(idCardInfo);

        /**
         * 其他类型证件信息	id_doc_info	object	否	证件类型为“来往内地通行证、来往大陆通行证、护照”时填写。
         */
        IdDocInfo idDocInfo = defaultIdDocInfo();
        //identityInfo.setIdDocInfo(idDocInfo);

        //经营者/法人是否为受益人	owner	bool	是
        //      1、若经营者/法人是最终受益人，则填写：true。
        //      2、若经营者/法人不是最终受益人，则填写：false。
        //  示例值：true
        identityInfo.setOwner(true);

        return identityInfo;
    }

    /**
     * 其他类型证件信息	id_doc_info	object	否	证件类型为“来往内地通行证、来往大陆通行证、护照”时填写。
     */
    private static IdDocInfo defaultIdDocInfo() {
        IdDocInfo idDocInfo = new IdDocInfo();
        //证件照片	id_doc_copy	string[1,256]	是
        //      1、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值：jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ
        idDocInfo.setIdDocCopy("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //证件姓名	id_doc_name	string[1,128]	是
        //      1、请填写经营者/法定代表人的证件姓名，2~30个中文字符、英文字符、符号。
        //      2、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6NxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //idDocInfo.setIdDocName("pVd1HJ6NxLMpRSvWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        idDocInfo.setIdDocName("善筑科技");

        //证件号码	id_doc_number	string[1,128]	是
        //      1、请填写经营者/法定代表人的证件号码。
        //      2、8-30位数字|字母|连字符，该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：AOZdYGISxo4V4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==
        //idDocInfo.setIdDocNumber("AOZdYGISxo4V4oASq6ZFonjTtgjjgKsadIKHXtb3JZKGZjduGdtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==");
        idDocInfo.setIdDocNumber("5225300199505033335");

        //证件有效期开始时间	doc_period_begin	string[1,128]	是
        //      1、选填，请按照示例值填写。
        //      2、结束时间大于开始时间。
        //  示例值：2019-06-06
        idDocInfo.setDocPeriodBegin("2019-06-06");

        //证件有效期结束时间	doc_period_end	string[1,128]	是
        //      1、必填，请按照示例值填写。
        //      2、若证件有效期为长期，请填写：长期。
        //      3、结束时间大于开始时间。
        //      4、证件有效期需大于60天。
        //  示例值：2026-06-06
        idDocInfo.setDocPeriodEnd("长期");

        return idDocInfo;
    }

    /**
     * 身份证信息	id_card_info	object	否	证件类型为“身份证”时填写。
     */
    private static IdCardInfo defaultIdCardInfo(Map<String,Object> map) {
        IdCardInfo idCardInfo = new IdCardInfo();
        //身份证人像面照片	id_card_copy	string[1,256]	是
        //      1、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值：jTpGmxUX3FBWVQ5NJTZvlKX_gdU4cRz7z5NxpnFuAxhBTEO_PvWkfSCJ3zVIn001D8daLC-ehEuo0BJqRTvDujqhThn4ReFxikqJ5YW6zFQ
        idCardInfo.setIdCardCopy(map.get("idCardCopy")+"");

        //身份证国徽面照片	id_card_national	string[1,256]	是
        //      1、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证）。
        //      2、可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值： 47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
        idCardInfo.setIdCardNational(map.get("idCardNational")+"");

        //身份证姓名	id_card_name	string[1,256]	是
        //      1、请填写个体户经营者/法定代表人对应身份证的姓名，2~30个中文字符、英文字符、符号。
        //      2、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPRDm2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //idCardInfo.setIdCardName("pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPRDm2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        idCardInfo.setIdCardName(map.get("legarName")+"");

        //身份证号码	id_card_number	string[1,256]	是
        //      1、请填写个体户经营者/法定代表人对应身份证的号码。
        //      2、15位数字或17位数字+1位数字|X，该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：AOZdYGISxo4y44/UgZ69bdagEcGe+vLYiKrzcrodtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==
        //idCardInfo.setIdCardNumber("AOZdYGISxo4y44/UgZ69bdagEcGe+vLYiKrzcrodtkRJJp0/0eow96uY1Pk7Rq79Jtt7+I8juwEc4P4TG5xzchG/5IL9DBd+Z0zZXkw==");
        idCardInfo.setIdCardNumber(map.get("legarCard")+"");

        //身份证有效期开始时间	card_period_begin	string[1,128]	是
        //      1、必填，请按照示例值填写。
        //      2、结束时间大于开始时间。
        //  示例值：2026-06-06
        idCardInfo.setCardPeriodBegin(map.get("cardStart")+"");

        //身份证有效期结束时间	card_period_end	string[1,128]	是
        //      1、必填，请按照示例值填写。
        //      2、若证件有效期为长期，请填写：长期。
        //      3、结束时间大于开始时间。
        //      4、证件有效期需大于60天。
        //  示例值：2036-06-06
        idCardInfo.setCardPeriodEnd(map.get("cardEnd")+"");
        idCardInfo.setIdCardAddress(map.get("idCardAddress")+"");
        return idCardInfo;
    }

    /**
     * 组织机构代码证	organization_info	object	条件选填	主体为企业/党政、机关及事业单位/其他组织，且证件号码不是18位时必填。
     */
    private static OrganizationInfo defaultOrganizationInfo(Map<String,Object> map) {
        OrganizationInfo organizationInfo = new OrganizationInfo();
        //组织机构代码证照片	organization_copy	string[1,256]	是	可上传1张图片，请填写通过《图片上传API》预先上传图片生成好的MediaID。
        //  示例值：47ZC6GCGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
        organizationInfo.setOrganizationCopy(map.get("organizationCopy")+"");

        //组织机构代码	organization_code	string[1,32]	是
        //      1、请填写组织机构代码证上的组织机构代码。
        //      2、可填写9或10位数字|字母|连字符。
        //  示例值：123456789-A
        organizationInfo.setOrganizationCode("1234567890");

        //组织机构代码证有效期开始日期	org_period_begin	string[1,32]	是
        //      1、请按照示例值填写。
        //      2、证件有效期需大于60天。
        //  示例值：2019-08-01
        organizationInfo.setOrgPeriodBegin("2019-08-01");

        //组织机构代码证有效期结束日期	org_period_end	string[1,32]	是
        //      1、请按照示例值填写。
        //      2、若证件有效期为长期，请填写：长期。
        //      3、结束日期大于开始日期。
        //      4、证件有效期需大于60天。
        //  示例值：2029-08-01，长期
        organizationInfo.setOrgPeriodEnd("长期");

        return organizationInfo;
    }

    /**
     * 登记证书照片	cert_copy	string[1,255]	是
     * 1、请填写通过《图片上传API》预先生成的MediaID，上传1张图片即可；
     * 2、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水 印（如微信支付认证） 。
     * 示例值: 0P3ng6_XXqK5dE5YdOmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
     */
    private static CertificateInfo defaultCertificateInfo() {
        CertificateInfo certificateInfo = new CertificateInfo();
        //登记证书照片	cert_copy	string[1,255]	是
        //      1、请填写通过《图片上传API》预先生成的MediaID，上传1张图片即可；
        //      2、请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水 印（如微信支付认证） 。
        //  示例值: 0P3ng6_XXqK5dE5YdOmFJBZR9FwczhJehHhAZN6BKXQPcs-VvdSo
        certificateInfo.setCertCopy("DzLwIk1vPNqvZuCZ0-zZnGmz4tyyErUSdoVSfaODHOXNKs26X3ILcgsX5cZ6yIGvjZm2VhL3fyhgAEcB3jlfKgxoG7eHj0cU3Z_RqvDTQoY");

        //登记证书类型	cert_type	string[1,25]	是	登记证书的类型
        //  枚举值：
        //      CERTIFICATE_TYPE_2388：事业单位法人证书
        //      CERTIFICATE_TYPE_2389：统一社会信用代码证书
        //      CERTIFICATE_TYPE_2390：有偿服务许可证（军队医院适用）
        //      CERTIFICATE_TYPE_2391：医疗机构执业许可证（军队医院适用）
        //      CERTIFICATE_TYPE_2392：企业营业执照（挂靠企业的党组织适用）
        //      CERTIFICATE_TYPE_2393：组织机构代码证（政府机关适用）
        //      CERTIFICATE_TYPE_2394：社会团体法人登记证书
        //      CERTIFICATE_TYPE_2395：民办非企业单位登记证书
        //      CERTIFICATE_TYPE_2396：基金会法人登记证书
        //      CERTIFICATE_TYPE_2397：慈善组织公开募捐资格证书
        //      CERTIFICATE_TYPE_2398：农民专业合作社法人营业执照
        //      CERTIFICATE_TYPE_2399：宗教活动场所登记证
        //      CERTIFICATE_TYPE_2400：其他证书/批文/证明
        //  示例值：CERTIFICATE_TYPE_2388
        certificateInfo.setCertType("CERTIFICATE_TYPE_2389");

        //证书号	cert_number	string[1,32]	是	请填写登记证书上的证书编号。
        //  示例值：111111111111
        certificateInfo.setCertNumber("111111111111");

        //商户名称	merchant_name	string[1,128]	是	请填写登记证书上的商户名称。
        //  示例值：xx公益团体
        certificateInfo.setMerchantName("善筑科技");

        //注册地址	company_address	string[1,128]	是	请填写登记证书的注册地址。
        //  示例值：广东省深圳市南山区xx路xx号
        certificateInfo.setCompanyAddress("广东省深圳市福田区卓越世纪中心");

        //法人姓名	legal_person	string[1,64]	是
        //      1、只能由中文字符、英文字符、可见符号组成。
        //      2、请填写登记证书上的法定代表人姓名。
        //  示例值：李四
        certificateInfo.setLegalPerson("善筑科技");

        //有效期限开始日期	period_begin	string[1,32]	是
        //      1、必填， 请参考示例值填写。
        //      2、开始日期，开始日期需大于当前日期
        //  示例值：2019-08-01
        certificateInfo.setPeriodBegin("2019-08-01");

        //有效期限结束日期	period_end	string[1,32]	是
        //      1、必填，请参考示例值填写。
        //      2、若证件有效期为长期，请填写：长期。
        //      3、结束日期大于开始日期。
        //      4、有效期必须大于60天。
        //  示例值：2029-08-01，长期
        certificateInfo.setPeriodEnd("长期");

        return certificateInfo;
    }

    /**
     * 营业执照	business_license_info	object	条件选填
     * 1、主体为个体户/企业，必填。
     * 2、请上传“营业执照”，需年检章齐全，当年注册除外。
     */
    private static BusinessLicenseInfo defaultBusinessLicenseInfo(Map<String,Object> ma) {
        BusinessLicenseInfo businessLicenseInfo = new BusinessLicenseInfo();
        //营业执照照片	license_copy	string[1,255]	是
        //      1、可上传1张图片，请填写通过《图片上传API》预先生成的MediaID。
        //      2、 请上传彩色照片or彩色扫描件or复印件（需加盖公章鲜章），可添加“微信支付”相关水印（如微信支付认证） 。
        //  示例值：47ZC6GC-vnrbEny__Ie_An5-tCpqxucuxi-vByf3Gjm7KE53JXvGy9tqZm2XAUf-4KGprrKhpVBDIUv0OF4wFNIO4kqg05InE4d2I6_H7I4
        businessLicenseInfo.setLicenseCopy(ma.get("licenseCopy")+"");

        //注册号/统一社会信用代码	license_number	string[1,32]	是	请填写营业执照上的营业执照注册号，注册号格式须为15位数字或18位数字|大写字母。
        //  示例值：123456789012345678
        businessLicenseInfo.setLicenseNumber(ma.get("businessNo")+"");

        //商户名称	merchant_name	string[1,128]	是
        //      1、请填写营业执照上的商户名称，2~110个字符，支持括号。
        //      2、个体户，不能以“公司”结尾。
        //      3、个体户，若营业执照上商户名称为“空“或“无”时，填写"个体户+经营者姓名"，如“个体户张三”。
        //  示例值：腾讯科技有限公司
        businessLicenseInfo.setMerchantName(ma.get("businessFullName")+"");

        //个体户经营者/法人姓名	legal_person	string[1,64]	是	请填写营业执照的经营者/法定代表人姓名。
        //  示例值：张三
        businessLicenseInfo.setLegalPerson(ma.get("legarName")+"");

        return businessLicenseInfo;
    }

    /**
     * 超级管理员信息	contact_info	object	是	body 超级管理员需在开户后进行签约，并接收日常重要管理信息和进行资金操作，请确定其为商户法定代表人或负责人。
     */
    public static ContactInfo defaultContactInfo(Map<String,Object> map) {
        ContactInfo contactInfo = new ContactInfo();
        //1、主体为“个体工商户/企业/政府机关/事业单位/社会组织”，可选择：LEGAL：经营者/法人，SUPER：经办人 。（经办人：经商户授权办理微信支付业务的人员）。
        //枚举值：
        //LEGAL：经营者/法人
        //SUPER：经办人
        //示例值：LEGAL
        contactInfo.setContactType("LEGAL");
        //超级管理员姓名	contact_name	string[2,2048]	是
        //      1、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriuhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //contactInfo.setContactName("pVd1HJ6zyvPedzGaV+X3qtmrq9bb9tPttdY+aQ6zBlw0xnOiNW6Hzy7UtC+xriuhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        contactInfo.setContactName(map.get("legarName")+"");

        //超级管理员身份证件号码	contact_id_number	string[1,2048]	二选一
        //      1、“超级管理员身份证号码”与“超级管理员微信openid”，二选一必填。
        //      2、超级管理员签约时，校验微信号绑定的银行卡实名信息，是否与该证件号码一致。
        //      3、可传身份证、来往内地通行证、来往大陆通行证、护照等证件号码。
        //      4、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6zyvPedzGaV+X3qtWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //contactInfo.setContactIdNumber("pVd1HJ6zyvPedzGaV+X3qtWKIjOv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        //contactInfo.setContactIdNumber("522530199505033335");

        //超级管理员微信openid	openid	string[1,128]
        //      1、“超级管理员身份证件号码”与“超级管理员微信openid”，二选一必填。
        //      2、超级管理员签约时，校验微信号是否与该微信openid一致。
        //  示例值：pVd1HJ6zyvPedzGaV+X/2C+xriudjD5APomty7/mYNxLMpRSvWKIjOv/69bD2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //contactInfo.setOpenid("pVd1HJ6zyvPedzGaV+X/2C+xriudjD5APomty7/mYNxLMpRSvWKIjOv/69bD2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");

        //联系手机	mobile_phone	string[1,2048]	是
        //      1、11位数字。
        //      2、用于接收微信支付的重要管理信息及日常操作验证码。
        //      3、该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6zyvPedzGaV+Xv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //contactInfo.setMobilePhone("pVd1HJ6zyvPedzGaV+Xv/69bDnuC4EL5Kz4jBHLiCyOb+tI0m2qhZ9evAM+Jv1z0NVa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        contactInfo.setMobilePhone(map.get("legarPhone")+"");

        //联系邮箱	contact_email	string[1,2048]	是
        //      1、用于接收微信支付的开户邮件及日常业务通知。
        //      2、需要带@，遵循邮箱格式校验，该字段需进行加密处理，加密方法详见《敏感信息加密说明》。(提醒：必须在HTTP头中上送Wechatpay-Serial)
        //  示例值：pVd1HJ6zyvPedzGaV+P/2qIdGdONoDzZelrxCl/NWWKIjOv/69bDnuC4EL5Kz4jBHLiCyOa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==
        //contactInfo.setContactEmail("pVd1HJ6zyvPedzGaV+P/2qIdGdONoDzZelrxCl/NWWKIjOv/69bDnuC4EL5Kz4jBHLiCyOa8MRtelw/wDa4SzfeespQO/0kjiwfqdfg==");
        contactInfo.setContactEmail(map.get("contactEmail")+"");

        return contactInfo;
    }
}
