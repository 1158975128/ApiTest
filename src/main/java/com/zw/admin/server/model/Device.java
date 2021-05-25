package com.zw.admin.server.model;

import java.util.Date;

import lombok.Data;

@Data
public class Device {

	private Long id;

	private String sn;
	private String os;
	private String ip;
	private String status;
	private Date createTime;

	private Date updatetime;


	private Integer uptime;

	//场所
	private String place;

	//卡号
	private String cardNumber;

	//路由id
	private String routeId;

	//用户id
	private String userId;
	//软件版本
	private String rupsVer;
	//登录时长
	private Long time;
	//设备sn
	private String machineId;

}
