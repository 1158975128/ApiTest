package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Information extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1094454125296307484L;
	private String name;
	private String value;

}
