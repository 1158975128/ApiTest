package com.zw.admin.server.dto;

import com.zw.admin.server.model.Place;
import lombok.Data;

import java.util.List;

/**
 * @author larry
 * @Description:
 * @date 2020/11/5 11:33
 */
@Data
public class UserPlaceDto {

    //用户id
    private String userId;

    private List<Place> places;
}
