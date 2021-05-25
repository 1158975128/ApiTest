package com.zw.admin.server.controller;

import java.util.Date;
import java.util.List;

import cn.hutool.json.JSONUtil;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.model.ResultDTO;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.UserArchiveDao;
import com.zw.admin.server.model.UserArchive;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 * 功能描述:用户存档控制器
 *
 * @author larry
 * @Date 2020/10/22 19:02
 */
@RestController
@Api(tags = "用户存档")
@RequestMapping("/userArchives")
public class UserArchiveController {

    @Resource
    private UserArchiveDao userArchiveDao;

    /**
     * 功能描述:用户存档
     *
     * @param userArchive
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/22 20:20
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    @LogAnnotation(module = "用户存档保存")
    public ResultVO<?> save(UserArchive userArchive) {
        String userId = userArchive.getUserId();
        String machineType = userArchive.getMachineType();
        String key = userArchive.getKey();
        Date time = userArchive.getUpdateTime();
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(machineType) || StringUtils.isBlank(key) || time == null) {
            return R.error(1, "必填字段为空");
        }
        //判断data是否为json
        if(!JSONUtil.isJson(userArchive.getArchive())){
            userArchive.setArchive(null);
        }
        //机器时间
        Long unityTimeStamp = time.getTime();
        List<UserArchive> userArchives = userArchiveDao.selectUserArchive(userArchive);
        //数据库没有则新增
        if (CollectionUtils.isEmpty(userArchives)) {
            //初始化创建时间
            userArchive.setCreateTime(new Date());
            userArchiveDao.save(userArchive);
        } else {
            //数据库有
            UserArchive archive = userArchives.get(0);
            //查询数据库最后更新时间
            Date updateTime = archive.getUpdateTime();
            //数据库时间戳
            long timeStamp = updateTime.getTime();
            //如果数据库时间大于unity时间，不用处理
            if (timeStamp >= unityTimeStamp) {
                //如果数据库时间大于unity时间，不用处理
                return R.ok();
            } else {
                //数据库时间小于Unity提交时间
                if (userArchives.size() > 1) {
                    //先删除再添加
                    userArchiveDao.deleteUserArchive(userArchive);
                    userArchiveDao.save(userArchive);
                    return R.ok();
                } else {
                    //直接更新
                    userArchive.setId(archive.getId());
                    userArchive.setCreateTime(null);
                    userArchiveDao.update(userArchive);
                    return R.ok();
                }
            }
        }
        return R.success();
    }


    /**
     * 功能描述:查询用户存档
     *
     * @param userArchive
     * @return com.zw.admin.server.model.ResultDTO<?>
     * @author larry
     * @Date 2020/10/23 17:49
     */
    @PostMapping("/selectUserArchive")
    @ApiOperation(value = "查询用户存档")
    public ResultDTO<?> selectUserArchive(UserArchive userArchive) {
        String userId = userArchive.getUserId();
        if (StringUtils.isBlank(userId)) {
            return R.fail(1, "参数为空");
        }
        List<UserArchive> userArchives = userArchiveDao.selectUserArchive(userArchive);
        return R.ok(userArchives);
    }
}