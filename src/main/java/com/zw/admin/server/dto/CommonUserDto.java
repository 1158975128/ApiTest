package com.zw.admin.server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author larry
 * @Description:
 * @date 2020/5/12 14:10
 */
@Data
public class CommonUserDto {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String id;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    // @NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, UpdateGroup.class })
    private String email;

    /**
     * 状态 0：禁用 1：正常
     */
    private String status;

    /**
     * 姓名(昵称?)
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     *
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    /**
     * 性别
     */
    private String sex;
    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *
     */
    private String updateUser;
    /**
     *
     */
    private String parentId;

    /**
     * 职称
     */
    private Long rank;
    /**
     * 职务
     */
    private String duty;
    /**
     * 科室
     */
    private Long office;
    /**
     * 部门
     */
    private String department;
    /**
     * 机构id
     */
    private Long organization;
    /**
     * 修改时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 岗位
     */
    private List<Long> postType;
    /**
     * 岗位小类
     */
    private List<Long> postSmallType;
    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

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

    //以下为合并patient后增加字段
    /**
     * 身份证号
     */
    private String identityNumber;

    /**
     * 职业
     */
    private Long profession;

    /**
     * 社保号
     */
    private String socialSecurityNumber;

    /**
     *年龄
     */
    private Long age;

    /**
     * 婚姻
     */
    private Long marriage;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 关系
     */
    private Long relation;

    /**
     * 联系电话
     */
    private String linkmanPhone;

    /**
     *创建人
     */
    private String createUser;

    /**
     *创建id
     */
    private String createUserId;

    // 1手动0自动
    private Integer isAuto;

    /**
     * 疗程中来源
     */
    private Long sourceId;


    /**
     * 关联所有医生名
     */
    private String doctorIdsName;


    /**
     * 病人生日
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date birthdate;

    /**
     * 用户来源
     */
    private Long source;

    /**
     * 是否是病人
     */
    private Long isPatient;

    //补充v4项目中用户的字段
    /**
     * 用户账号
     */
    private String username;

    /**
     * 头像
     */
    private String headImage;

    private Integer praiseCountbymileage;

    private Integer praiseCountbytime;

    /**
     * 是否删除
     */
    private Integer isdelete;

    /**
     * 体重
     */
    private Double weight;

    /**
     * 身高
     */
    private Integer height;

    private String isPush;

    /**
     * 最后登录时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 名字
     */
    private String firstName;

    /**
     * 姓氏
     */
    private String lastName;

    /**
     * 组织
     */
    private String institution;

    /**
     * 国家
     */
    private String country;

    /**
     * 名字
     */
    private String isRemember;

    /**
     * 0正常注册号1离线注册号
     */
    private String isTemporary;

    /**
     * 机器id
     */
    private String machineId;

    /**
     * 0病人1治疗室2管理员3开发者4超级管理员5普通用户
     */
    private Long identity;

    /**
     * 0病人1治疗室2管理员3开发者4超级管理员5普通用户
     */
    private String badposition;

    /**
     *备注
     */
    private String remark;

    private String answer1;

    private String answer2;

    private String userTrajectory;

    private String userLanguage;

    private String userStyle;

    /**
     *用户来源的系统，1代表用户来源于瑞和康，2代表用户来源于V4系统
     */
    private int resourceSys;
}
