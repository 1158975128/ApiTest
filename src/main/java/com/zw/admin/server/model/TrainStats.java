package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 *功能描述:用户训练统计
 * @author larry
 * @Date 2020/10/22 19:01
 */
@Data
public class TrainStats extends BaseEntity<Long> {

	//用户id
    private String userId;
    //机器类型
    private String machineType;
    //机器运行时长
    private Integer trainTime;
    //机器移动距离
    private Integer trainDistance;
    //机器运行次数
    private Integer trainTimes;
    //机器运行天数
    private Integer trainDays;
    //用户用力次数
    private Integer forceTimes;
    //用户总得分
    private Integer totalScore;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;

}