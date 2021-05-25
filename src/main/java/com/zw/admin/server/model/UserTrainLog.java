package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 *功能描述:用户训练日志同步
 * @author larry
 * @Date 2020/10/22 19:04
 */
@Data
public class UserTrainLog extends BaseEntity<Long> {

	//训练日志id
    private String trainLogId;
    //用户id
    private String userId;
    //机器id
    private String machineId;
    //游戏id
    private Integer gameId;
    //机器数据
    private String machineData;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;
	//游戏数据
    private String gameData;
    //训练时长
    private Integer time;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //机器型号
    private String machineType;
    //游戏名
    private String gameName;
    //用户名
    private String userName;
}