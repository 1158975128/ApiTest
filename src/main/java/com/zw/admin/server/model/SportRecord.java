package com.zw.admin.server.model;

import lombok.Data;

@Data
public class SportRecord extends BaseEntity<Long> {
	private String uid;
	private String eid;
	private String rid;
	private String val;

}