package com.zw.admin.server.controller;

import java.util.ArrayList;
import java.util.List;

import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.page.table.PageTableRequest;
import com.zw.admin.server.page.table.PageTableHandler;
import com.zw.admin.server.page.table.PageTableResponse;
import com.zw.admin.server.page.table.PageTableHandler.CountHandler;
import com.zw.admin.server.page.table.PageTableHandler.ListHandler;
import com.zw.admin.server.dao.UserAchivementDao;
import com.zw.admin.server.model.UserAchivement;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户成就控制层
 * @author larry
 * @Date 2020/7/6 15:49
 */
@Slf4j
@RestController
@RequestMapping("/userAchivements")
public class UserAchivementController {

    @Resource
    private UserAchivementDao userAchivementDao;

    /**
     * 功能描述:保存或者更新用户成就
     *
     * @param userAchivement
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/7/6 15:59
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation(value = "保存")
    public ResultVO<?> saveOrUpdate(UserAchivement userAchivement) {
        log.info("userAchivements保存:"+userAchivement.toString());
        String userId = userAchivement.getUserId();
        String achivementId = userAchivement.getAchivementId();
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(achivementId)){
            return R.error(1, "参数缺失");
        }
        UserAchivement userAchivement1 = userAchivementDao.selectUserAchivement(userAchivement);
        if(userAchivement1!=null){
            //更新
            userAchivementDao.update(userAchivement);
        }else {
            //新增
            userAchivementDao.save(userAchivement);
        }
        return R.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id获取")
    public UserAchivement get(@PathVariable Long id) {
        return userAchivementDao.getById(id);
    }

    @PutMapping
    @ApiOperation(value = "修改")
    public UserAchivement update(@RequestBody UserAchivement userAchivement) {
        userAchivementDao.update(userAchivement);
        return userAchivement;
    }

    /**
     * 功能描述:查询用户成就列表
     *
     * @param userAchivement
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/7/6 15:55
     */
    @PostMapping("/getUserAchivements")
    @ApiOperation(value = "列表")
    public ResultVO<?> getUserAchivements(UserAchivement userAchivement) {
        log.info("userAchivements查询:"+userAchivement.toString());
        String userId = userAchivement.getUserId();
        String achivementId = userAchivement.getAchivementId();
        if (StringUtils.isBlank(userId) && StringUtils.isBlank(achivementId)) {
            //如果用户数据为空
            return R.success(new ArrayList<UserAchivement>());
        }
        List<UserAchivement> list = userAchivementDao.selectUserAchivements(userAchivement);
        return R.success(list);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public void delete(@PathVariable Long id) {
        userAchivementDao.delete(id);
    }
}