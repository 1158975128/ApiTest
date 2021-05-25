package com.zw.admin.server.model;

import lombok.Data;

/**
 * @author larry
 * @Description:省份
 * @date 2020/10/26 9:59
 */
@Data
public class Province {

    private Long id;

    private String province;

    //坐标
    private String coordinate;
}
