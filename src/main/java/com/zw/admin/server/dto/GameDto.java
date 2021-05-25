package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:游戏包装类
 * @date 2020/11/17 10:53
 */
@Data
public class GameDto {
    //游戏Id
    private long id;
    //游戏名称
    private String gameName;
    //日期
    private String date;
    //数量
    private int num;
}
