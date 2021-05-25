package com.zw.admin.server.model;

import java.util.Date;

import lombok.Data;

@Data
public class Test {

	private Long id;
	private String username;
	private String score;
	private Date time;
	private Long testId;

}
