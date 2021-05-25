package com.zw.admin.server.model;

import lombok.Data;

/**
 *功能描述:用户评估数据同步
 * @author larry
 * @Date 2020/10/22 19:02
 */
@Data
public class UserTrainEvaluation extends BaseEntity<Long> {

	//评估id
    private String evaluationId;
    //用户id
    private String userId;
    //机器sn
    private String machineId;
    //评估类型
    private Integer type;
    //评估数据
    private String data;


}