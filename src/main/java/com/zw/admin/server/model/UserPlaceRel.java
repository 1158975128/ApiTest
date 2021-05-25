package com.zw.admin.server.model;

import lombok.Data;

/**
 *功能描述:用户与场所关系表
 * @author larry
 * @Date 2020/10/26 17:39
 */
@Data
public class UserPlaceRel extends BaseEntity<Long> {

	//用户id
    private String userId;
    //场所
    private String place;
    //场所id
    private Long placeId;

}