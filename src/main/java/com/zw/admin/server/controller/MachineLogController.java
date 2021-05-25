package com.zw.admin.server.controller;

import java.util.List;

import cn.hutool.json.JSONUtil;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
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
import com.zw.admin.server.dao.MachineLogDao;
import com.zw.admin.server.model.MachineLog;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

@RestController
@RequestMapping("/machineLog")
public class MachineLogController {

    @Resource
    private MachineLogDao machineLogDao;

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO<?> save(MachineLog machineLog) {
        String logId = machineLog.getLogId();
        String data = machineLog.getData();
        String machineId = machineLog.getMachineId();
        //非空校验
        if (StringUtils.isBlank(logId) || StringUtils.isBlank(machineId)) {
            return R.error(1, "必填参数为空");
        }
        //json校验
        if (!JSONUtil.isJson(data)) {
            //非json
            return R.error(1, "data格式不正确");
        }
        machineLogDao.save(machineLog);
        return R.success();
    }


}