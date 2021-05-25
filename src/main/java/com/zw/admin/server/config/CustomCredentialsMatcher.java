package com.zw.admin.server.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//	String string = new String(token.getPassword());
//	String credentials = (String) info.getCredentials();
//	
//	boolean s=new String(token.getPassword()).equals(info.getCredentials());
		return equals(token, info);
	}
}