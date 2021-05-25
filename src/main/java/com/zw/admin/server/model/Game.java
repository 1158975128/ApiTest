package com.zw.admin.server.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Game extends BaseEntity<Long> {

	/**
	 *
	 */
	private static final long serialVersionUID = -8839864977379809016L;
	private String userId;
	@NotBlank
	private String name;
	private String gamename;
	private String modulename;
	private String pic;
	private String[] pics;
	private String description;
	private String type;
	private String mode;
	private String categoty;
	private String deviceType;
	private String icon;
	private Integer isDefault = 0;
	private Integer status;

	private String statusStr = "-";
	private int ugStatus;

	// 0申请中1审核未通过2批准中3批准未通过4批准通过
	public void setStatus(Integer status) {
		this.status = status;
		if (status != null) {
			switch (status) {
			case 0:
				statusStr = "申请中";
				break;
			case 1:
				statusStr = "审核未通过";
				break;
			case 2:
				statusStr = "批准中";
				break;
			case 3:
				statusStr = "批准未通过";
				break;
			case 4:
				statusStr = "批准通过";
				break;
			default:
				break;
			}
		}
	}

}
