package com.zw.admin.server.model;

import lombok.Data;

/**
 *功能描述:场所
 * @author larry
 * @Date 2020/11/5 16:10
 */
@Data
public class Place extends BaseEntity<Long> {
	//场所
	private String place;
	private String createUser;
	private String updateUser;

}