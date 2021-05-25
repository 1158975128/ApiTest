package com.zw.admin.server.enums;

/**
 * 文件状态
 * @Description:
 * @author: squall
 * @date:  2021/4/12 14:43
 * @version V1.0
 */
public enum FileStatusEnum {
    /**0申请中*/
    APPLYING(0),
    /**1审核未通过*/
    CHECKING(1),
    /**2批准中*/
    PROCESSING(2),
    /**3批准未通过*/
    APPROVED(3),
    /**4批准通过*/
    UNAPPROVED(4);

    FileStatusEnum(int value) {
        this.value = value;
    }
    private int value;

    static FileStatusEnum ofValue(Integer value) {
        switch (value) {
            case 0: return APPLYING;
            case 1: return CHECKING;
            case 2: return PROCESSING;
            case 3: return APPROVED;
            case 4: return UNAPPROVED;
        }
        return null;
    }
}
