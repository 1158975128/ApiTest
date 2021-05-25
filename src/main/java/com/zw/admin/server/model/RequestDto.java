package com.zw.admin.server.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author larry
 * @Description:
 * @date 2020/4/30 17:44
 */
@Getter
@Setter
public class  RequestDto<T> {
    int page;
    int num;
    T params;
}
