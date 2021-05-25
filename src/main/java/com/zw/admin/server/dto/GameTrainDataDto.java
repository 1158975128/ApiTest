package com.zw.admin.server.dto;

import lombok.Data;

import java.util.List;

/**
 * @author larry
 * @Description:游戏训练
 * @date 2020/11/18 10:06
 */
@Data
public class GameTrainDataDto {

    private String date;

    private List<GameDto> gameDtoList;
}
