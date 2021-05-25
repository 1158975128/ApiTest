package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Pic extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String k;
	private String pic;
	private Integer version;

}
