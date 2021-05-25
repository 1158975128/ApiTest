package com.zw.admin.server.com.zw.admin.server.controller;

import com.zw.admin.server.controller.UserArchiveController;
import com.zw.admin.server.model.ResultDTO;
import com.zw.admin.server.model.ResultVO;
import com.zw.admin.server.model.UserArchive;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserArchiveTest {

    @Resource
    UserArchiveController userArchiveController;

    @Test
    public void save(){
        //用户存档
        UserArchive archive = new UserArchive();
        archive.setKey("key002");
        archive.setMachineType("M2");
        archive.setUserId("userId001");
        ResultVO<?> resultVO = userArchiveController.save(archive);
        log.info("msg:"+resultVO.getData());
    }

    @Test
    public void selectUserArchive(){
        //用户存档
        UserArchive archive = new UserArchive();
        archive.setUserId("userId001");
        ResultDTO<?> resultVO = userArchiveController.selectUserArchive(archive);
        log.info("msg:"+resultVO.getMsg());
    }

}
