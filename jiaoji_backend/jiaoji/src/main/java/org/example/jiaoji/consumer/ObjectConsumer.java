package org.example.jiaoji.consumer;



import org.example.jiaoji.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;


@Service  
public class ObjectConsumer {  
    @Autowired
    private ObjectService objectService;
    

    @KafkaListener(topics = "object_updateAveScore", groupId = "object-updateAveScore-group")  
    public void consumeObjDesScore(String message) { 
        // 解析JSON字符串 
        JSONObject jsonObject = JSONObject.parseObject(message);

        // 获取JSON对象中的值
        int topicId = jsonObject.getInteger("objectId");
        int score = jsonObject.getInteger("score");
        objectService.updateAveScore(topicId, score);
        System.out.println("Received message: " + message);  

    } 


    @KafkaListener(topics = "object_decAveScore", groupId = "object-decAveScore-group")  
    public void consumeObjupdateScore(String message) { 
        // 解析JSON字符串 
        JSONObject jsonObject = JSONObject.parseObject(message);

        // 获取JSON对象中的值
        int topicId = jsonObject.getInteger("objectId");
        int score = jsonObject.getInteger("score");
        objectService.decAveScore(topicId, score);
        System.out.println("Received message: " + message);  

    } 

    @KafkaListener(topics = "object_updateHotComment", groupId = "object-updateHotComment-group")  
    public void consumeObjHot(String message) { 
        // 解析JSON字符串 
        JSONObject jsonObject = JSONObject.parseObject(message);

        // 获取JSON对象中的值
        int topicId = jsonObject.getInteger("objectId");
        int id = jsonObject.getInteger("id");
        int change = jsonObject.getInteger("change");
        objectService.updateHotComment(topicId, id, change);
        System.out.println("Received message: " + message);  
    } 
}
