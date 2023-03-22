package vip.xiaonuo.core.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class ResultBean {
	/**
	 * @des 默认返回成功  MessageCode.SUCCESS.getMsgCode()
	 * @return
	 */
	private Integer code=MessageCode.SUCCESS.getMsgCode();
	/**
	 * @des 默认返回成功    MessageCode.SUCCESS.getMessage()
	 * @return
	 */
	private String msg;
	/**
	 * @return 返回结果
	 */
	private Object data;
	/**
	 * @return 返回自定义扩展信息
	 */
	private String result;

	public ResultBean() {
		super();
	}

	public ResultBean(Object data) {
		super();
		this.data = data;
	}
	
	/**
	 * @param messageCode 枚举
	 * @param message 自定义返回信息
	 */
	public ResultBean(MessageCode messageCode, String message) {
		super();
		this.code = messageCode.getMsgCode();
		this.msg = message;
	}

	/**
	 * @des 默认返回成功   MessageCode.SUCCESS.getMsgCode()
	 * @return Integer
	 */
	public Integer getCode() {
		if(null == code) {
			return MessageCode.SUCCESS.getMsgCode();
		}
		return code;
	}
	/**
	 * @des 默认返回成功
	 * @return
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * @des 默认返回成功  MessageCode.SUCCESS.getMessage()
	 * @return String
	 */
	public String getMsg() {
		if(StringUtils.isBlank(msg) && getCode() == MessageCode.SUCCESS.getMsgCode()) {
			return MessageCode.SUCCESS.getMessage();
		}else if(StringUtils.isBlank(msg) && null != getCode()){
			return MessageCode.getMessage(getCode());
		}
		return msg;
	}

	/**
	 * @des 默认返回成功
	 * @return
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @des 返回结果
	 * @return Object
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @return 返回结果
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * @des 返回自定义扩展信息
	 * @return String
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @return 返回自定义扩展信息
	 */
	public void setResult(String result) {
		this.result = result;
	}
	
	public static ResultBean failedMsg(String msg) {
		ResultBean rb = new ResultBean();
		rb.setMsg(msg);
		rb.setCode(MessageCode.ERROR.getMsgCode());
		return rb;
	}
	
	public static ResultBean success(Object data) {
		ResultBean rb = new ResultBean();
		rb.setData(data);
		rb.setCode(MessageCode.SUCCESS.getMsgCode());
		return rb;
	}
	
	public static ResultBean successMsg(String msg) {
		ResultBean rb = new ResultBean();
		rb.setMsg(msg);
		rb.setCode(MessageCode.SUCCESS.getMsgCode());
		return rb;
	}
}
