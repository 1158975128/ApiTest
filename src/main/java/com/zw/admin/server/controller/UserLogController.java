package com.zw.admin.server.controller;

import java.util.Date;
import java.util.List;

import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.MachineInfoDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.UserLogDao;
import com.zw.admin.server.model.UserLog;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户登录机器日志
 * @author larry
 * @Date 2020/10/29 14:02
 */
@Slf4j
@RestController
@RequestMapping("/userLogs")
public class UserLogController {

    @Resource
    private UserLogDao userLogDao;
    @Resource
    private MachineInfoDao machineInfoDao;

    /**
     * 功能描述:用户登录机器日志添加
     *
     * @param userLog
     * @return com.zw.admin.server.model.UserLog
     * @author larry
     * @Date 2020/10/29 14:05
     */
    @LogAnnotation(module = "用户登录机器日志添加")
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO<?> save(UserLog userLog) {
        log.info("用户登录机器日志添加:" + userLog.toString());
        String userId = userLog.getUserId();
        String machineId = userLog.getMachineId();
        Date updateTime = userLog.getUpdateTime();
        String logId = userLog.getLogId();
        //非空校验
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(machineId) || updateTime == null || StringUtils.isBlank(logId)) {
            return R.error(1, "必填参数为空");
        }
        //machineId是否合理校验
        String machineType = machineInfoDao.getMachineTypeBySn(machineId);
        if (StringUtils.isBlank(machineType)) {
            return R.error(2, "机器编号不合理");
        }
        //重复性校验
        UserLog log = userLogDao.checkExists(userLog);
        if (log != null) {
            //return R.error(3,"数据重复")
            //直接返回，不用存储
            return R.success();
        }
        //根据logId判断是否重复，如有重复更新
        userLog.setMachineType(machineType);
        if(userLogDao.countUserLogByLogId(logId)>0){
            userLogDao.update(userLog);
        }else {
            userLogDao.save(userLog);
        }
        return R.success();
    }


}