package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:
 * @date 2020/10/27 10:57
 */
@Data
public class GameNumDto {

    /**
     * 日期
     */
    private String game;

    /**
     * 次数
     */
    private Long num;

    /**
     * 用时
     */
    private Long time;

    /**
     * 设备型号
     */
    private String machineType;
}
