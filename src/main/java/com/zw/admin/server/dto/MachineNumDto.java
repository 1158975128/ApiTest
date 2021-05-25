package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:
 * @date 2020/10/26 16:27
 */
@Data
public class MachineNumDto {

    private String machineType;

    private Long num;

    private Long time;

    //百分比
    private String rate;
}
