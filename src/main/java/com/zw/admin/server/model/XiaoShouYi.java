package com.zw.admin.server.model;

import lombok.Data;

/**
 *功能描述:销售易参数包装类
 * @author larry
 * @Date 2020/11/20 15:54
 */
@Data
public class XiaoShouYi extends BaseEntity<Long> {

	//连接器id
    private String clientId;
    //连接器密码
    private String clientSecret;
    //回调地址
    private String redirectUri;
    //用户名
    private String username;
    //密码
    private String password;
    //销售易安全令牌
    private String securityToken;
    //获取token地址
    private String tokenUrl;
    //超时时间
    private Integer timeOut;
    //连接器名称
    private String connectName;

}