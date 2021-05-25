package com.zw.admin.server.com.zw.admin.server.controller;

import com.zw.admin.server.controller.UserTrainLogController;
import com.zw.admin.server.dto.PageRequest;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserTrainEvaluation;
import com.zw.admin.server.model.UserTrainLog;
import com.zw.admin.server.model.UserTrainLogBo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTrainLogTest {

    @Resource
    UserTrainLogController userTrainLogController;

    @Test
    public void test1(){
        log.info("test1");
    }

    @Test
    public void save(){
        //训练数据添加
        UserTrainLog userTrainLog = new UserTrainLog();
        userTrainLog.setMachineId("111111");
        userTrainLog.setUserName("test1");
        userTrainLog.setTrainLogId("log111");
        userTrainLog.setGameId(10091);
        userTrainLogController.save(userTrainLog);
    }

    @Test
    public void getById(){
        //根据id查询
        Long id = 5801l;
        Integer limit = 2;
        ResultVO<?> byId = userTrainLogController.getById(id, limit);
        log.info("msg:"+byId.getMsg());
    }

    @Test
    public void selectTrainsLogPage(){
        //分页查询
        PageRequest<UserTrainLogBo> request = new PageRequest<>();
        request.setNum(10);
        request.setPage(1);
        Map<String, Object> params = new HashMap<>();
        request.setParams(new UserTrainLogBo());
        String token = "";
        ResultVO<?> resultVO = userTrainLogController.selectTrainsLogPage(request,token,"fris");
        log.info("msg:"+resultVO.getMsg());
    }



    @Test
    public void selectTrainEvaluationPage(){
        //分页查询评估报告分页
        PageRequest<UserTrainEvaluation> request = new PageRequest<>();
        request.setNum(10);
        request.setPage(1);
    }

    @Test
    public void selectTrainEvaluationDetail(){
        //查询评估报告
        UserTrainEvaluation evaluation = new UserTrainEvaluation();
        evaluation.setId(824l);
        ResultVO<?> resultVO = userTrainLogController.selectTrainEvaluation(evaluation);
        log.info("msg:"+resultVO.getMsg());
    }

    @Test
    public void selectGameTrainData(){
        //查询统计信息
        UserTrainLogBo userTrainLogBo = new UserTrainLogBo();
        userTrainLogBo.setDateType("day");
        userTrainLogBo.setCountType("time");
        userTrainLogBo.setStartTime("2020-10-26 00:00:00");
        userTrainLogBo.setEndTime("2020-11-24 13:44:26");
        ResultVO<?> resultVO = userTrainLogController.selectGameTrainData(userTrainLogBo);
        log.info("msg:"+resultVO.getMsg());
    }
}
