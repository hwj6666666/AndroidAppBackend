package org.example.jiaoji.service.serverimpl;

import java.util.List;

import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.CommentService;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.RemarkService;
import org.example.jiaoji.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;


@Service  
public class KafkaConsumerService {  
    @Autowired
    private TopicService topicService;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private RemarkService remarkService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RemarkMapper remarkMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TopicMapper topicMapper;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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

    @KafkaListener(topics = "object_decAveScore", groupId = "object-decAveScore-group")  
    public void consumeView(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        topicService.addViews(id);
        System.out.println("Received message: " + message);
    } 

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

    @KafkaListener(topics = "object_getScore", groupId = "object-updateHotComment-group")  
    public List<Integer> consumeObjScore(String message) { 
        // 解析JSON字符串 
        Integer objectId=Integer.parseInt(message);



        String key="ObjScore:"+objectId;
        if (stringRedisTemplate.opsForValue().get(key) == null) {
            List<Integer> scores = remarkService.getScore(objectId);
            String json = JSON.toJSONString(scores);
            stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
            return scores;
        } else {
            String json = stringRedisTemplate.opsForValue().get(key);
            List<Integer> scores = JSON.parseObject(json, new TypeReference<List<Integer>>() {});
            return scores;
        }
    } 

    @KafkaListener(topics = "object_updateHotComment", groupId = "object-updateHotComment-group")  
    public void deleteComment(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        commentService.deleteById(id);
    } 



    @KafkaListener(topics = "addComment", groupId = "object-updateHotComment-group")  
    public void addComment(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        Remark remark = remarkMapper.selectById(id).get(0);
        Objects object = objectMapper.selectById(remark.getObjectId()).get(0);
        Topic topic = topicMapper.selectById(object.getTopicId());
        topicMapper.updateRemarkNum(topic.getRemarkNum() + 1, topic.getId());
    }
}





