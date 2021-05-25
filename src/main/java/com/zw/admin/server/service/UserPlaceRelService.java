package com.zw.admin.server.service;

import com.zw.admin.server.dto.UserPlaceDto;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 功能描述:用户设备关联表
 *
 * @author larry
 * @Date 2020/11/5 15:20
 */
public interface UserPlaceRelService {

    /**
     * 功能描述:添加用户和场所关系
     *
     * @param userPlaceDto
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/11/5 15:27
     */
    Integer save(UserPlaceDto userPlaceDto);


}
