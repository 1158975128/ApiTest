package com.zw.admin.server.controller;

import java.util.List;

import cn.hutool.json.JSONUtil;
import com.zw.admin.server.annotation.LogAnnotation;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.utils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zw.admin.server.dao.UserTrainEvaluationDao;
import com.zw.admin.server.model.UserTrainEvaluation;
import io.swagger.annotations.ApiOperation;

import javax.annotation.Resource;

/**
 *功能描述:用户评估数据同步控制器
 * @author larry
 * @Date 2020/10/22 19:02
 */
@Slf4j
@RestController
@Api(tags = "用户评估数据同步")
@RequestMapping("/userTrainEvaluations")
public class UserTrainEvaluationController {

    @Resource
    private UserTrainEvaluationDao userTrainEvaluationDao;

    /**
     * 功能描述:用户评估数据
     *
     * @param userTrainEvaluation
     * @return com.zw.admin.server.model.ResultVO<?>
     * @author larry
     * @Date 2020/10/22 20:18
     */
    @LogAnnotation(module = "用户评估数据添加")
    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResultVO<?> save(UserTrainEvaluation userTrainEvaluation) {
        //log.info(userTrainEvaluation.toString());
        //非空校验
        String evaluationId = userTrainEvaluation.getEvaluationId();
        String userId = userTrainEvaluation.getUserId();
        String machineId = userTrainEvaluation.getMachineId();
        if (StringUtils.isBlank(evaluationId) || StringUtils.isBlank(userId) || StringUtils.isBlank(machineId)) {
            return R.error(1, "必填字段为空");
        }
        //根据machineId查询
        if(!JSONUtil.isJson(userTrainEvaluation.getData())){
            userTrainEvaluation.setData(null);
        }else {
            log.info("userTrainEvaluations:"+userTrainEvaluation.toString());
        }
        userTrainEvaluationDao.save(userTrainEvaluation);

        return R.success();
    }

}