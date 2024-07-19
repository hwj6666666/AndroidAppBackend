package org.example.jiaoji.consumer;

import org.example.jiaoji.service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSONObject;

@Service  
public class RemarkConsumer {  
    @Autowired
    private RemarkService remarkService;

 @KafkaListener(topics = "RemarkchangeLike", groupId = "RemarkchangeLike-group")  
    public void consumeObjupdateScore(String message) { 
        // 解析JSON字符串 
        JSONObject jsonObject = JSONObject.parseObject(message);

        // 获取JSON对象中的值
        int id = jsonObject.getInteger("id");
        int uid = jsonObject.getInteger("uid");
        int change = jsonObject.getInteger("change");
        remarkService.changeLike(id, uid, change);
        System.out.println("Received message: " + message);  
    } 
}
