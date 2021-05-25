package com.zw.admin.server.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author larry
 * @Description:游戏文件请求DTO
 * @date 2020/11/25 11:45
 */
@Data
public class GameFileReqDto implements Serializable {

    //游戏id
    private Long id;
    //是否是默认游戏
    private Integer isDefault;
    //平台
    private String platform;
    //机型
    private String type;
}
