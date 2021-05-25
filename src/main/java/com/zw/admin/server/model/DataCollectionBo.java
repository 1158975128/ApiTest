package com.zw.admin.server.model;

import lombok.Data;

/**
 * @author larry
 * @Description:包装用户数据的业务类
 * @date 2020/8/19 16:00
 */
@Data
public class DataCollectionBo extends DataCollection {
    //游戏id
    private String gameId;
    //游戏名称
    private String gameName;
    //产生报告时间
    private String date;
    //训练时长
    private Long trainTime;
    //训练数据
    private String rawData;
    //采集频率
    private Long duration;
    //游戏范围
    private String rangeInfo;
    //是否有训练数据
    private Integer haveData;


}
