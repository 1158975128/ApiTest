package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Log extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String machineId;
	private String userId;
	private String msg;

}
