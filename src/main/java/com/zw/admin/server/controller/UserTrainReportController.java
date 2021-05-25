package com.zw.admin.server.controller;

import java.util.List;

import cn.hutool.json.JSONUtil;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.dao.MachineInfoDao;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.UserTrainReportDao;
import com.zw.admin.server.model.UserTrainReport;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户训练报表同步
 * @author larry
 * @Date 2020/10/22 19:13
 */
@Slf4j
@RestController
@Api(tags = "用户训练报表同步")
@RequestMapping("/userTrainReports")
public class UserTrainReportController {

    @Resource
    private UserTrainReportDao userTrainReportDao;
    @Resource
    private MachineInfoDao machineInfoDao;

    /**
     * 功能描述:保存用户训练数据
     *
     * @param userTrainReport
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/22 20:09
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO<?> save(UserTrainReport userTrainReport) {
        String reportId = userTrainReport.getReportId();
        String userId = userTrainReport.getUserId();
        String machineId = userTrainReport.getMachineId();
        if(StringUtils.isBlank(reportId)||StringUtils.isBlank(userId)||StringUtils.isBlank(machineId)){
            return R.error(1,"必填字段为空");
        }
        String machineType = machineInfoDao.getMachineTypeBySn(machineId);
        userTrainReport.setMachineType(machineType);
        //判断data是否为json
        if(!JSONUtil.isJson(userTrainReport.getData())){
            userTrainReport.setData(null);
        }
        userTrainReportDao.save(userTrainReport);
        return R.success();
    }

}