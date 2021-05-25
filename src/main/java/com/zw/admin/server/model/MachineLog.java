package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class MachineLog extends BaseEntity<Long> {

	//日志id
    private String logId;
    //机器Id
    private String machineId;
    //日志数据
    private String data;
    //MMU app版本
    private String mmuAppVer;
    //mmu Iap 版本
    private String mmuIapVer;
	//Rups版本
    private String rupsVer;

}