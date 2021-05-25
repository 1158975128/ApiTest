package com.zw.admin.server.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ResponseInfo implements Serializable {

	private static final long serialVersionUID = -4417715614021482064L;

	private int code;
	private String msg;
	private Map<Object, Object> data = new HashMap<>();

	public ResponseInfo(int code, String msg) {
		super();
		this.code = code;
		this.setMsg(msg);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<Object, Object> getData() {
		return data;
	}

	public void setData(Map<Object, Object> data) {
		this.data = data;
	}

}
