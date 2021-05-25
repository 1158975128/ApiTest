package com.zw.admin.server.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class User extends BaseEntity<String> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String username;
	private String phone;
	private String password;
	private Integer age;
	private String sex;
	private String headImage;
	private String name;
	private Integer praiseCountbymileage;
	private Integer praiseCountbytime;
	private String updateUser;
	private Integer isDelete;
	private Double weight;
	private Integer height;
	private String isPush;
	private Date lastLoginTime;
	private String firstName;
	private String lastName;
	private String email;
	private String institution;
	private String country;
	private String status;
	private String parentId;
	private String isRemember;
	private String isTemporary;
	private String machineId;
	private Long identity;
	private String badposition;
	private String remark;
	private String answer1;
	private String answer2;
	private String userTrajectory;
	private String userLanguage;
	private String userStyle;

	private String gameIds;

	private String statusStr = "-";

	public interface Status {
		String DISABLED = "0";
		String VALID = "1";
		String UNLOCKED = "2";
		String LOCKED = "3";
	}

	// 0申请中1审核未通过2批准中3批准未通过4批准通过
	public void setStatus(String status) {
		this.status = status;
		if (status != null) {
			switch (status) {
			case "0":
				statusStr = "未申请";
				break;
			case "1":
				statusStr = "申请中";
				break;
			case "2":
				statusStr = "未通过";
				break;
			case "3":
				statusStr = "通过";
				break;
			default:
				break;
			}
		}
	}

	/**
	 *用户来源的系统，1代表用户来源于瑞和康，2代表用户来源于V4系统
	 */
	private Integer resourceSys;

	private Date birthday;

	private Date createTime;

	/**
	 * 是否在线验证，0为离线，1为在线
	 */
	private Integer isChecked;
	/**
	 *病例号
	 */
	private String anamnesisid;
	/**
	 *验证字段
	 */
	private Integer checkColumn;

	private List<Long> roleIds;

	/**
	 *在线Id
	 */
	private String onlineId;

	/**
	 *微信openId
	 */
	private String openid;

}
