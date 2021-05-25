package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 *功能描述:用户训练报表同步
 * @author larry
 * @Date 2020/10/22 19:13
 */
@Data
public class UserTrainReport extends BaseEntity<Long> {

	//训练日志id
    private String reportId;
    //用户id
    private String userId;
    //机器id
    private String machineId;
    //训练日志id
    private String trainLogId;
    //游戏id
    private Integer gameId;
    //游戏类型
    private Integer type;
    //游戏模式
    private Integer mode;
    //游戏分类
    private Integer category;
    //训练数据
    private String data;
    //机器型号
    private String machineType;
    private Date createTime;
    private Date updateTime;
    //条数
    private Integer limit;
}