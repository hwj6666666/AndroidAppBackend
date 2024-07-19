package org.example.jiaoji.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicControllerTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;
    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    private Topic topic;
    private PageInfo<Topic> pageInfo;
    String jsonTopic, jsonOneTopic;

    @BeforeEach
    void setUp() {
        topic = new Topic();  // 初始化 Topic 对象
        pageInfo = new PageInfo<>(Collections.singletonList(topic));
        jsonTopic = JSON.toJSONString(pageInfo);
        jsonOneTopic = JSON.toJSONString(topic);
        MockitoAnnotations.openMocks(this);
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetTopic() {
        when(valueOperations.get("id0pageIndex1pageSize10typetype")).thenReturn(null);
        when(topicService.SelectAll(anyInt(), anyInt(), anyString())).thenReturn(pageInfo);
        when(topicService.SelectByClassId(anyInt(),anyInt(),anyInt(),anyString())).thenReturn(pageInfo);

        ResponseEntity<PageInfo<Topic>> response = topicController.getTopic(0, 1, 10, "type");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageInfo, response.getBody());

        ResponseEntity<PageInfo<Topic>> response2 = topicController.getTopic(1, 1, 10, "type");

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(pageInfo, response2.getBody());

        when(valueOperations.get("id0pageIndex1pageSize10typetype")).thenReturn(jsonTopic);

        ResponseEntity<PageInfo<Topic>> response1 = topicController.getTopic(0, 1, 10, "type");

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(pageInfo, response.getBody());

        verify(valueOperations, times(3)).get("id0pageIndex1pageSize10typetype");
        verify(valueOperations, times(1)).set("id0pageIndex1pageSize10typetype", jsonTopic, 8, java.util.concurrent.TimeUnit.SECONDS);
        verify(topicService, times(1)).SelectAll(anyInt(), anyInt(), anyString());
    }

    @Test
    void testGetOneTopic() {
        when(valueOperations.get(anyString())).thenReturn(null);
        when(topicService.SelectById(anyInt())).thenReturn(topic);

        ResponseEntity<Topic> response = topicController.getOneTopic(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(topic, response.getBody());

        when(valueOperations.get("topicId:1")).thenReturn(jsonOneTopic);

        ResponseEntity<Topic> response1 = topicController.getOneTopic(1);

        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(topic, response1.getBody());

        verify(valueOperations, times(3)).get("topicId:1");
        verify(valueOperations, times(1)).set("topicId:1", jsonOneTopic, 3600, java.util.concurrent.TimeUnit.SECONDS);
        verify(topicService, times(1)).SelectById(anyInt());
    }

    @Test
    void testInsert() {
        RetType retType = new RetType();
        when(topicService.insertTopic(any(Topic.class))).thenReturn(retType);

        RetType response = topicController.insert(topic);

        assertEquals(retType, response);
        verify(topicService, times(1)).insertTopic(any(Topic.class));
    }

    @Test
    void testSearch() {
        when(topicService.search(anyString(), anyInt(), anyInt())).thenReturn(pageInfo);

        ResponseEntity<PageInfo<Topic>> response = topicController.search("keyword", 1, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pageInfo, response.getBody());

        verify(topicService, times(1)).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void testFollow() {
        RetType retType = new RetType();
        when(topicService.setFollow(anyInt(), anyInt())).thenReturn(retType);

        RetType response = topicController.follow(1, 1);

        assertEquals(retType, response);
        verify(topicService, times(1)).setFollow(anyInt(), anyInt());
    }

    @Test
    void testUnfollow() {
        when(topicService.findFollow(anyInt(), anyInt())).thenReturn(true);

        boolean response = topicController.unfollow(1, 1);

        assertEquals(true, response);
        verify(topicService, times(1)).findFollow(anyInt(), anyInt());
    }

    @Test
    void testDelete() {
        RetType retType = new RetType();
        when(topicService.deleteTopic(anyInt())).thenReturn(retType);

        RetType response = topicController.delete(1);

        assertEquals(retType, response);
        verify(topicService, times(1)).deleteTopic(anyInt());
        verify(stringRedisTemplate, times(1)).delete(anyString());
    }

    @Test
    void testHotTopic() {
        List<Topic> topics = Collections.singletonList(topic);
        String json = JSON.toJSONString(topics);
        when(valueOperations.get("hotTopic")).thenReturn(null);
        when(topicService.hotTopic()).thenReturn(topics);

        List<Topic> response = topicController.hotTopic();

        assertEquals(topics, response);

        when(valueOperations.get("hotTopic")).thenReturn(json);
        List<Topic> response1 = topicController.hotTopic();

        assertEquals(topics, response1);
        verify(topicService, times(1)).hotTopic();
        verify(valueOperations, times(3)).get("hotTopic");
        verify(valueOperations, times(1)).set("hotTopic", json, 3600, java.util.concurrent.TimeUnit.SECONDS);
    }
}