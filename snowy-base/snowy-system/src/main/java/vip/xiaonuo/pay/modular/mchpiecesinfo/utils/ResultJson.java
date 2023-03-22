package vip.xiaonuo.pay.modular.mchpiecesinfo.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Map;

public class ResultJson implements Serializable {
	private static final long serialVersionUID = -1564214038613217530L;
	/**
	 * 提示信息
	 */
	private String msg = "error";// 提示信息
	/**
	 * 提示代码 0 为成功
	 */
	private String code = "000001";// 提示代码
	/**
	 * 其他数据信息
	 */
	private Map<String, Object> data;//数据

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 将json数据转化为String输出
	 * @return
	 */
	public String getJsonStr(){
		JSONObject obj = new JSONObject();
		obj.put("msg", this.getMsg());
		obj.put("code", this.getCode());
		if(this.data!=null){
			obj.put("data", this.data);
		}
		return obj.toJSONString();
	}
	
	public void setCodeAndMsg(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}

