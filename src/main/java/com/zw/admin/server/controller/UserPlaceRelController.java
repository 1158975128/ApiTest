package com.zw.admin.server.controller;

import java.util.List;

import com.zw.admin.server.dao.UserDao;
import com.zw.admin.server.dto.UserPlaceDto;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.User;
import com.zw.admin.server.utils.Constant;
import com.zw.admin.server.utils.R;
import com.zw.admin.server.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.zw.admin.server.dao.UserPlaceRelDao;
import com.zw.admin.server.model.UserPlaceRel;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户与场所关系控制器
 * @author larry
 * @Date 2020/10/26 17:53
 */
@RestController
@RequestMapping("/userPlaceRel")
public class UserPlaceRelController {

    @Resource
    private UserPlaceRelDao userPlaceRelDao;
    @Resource
    private  UserDao userDao;

    /**
     * 功能描述:添加用户与场所关系
     *
     * @param userPlaceDto
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/5 15:33
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO<?> save(@RequestBody UserPlaceDto userPlaceDto) {
        userPlaceRelDao.saveUserPlaceRelBatch(userPlaceDto);
        //根据userId删除原有关系
        return R.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public ResultVO<?> update(@RequestBody UserPlaceRel userPlaceRel) {
        userPlaceRelDao.update(userPlaceRel);
        return R.success();
    }

    /**
     * 功能描述:查询场所下拉框
     *
     * @param
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/11/7 14:54
     */
    @GetMapping("/getPlaces")
    @ApiOperation(value = "查询场所下拉框")
    public ResultVO<?> getPlaces() {
        //当前用户
        User activeUser = UserUtil.getCurrentUser();
        if (activeUser == null) {
            return R.success();
        } else {
            if (StringUtils.isNotBlank(activeUser.getId())) {
                List<Long> roleIds = userDao.selectRoleIdByUserId(activeUser.getId());
                if (!CollectionUtils.isEmpty(roleIds)) {
                    if (roleIds.contains(Constant.ROOT)) {
                        //如果是超级管理员查询所有
                        return R.success(userPlaceRelDao.selectPlaceByUserId(null));
                    } else if (roleIds.contains(Constant.ORGANIZATION_ADMIN)) {
                        //如果是医院管理员
                        return R.success(userPlaceRelDao.selectPlaceByUserId(activeUser.getId()));
                    }
                }
            }
            return R.success(null);
        }
    }

}