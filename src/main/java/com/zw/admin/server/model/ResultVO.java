package com.zw.admin.server.model;

import lombok.Data;

/**
 * 返回值
 *
 *
 */
@Data
public class ResultVO<T> {

	/** 状态码 */
	private Integer code;

	/** 状态信息 */
	private String msg;

	/** 返回数据 */
	private T data;

}
