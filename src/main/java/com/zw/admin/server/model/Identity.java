package com.zw.admin.server.model;

import lombok.Data;
import lombok.Setter;

/**
 * @author larry
 * @Description:身份枚举
 * @date 2020/9/15 16:12
 */
public enum Identity {

    PATIENT(0, "病人"), THERAPIST(1, "治疗师"), ADMIN(2, "管理者"),
    CODER(3, "开发者"), SUPER_ADMIN(4, "超级管理员");

    private int identity;

    private String desc;

    private Identity(int identity, String desc) {
        this.identity = identity;
        this.desc = desc;
    }

    public int getIdentity() {
        return identity;
    }

    public String getDesc() {
        return desc;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
