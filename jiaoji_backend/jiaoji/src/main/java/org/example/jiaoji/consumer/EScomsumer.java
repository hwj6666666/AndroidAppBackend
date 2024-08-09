package org.example.jiaoji.consumer;

import java.io.IOException;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.alibaba.fastjson2.JSON;

public class EScomsumer {
    @Autowired
    private UserMapper usermapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestHighLevelClient client;

    @KafkaListener(topics = "ES_update_user", groupId = "object-updateHotComment-group")  
    public void consumeESUpdateUser(Integer message) { 
        // 解析JSON字符串 
        Integer userId=message;

        // 获取JSON对象中的值
        User user=usermapper.selectByUserId(userId);
        IndexRequest request = new IndexRequest("user").id(userId.toString());

        // 2.准备Json文档
        request.source(JSON.toJSONString(user), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
            System.out.println("Received message: " + message);  
        } 


        @KafkaListener(topics = "ES_store_user", groupId = "object-updateHotComment-group")  
    public void consumeObjHot(Integer message) { 
        // 解析JSON字符串 

        // 获取JSON对象中的值
        Integer userId=message;
        User user=usermapper.selectByUserId(userId);
        IndexRequest request = new IndexRequest("user").id(userId.toString());
        // 2.准备Json文档
        request.source(JSON.toJSONString(user), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "ES_store_topic", groupId = "object-updateHotComment-group")  
    public void consumeESStoreTopic(String title) { 
        // 解析JSON字符串 
        Topic topic=topicMapper.selectByTitle(title);
        // 获取JSON对象中的值
        IndexRequest request = new IndexRequest("topic").id(topic.getId().toString());
        // 2.准备Json文档
        request.source(JSON.toJSONString(topic), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "ES_store_object", groupId = "object-updateHotComment-group")  
    public void consumeESStoreObject(String title) { 
        // 解析JSON字符串 
        Objects object=objectMapper.selectByTitle(title);
        // 获取JSON对象中的值
        IndexRequest request = new IndexRequest("object").id(object.getId().toString());

        // 2.准备Json文档
        request.source(JSON.toJSONString(object), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    
}
