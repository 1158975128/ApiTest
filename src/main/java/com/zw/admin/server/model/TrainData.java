package com.zw.admin.server.model;

import lombok.Data;

/**
 * @author larry
 * @Description:
 * @date 2020/11/16 15:35
 */
@Data
public class TrainData {

    //场所数量
    private Long placeNum;
    //机器数量
    private Long machineNum;
    //用户数量
    private Long userNum;
    //训练次数
    private Long trainNum;
    //训练时长
    private Long trainTime;

}
