package com.zw.admin.server.com.zw.admin.server.controller;

import com.zw.admin.server.controller.UserTrainReportController;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserTrainReport;
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
public class UserTrainReportTest {

    @Resource
    UserTrainReportController userTrainReportController;

    @Test
    public void save(){
        //保存报告
        UserTrainReport userTrainReport = new UserTrainReport();
        userTrainReport.setReportId("reportId001");
        userTrainReport.setUserId("userId001");
        userTrainReport.setMachineId("machineId001");
        userTrainReport.setTrainLogId("trainLogId001");
        userTrainReport.setGameId(10000);
        userTrainReport.setCreateTime(new Date());
        ResultVO<?> resultVO = userTrainReportController.save(userTrainReport);
        log.info("msg:"+resultVO.getMsg());
    }

}

