package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;
/**
 *功能描述:用户登录机器日志表
 * @author larry
 * @Date 2020/10/29 13:58
 */
@Data
public class UserLog extends BaseEntity<Long> {
    private String userId;
    private String machineId;
    private String machineType;
    private String ip;
    private String rupsVer;
    private Integer time;
    //记录id
    private String logId;

}