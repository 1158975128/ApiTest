package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:销售易xoql返回结果包装
 * @date 2020/11/20 16:23
 */
@Data
public class XoqlDto {

    //响应码
    private String code;
    //响应信息
    private String msg;
    //报错信息
    private String errorInfo;
    //响应数据
    private Object data;
}
