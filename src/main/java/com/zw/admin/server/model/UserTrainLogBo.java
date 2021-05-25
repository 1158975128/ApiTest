package com.zw.admin.server.model;

import lombok.Data;

import java.util.List;

/**
 * @author larry
 * @Description:训练日志包装类
 * @date 2020/11/12 16:27
 */
@Data
public class UserTrainLogBo extends UserTrainLog{

    //设置
    private String rangeInfo;

    //范围
    private String rangeIndex;

    //sn集合
    private List<String> machineIds;

    //管理患者集合
    private List<String> userIds;

    private String place;

    //时间类型，day,hour,week,month
    private String dateType;

    //统计类型,times,time,patient
    private String countType;

    private String userName;

    //在线id
    private String onlineId;

    private String userId;

}
