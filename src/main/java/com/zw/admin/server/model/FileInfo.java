package com.zw.admin.server.model;

import lombok.Data;

@Data
public class FileInfo extends BaseEntity<Long> {

	private static final long serialVersionUID = -5761547882766615438L;

	private String contentType;
	private long size;
	private String path;
	private String url;
	private String type;
	private String sn;
	private String name;
	private Long version;
	private int code = 0;
	private String platform;
	private String platformId;
	private Integer status;

	private String statusStr = "-";

	// 0申请中1审核未通过2批准中3批准未通过4批准通过
	public void setStatus(Integer status) {
		this.status = status;
		if (status != null) {
			switch (status) {
			case 0:
				statusStr = "申请中";
				break;
			case 1:
				statusStr = "审核未通过";
				break;
			case 2:
				statusStr = "批准中";
				break;
			case 3:
				statusStr = "批准未通过";
				break;
			case 4:
				statusStr = "批准通过";
				break;
			default:
				break;
			}
		}
	}

}
