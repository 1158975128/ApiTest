package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Dict extends BaseEntity<Long> {

	private static final long serialVersionUID = -2431140186410912787L;
	private String type;
	private String k;
	private String val;

}
