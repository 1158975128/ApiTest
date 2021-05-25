package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @author larry
 * @Description:设备配置
 * @date 2020/9/14 10:09
 */
@Data
public class DeviceConfig {

    private String sn;
    private Date createTime;

    private Date updateTime;

    //场所
    private String place;

    //卡号
    private String cardNumber;

    //路由id
    private String routeId;
}
