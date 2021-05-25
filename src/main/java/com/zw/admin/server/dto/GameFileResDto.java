package com.zw.admin.server.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author larry
 * @Description:游戏文件响应包装类
 * @date 2020/11/25 13:49
 */
@Data
public class GameFileResDto{

    private String name;

    private String platformId;

    private String sn;

    private String type;

    private Long version;
}
