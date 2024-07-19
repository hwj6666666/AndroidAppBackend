package org.example.jiaoji.consumer;


import org.example.jiaoji.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;


@Service  
public class TopicConsumer {  
    @Autowired
    private TopicService topicService;

    @KafkaListener(topics = "topic_follow", groupId = "topic-follow-group")  
    public void consumeFollow(String message) { 
        // 解析JSON字符串
        JSONObject jsonObject = JSONObject.parseObject(message);

        // 获取JSON对象中的值
        int topicId = jsonObject.getInteger("topicId");
        int userId = jsonObject.getInteger("userId");
        topicService.setFollow(topicId, userId);
        System.out.println("Received message: " + message);  

    }  


    @KafkaListener(topics = "topic_views", groupId = "topic_views-group")  
    public void consumeView(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        topicService.addViews(id);
        System.out.println("Received message: " + message);
    } 
}
