package com.zw.admin.server.model;

import lombok.Data;

@Data
public class UserTrajectory {

	private Long id;
	private String trajectoryid;
	private String creatorid;
	private String modifyid;
	private String name;
	private String machinetype;
	private String image;
	private Integer weight;
	private Integer height;
	private String point;
	private String createdate;
	private String updatedate;

}
