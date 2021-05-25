package com.zw.admin.server.config;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Data;

@Data
public class CustomToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 8527195102543157511L;

	private String phone;

	private String email;

	private String isRemember;

	private String isTemporary;

	private String machineId;

	private String openid;

	private String userId;

	public CustomToken() {
		super();
	}

	public CustomToken(final String username, final String password, final Boolean rememberMe, final String phone,
			final String email, final String isRemember, final String isTemporary, final String machineId,final String openid) {
		super(username, password, rememberMe, null);
		this.setPhone(phone);
		this.setEmail(email);
		this.setIsRemember(isRemember);
		this.setIsTemporary(isTemporary);
		this.setMachineId(machineId);
		this.setOpenid(openid);
	}

}
