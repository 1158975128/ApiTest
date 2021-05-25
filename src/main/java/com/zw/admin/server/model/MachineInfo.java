package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 * @author larry
 * @Description:机器信息
 * @date 2020/6/19 8:52
 */
@Data
public class MachineInfo {

    private Long id;

    //机器类型
    private String machineType;

    //底层版本号
    private String algorithmVersion;

    //硬件版本号
    private String hardwareVersion;

    //机械版本号
    private String mechanismVersion;

    //软件版本号
    private String softwareVersion;

    //序列号
    private String sn;

    //机构名称
    private String organization;

    private Date createTime;

    //unity新增字段
    //小电脑id
    private String osId;
    //运行时长
    private Integer runTime;
    //运行次数
    private Integer runTimes;
    //其他数据
    private String data;
    //app版本
    private String mmuAppVer;
    //iap版本
    private String mmuIapVer;
    //rups版本
    private String rupsVer;
    //机械版本
    private String mechanismVer;
    //硬件版本
    private String hardwareVer;

    //场所
    private String place;

    //场所id
    private Long placeId;

    //卡号
    private String cardNumber;

    //路由id
    private String route;
    //更新时间
    private Date updateTime;
    //更新人
    private String updateUser;
    //省份
    private String province;
    //销售序列号
    private String serialNumber;
    //ip
    private String ip;

}
