package com.zw.admin.server.model;

import lombok.Data;

@Data
public class DeviceType extends BaseEntity<Long> {

	private String name;
	private Long parentId;

	private String parentName;
}
