package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Dbereport extends BaseEntity<String> {

	private String content;
	private String date;
	private String image;
	private String mode;
	private String result;
	private Integer time;
	private String userId;

}
