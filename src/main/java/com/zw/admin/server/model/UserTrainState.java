package com.zw.admin.server.model;

import lombok.Data;

import java.util.Date;
/**
 *功能描述:用户统计信息
 * @author larry
 * @Date 2020/7/6 15:49
 */
@Data
public class UserTrainState {

	/*
	 *id
	 */
	private Long id;
	/*
	 *用户id
	 */
    private String userId;
	/*
	 *机器类型
	 */
    private String machineType;
	/*
	 *训练总时间
	 */
    private String trainTime;
	/*
	 *训练总移动距离
	 */
    private String trainDistance;
	/*
	 *训练用力次数
	 */
    private String trainExert;
	/*
	 *创建时间
	 */
    private String createTime;
	/*
	 *更新时间
	 */
    private String updateTime;
	/*
	 *用户游戏得分
	 */
    private String userGameGrade;

}