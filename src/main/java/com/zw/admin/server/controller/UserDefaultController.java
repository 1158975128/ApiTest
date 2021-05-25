package com.zw.admin.server.controller;

import com.zw.admin.server.dao.UserDefaultDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserDefault;
import com.zw.admin.server.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author larry
 * @Description:用户默认游戏参数控制器
 * @date 2020/6/17 17:21
 */
@Slf4j
@Api(tags = "用户默认游戏数据")
@RestController
@RequestMapping("/userDefault")
public class UserDefaultController {

    @Resource
    private UserDefaultDao userDefaultDao;

    /**
     * 功能描述:同步用户默认数据
     *
     * @param userDefault
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/6/18 11:08
     */
    @ApiOperation(value = "同步用户默认数据")
    @PostMapping("/saveAndUpdate")
    public ResultVO<?> sync(UserDefault userDefault) {
        if(userDefault==null){
            return R.error(1, "参数错误");
        }
        log.info("userDefault:"+userDefault.toString());
        String userId = userDefault.getUserId();
        String defaultRobotType = userDefault.getDefaultRobotType();
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(defaultRobotType)){
            return R.error(1, "参数错误");
        }
        //判断数据库中是否存在，如果不存在添加，否者更新
        UserDefault userDefault1 = userDefaultDao.selectOneUserDefault(userDefault);
        if(userDefault1!=null){
            //更新
            userDefaultDao.updateUserDefault(userDefault);
        }else {
            //添加
            userDefaultDao.insertUserDefault(userDefault);
        }
        return R.success();
    }

    /**
     * 功能描述:根据机器类型和用户有id查询用户默认数据
     *
     * @param userDefault
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/6/18 11:21
     */
    @ApiOperation(value = "根据机器类型和用户有id查询用户默认数据")
    @PostMapping("/selectOneUserDefault")
    public ResultVO<?> selectOneUserDefault(UserDefault userDefault) {
        userDefault = userDefaultDao.selectOneUserDefault(userDefault);
        return R.success(userDefault);
    }

}
