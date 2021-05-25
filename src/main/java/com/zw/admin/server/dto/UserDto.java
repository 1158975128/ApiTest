package com.zw.admin.server.dto;

import java.util.List;

import com.zw.admin.server.model.Place;
import com.zw.admin.server.model.Role;
import com.zw.admin.server.model.User;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class UserDto extends User {

	private static final long serialVersionUID = -184009306207076712L;

	private String verificationCode;

	//private List<Long> placeIds;

	private List<Role> roles;

	private List<Place> places;
	//转在线时候code
	private String activeCode;
	//版本号
	private String version;

}
