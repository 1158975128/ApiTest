package com.zw.admin.server.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author larry
 * @Description:
 * @date 2020/11/16 17:53
 */
@Data
public class GameTrainReqDto {

    //时间类型，day,hour,week,month
    private String dateType;

    //开始时间
    private String startTime;

    //结束时间
    private String endTime;

    //统计类型,times,time,patient
    private String countType;
}
