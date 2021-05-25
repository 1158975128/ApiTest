package com.zw.admin.server.model;

import lombok.Data;
/**
 *功能描述:用户成就实体类
 * @author larry
 * @Date 2020/7/6 15:49
 */
@Data
public class UserAchivement {

	/*
	 *id
	 */
	private Long id;
	/*
	 *用户id
	 */
    private String userId;
	/*
	 *成就id
	 */
    private String achivementId;
	/*
	 *进度
	 */
    private String process;
	/*
	 *状态，0没触发，1已经触发过
	 */
    private String state;
	/*
	 *创建时间
	 */
    private String createTime;
	/*
	 *更新时间
	 */
    private String updateTime;

}