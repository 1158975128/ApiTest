package com.zw.admin.server.dto;

/**
 * @author larry
 * @Description:查询类型枚举
 * @date 2020/11/17 11:30
 */
public enum CountTypeEnum {

    //次数
    TIMES("times"),
    //时长
    TIME("time"),
    //患者数
    PATIENT("patient");

    private String countType;

    CountTypeEnum(String countType) {
        this.countType = countType;
    }

    public String getCountType(){
        return countType;
    }
}
