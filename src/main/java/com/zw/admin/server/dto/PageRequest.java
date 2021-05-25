package com.zw.admin.server.dto;

import lombok.Data;

/**
 * @author larry
 * @Description:分页请求包装类
 * @date 2020/11/5 17:31
 */
@Data
public class PageRequest<T> {

    //第几页
    private Integer page;

    //每页多少条
    private Integer num;

    //查询条件
    private T params;
}
