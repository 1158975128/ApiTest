package com.zw.admin.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author larry
 * @Description:
 * @date 2020/5/15 9:59
 *
 */
@Setter
@Getter
public class ResponseDto {

    private Integer total;

    private List<?> list;
}

