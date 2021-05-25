package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;

/**
 *功能描述:用户存档
 * @author larry
 * @Date 2020/10/22 19:02
 */
@Data
public class UserArchive {

    private Long id;

	//用户id
    private String userId;
    //机器类型
    private String machineType;
    //存档Key
    private String key;
    //存档内容
    private String archive;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;


}