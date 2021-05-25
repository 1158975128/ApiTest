package com.zw.admin.server.dto;

import lombok.Data;

/**
 *功能描述:时间类型枚举
 * @author larry
 * @Date 2020/11/17 11:24
 */
public enum DateTypeEnum {

    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month");

    private String dateType;

    DateTypeEnum(String dateType)
    {
        this.dateType = dateType;
    }

    public String getDateType(){
        return dateType;
    }


}
