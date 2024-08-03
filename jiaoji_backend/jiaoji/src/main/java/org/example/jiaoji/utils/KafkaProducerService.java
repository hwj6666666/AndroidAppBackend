package org.example.jiaoji.utils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;



import com.alibaba.fastjson2.JSON;



@Service  
public class KafkaProducerService {  
    @Autowired  
    private KafkaTemplate<String, String> kafkaTemplate;  
    
    public void sendMessage(String topic, Object message) {
        String json=JSON.toJSONString(message);
        kafkaTemplate.send(topic, json);
    }   
}
