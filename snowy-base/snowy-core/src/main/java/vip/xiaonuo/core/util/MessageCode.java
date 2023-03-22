package vip.xiaonuo.core.util;

/**
   * 返回信息及编码
 */
public enum MessageCode {

    SUCCESS(200, "请求成功"),
    ERROR(300, "请求失败，业务处理错误"),
    BAD_REQUEST_PARAMS(400, "请求参数有误"),
    TOKEN_INVALID(401, "用户验证信息无效"),
    NOT_LOGIN(402, "请先登录"),
    PERMISSION_DENIED(403, "权限不足"),
    SIGN_ERROR(405, "签名无效"),
    SYSTEM_ERROR(500, "系统繁忙，请稍后再试"),
    BIZ_ERROR(501, "业务异常"),
	REFUND_ERROR(601, "退款失败");


    private int msgCode;
    private String message;

    MessageCode(int msgCode, String message) {
        this.msgCode = msgCode;
        this.message = message;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public String getMessage() {
        return message;
    }
      
    public static String getMessage(Integer value) {  
        MessageCode[] businessModeEnums = values();  
        for (MessageCode businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.msgCode == value) {  
                return businessModeEnum.message;  
            }  
        }  
        return null;  
    }
    
    public static String getName(Integer value) {  
        MessageCode[] businessModeEnums = values();  
        for (MessageCode businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.msgCode == value) {  
                return businessModeEnum.name();  
            }  
        }  
        return null;  
    }
    
    public static Integer getCode(String enumName) {  
        MessageCode[] businessModeEnums = values();  
        for (MessageCode businessModeEnum : businessModeEnums) {  
            if (businessModeEnum.name().equals(enumName)) {  
                return businessModeEnum.msgCode;  
            }  
        }
		return null;  
    }
    
    public static void main(String[] args) {
		System.out.println(MessageCode.getCode("BAD_REQUEST_PARAMS"));
	}
}