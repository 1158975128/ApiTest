package com.zw.admin.server.model;

import lombok.Data;

/**
 * @author larry
 * @Description:患者评估数据包装类
 * @date 2021/1/12 15:11
 */
@Data
public class UserTrainEvaluationBo extends UserTrainEvaluation{

    private String userName;

    private String machineType;

}
