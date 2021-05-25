package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:每天对应数量
 * @date 2020/10/21 15:26
 */
@Data
public class DateNumDto {

    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Integer num;

    //坐标
   private String coordinate;
}
