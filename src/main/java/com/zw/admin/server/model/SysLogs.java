package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class SysLogs extends BaseEntity<Long> {

	private static final long serialVersionUID = -7809315432127036583L;
	private User user;
	/*
	 *用户操作
	 */
	private String module;
	private Boolean flag;
	private String remark;
	/*
	 *用户
	 */
	private String username;
	// 请求方法
	private String method;
	// 请求参数
	private String params;
	// 执行时长(毫秒)
	private Long time;
	// IP地址
	private String ip;

}
