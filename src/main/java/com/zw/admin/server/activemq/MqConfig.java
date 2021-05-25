package com.zw.admin.server.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.Topic;

/**
 * @author larry
 * @Description:队列配置类
 * @date 2020/6/15 14:07
 */
@Configuration
@EnableJms
public class MqConfig {

    /**
     * 功能描述:点对点
     *
     * @param
     * @return org.apache.activemq.command.ActiveMQQueue
     * @author larry
     * @Date 2020/6/15 15:31
     */
    @Bean
    public ActiveMQQueue Queue() {
        return new ActiveMQQueue("ActiveMQQueue");
    }

    /**
     * 功能描述:pub,sub模型
     *
     * @param
     * @return javax.jms.Topic
     * @author larry
     * @Date 2020/6/15 15:31
     */
    @Bean
    public Topic topic() {
        return new ActiveMQTopic("ActiveMQTopic");
    }
}

