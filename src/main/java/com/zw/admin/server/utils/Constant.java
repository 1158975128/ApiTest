package com.zw.admin.server.utils;

import java.util.UUID;

/**
 * @Author: ZhangAnCheng
 * @Description: Constant
 * @Date: 13:11 2018/6/26
 */
public class Constant {
    public static final String JWT_ID = UUID.randomUUID().toString();

    /**
     * 加密密文
     */
    public static final String JWT_SECRET = "Authorization";

    /**
     * 测试通关秘钥
     */
    public static final String PASS_SECRET_KEY = "TEC_DEV";

    /**
     * 毫秒
     */
    public static final int JWT_TTL = 60*60*1000;
    
    public static final String REQUEST_TOKEN_PARAM_NAME = "Authorization";

    //治疗师
    public static Long THERAPIST = 1L;
    //医院管理人员
    public static Long ORGANIZATION_ADMIN = 2L;

    public static Integer NON_CHECK = 0;

    public static Integer IS_CHECK = 0;

    public static Integer EMAIL_CHECK = 1;

    public static Integer PHONE_CHECK = 2;

    public static Integer ALL_CHECK = 3;

    //患者
    public static Long PATIENT = 0L;
    //系统管理员
    public static Long DEVELOPER = 3L;
    //超级管理员
    public static Long ROOT = 4L;
    //用户删除
    public static Integer IS_DELETE=1;

}
