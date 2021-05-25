package com.zw.admin.server.dto;

import lombok.Data;

import java.util.List;

/**
 * @author larry
 * @Description:请求销售易后sn包装类
 * @date 2020/11/21 16:27
 */
@Data
public class SerialNumberDto {

    private String id;

    private String name;

    private List<String> machineTypes;

    private String sn;

    private String createdAt;

    private String updatedAt;

    private String hardwareVer;

    private String mechanismVer;

    private String softwareVersion;

    private String algorithmVersion;

    private String machineType;

    private String place;



}
