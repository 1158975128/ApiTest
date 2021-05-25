package com.zw.admin.server.model;

import lombok.Data;

/**
 * @author larry
 * @Description:训练数据汇总
 * @date 2020/8/18 16:12
 */
@Data
public class TrainDataSum {

    //机器类型数量
    private Long machineTypeNum;
    //sn号数量
    private Long snNum;
    //患者数量
    private Long userNum;
    //训练次数
    private Long trainNum;
    //训练时长
    private Long trainTime;


}
