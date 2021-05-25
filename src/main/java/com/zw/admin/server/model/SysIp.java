package com.zw.admin.server.model;

import lombok.Data;

@Data
public class SysIp extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8519524680317281344L;
	private String name;
	private String ip;
	private String frontIp;

}
