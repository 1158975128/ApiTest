package com.zw.admin.server.model;

import lombok.Data;

@Data
public class DataCollection extends BaseEntity<Long> {

	private String uid;
	private String eid;
	private String etype;
	private String rid;
	private String val;
	private String startTime;
	private String endTime;
	//范围大小，0，自定义 1，大 2，中 3，小
	private String rangeIndex;
	//用户名
	private String name;

}
