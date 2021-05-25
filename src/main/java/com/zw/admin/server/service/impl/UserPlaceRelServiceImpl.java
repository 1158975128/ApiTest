package com.zw.admin.server.service.impl;

import com.zw.admin.server.dao.UserPlaceRelDao;
import com.zw.admin.server.dto.UserPlaceDto;
import com.zw.admin.server.service.UserPlaceRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author larry
 * @Description:
 * @date 2020/11/5 15:22
 */
@Service
public class UserPlaceRelServiceImpl implements UserPlaceRelService {

    @Resource
    private UserPlaceRelDao userPlaceRelDao;

    /**
     * 功能描述:添加用户和场所关系
     *
     * @param userPlaceDto
     * @return java.lang.Integer
     * @author larry
     * @Date 2020/11/5 15:27
     */
    @Transactional
    public Integer save(UserPlaceDto userPlaceDto) {
        userPlaceRelDao.removeRelByUserId(userPlaceDto);
        return userPlaceRelDao.saveUserPlaceRelBatch(userPlaceDto);
    }
}
