package com.zw.admin.server.com.zw.admin.server.controller;

import com.zw.admin.server.controller.UserTrainEvaluationController;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserTrainEvaluation;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTrainEvaluationTest {

    @Resource
    UserTrainEvaluationController userTrainEvaluationController;

    @Test
    public void save(){
        //用户评估报告上传
        UserTrainEvaluation evaluation = new UserTrainEvaluation();
        evaluation.setEvaluationId("evaluationId002");
        evaluation.setUserId("userId002");
        evaluation.setMachineId("machineId");
        evaluation.setType(1);
        evaluation.setData("{'date':'111'}");
        evaluation.setUpdateTime(new Date());
        ResultVO<?> resultVO = userTrainEvaluationController.save(evaluation);
        log.info("msg:"+resultVO.getMsg());
    }
}
