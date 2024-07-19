package org.example.jiaoji.utils;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.SendResult;



import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;



@Service  
public class KafkaProducerService {  
    @Autowired  
    private KafkaTemplate<String, String> kafkaTemplate;  
    
    public void sendMessage(String topic, Object message) {
        String json=JSON.toJSONString(message);
        kafkaTemplate.send(topic, json);
    }  

    public List<Integer> syncGetObjScore(String topic, Object message) {
        String json=JSON.toJSONString(message);
        //kafka发送同步消息并获取返回值
        try{
            SendResult<String,String> result=kafkaTemplate.send(topic, json).get();
            String res=result.toString();
            //把返回值转换为数组List<Integer>
            List<Integer> scores = JSON.parseObject(res, new TypeReference<List<Integer>>() {});
            return scores;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }  
}
