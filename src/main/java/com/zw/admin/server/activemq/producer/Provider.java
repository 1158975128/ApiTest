package com.zw.admin.server.activemq.producer;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Topic;
import java.util.HashMap;
import java.util.Map;

/**
 * @author larry
 * @Description:消息发送
 * @date 2020/6/16 15:42
 */
@Slf4j
@Component
public class Provider {

    @Resource
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Resource
    private ActiveMQQueue queue;
    @Resource
    private Topic topic;

    /**
     * 功能描述:发送点对点模式
     *
     * @param msg
     * @return void
     * @author larry
     * @Date 2020/6/15 15:34
     */
    public void sendMessage(String msg) {
        //创建队列
        jmsMessagingTemplate.convertAndSend(queue, msg);
        log.info("发送点对点消息:"+msg);
    }

    /**
     * 功能描述:发送pub,sub模型
     *
     * @param msg
     * @return void
     * @author larry
     * @Date 2020/6/15 15:35
     */
    public void sendTopic(String msg,String operate) {
        Map<String,String> mqMap = new HashMap<String,String>();
        //发送消息的系统
        mqMap.put("id","v4");
        //据悉消息内容
        mqMap.put("msg",msg);
        mqMap.put("operate",operate);
        String mqMsg = JSONObject.toJSONString(mqMap);
        jmsMessagingTemplate.convertAndSend(topic, mqMsg);
        log.info("发送主题消息:"+msg);
    }

}

