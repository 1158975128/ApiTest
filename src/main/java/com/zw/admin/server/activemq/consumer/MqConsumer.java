package com.zw.admin.server.activemq.consumer;

import com.zw.admin.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author larry
 * @Description:
 * @date 2020/6/15 17:00
 */
@Slf4j
@Component
public class MqConsumer {

    @Resource
    private UserService userService;

    @JmsListener(destination = "ActiveMQQueue")
    public void readMq(String msg){
        log.info("接收到消息队列:"+msg);
    }

}