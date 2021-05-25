package com.zw.admin.server.com.zw.admin.server.controller;

import com.zw.admin.server.controller.TrainStatsController;
import com.zw.admin.server.model.ResultDTO;
import com.zw.admin.server.model.TrainStats;
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
public class TrainStatsTest {

    @Resource
    TrainStatsController trainStatsController;

    @Test
    public void save(){
        //用户统计保存，缺少必填字段
        TrainStats trainStats =  new TrainStats();
        trainStats.setUserId("userId001");
        trainStats.setMachineType("M2");
        trainStats.setTrainTime(20);
        trainStats.setTrainDays(10);
        trainStats.setForceTimes(333);
        trainStats.setTotalScore(1000);
        //trainStats.set
        ResultDTO<?> resultVO = trainStatsController.save(trainStats);
        log.info("msg:"+resultVO.getMsg());
        //补充必填字段
        trainStats.setUpdateTime(new Date());
        //trainStats.set
        ResultDTO<?> resultVO1 = trainStatsController.save(trainStats);
        log.info("msg:"+resultVO1.getMsg());
    }

    @Test
    public void selectTrainStats(){
        //用户统计查询
        TrainStats trainStats =  new TrainStats();
        trainStats.setUserId("609f30dc914b4afb9591a4a4313f5b30");
        trainStats.setMachineType("M2");
        ResultDTO<?> resultDTO = trainStatsController.selectTrainStats(trainStats);
        log.info("msg:"+resultDTO.getData());
    }

}
