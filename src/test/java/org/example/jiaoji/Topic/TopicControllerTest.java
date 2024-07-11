package org.example.jiaoji.Topic;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

import org.example.jiaoji.controller.TopicController;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TopicControllerTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOps;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    public void testGetTopic_CacheMiss() {
        Integer id = 1;
        int pageIndex = 1;
        int pageSize = 10;
        String type = "hot";
        String key = "id" + id + "pageIndex" + pageIndex + "pageSize" + pageSize + "type" + type;
        PageInfo<Topic> expectedTopic = new PageInfo<>();
        
        when(valueOps.get(key)).thenReturn(null);
        when(topicService.SelectByClassId(id, pageSize, pageIndex, type)).thenReturn(expectedTopic);
        doNothing().when(valueOps).set(eq(key), anyString(), eq(8L));

        ResponseEntity<PageInfo<Topic>> response = topicController.getTopic(id, pageIndex, pageSize, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTopic, response.getBody());
        verify(valueOps, times(1)).set(eq(key), anyString(), eq(8L));
    }

    @Test
    public void testGetTopic_CacheHit() {
        Integer id = 1;
        int pageIndex = 1;
        int pageSize = 10;
        String type = "type";
        String key = "id" + id + "pageIndex" + pageIndex + "pageSize" + pageSize + "type" + type;
        PageInfo<Topic> expectedTopic = new PageInfo<>();
        String json = JSON.toJSONString(expectedTopic);

        when(valueOps.get(key)).thenReturn(json);

        ResponseEntity<PageInfo<Topic>> response = topicController.getTopic(id, pageIndex, pageSize, type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOneTopic_CacheMiss() {
        Integer id = 1;
        String key = "topicId:" + id;
        Topic expectedTopic = new Topic();
        expectedTopic.setId(id);
        expectedTopic.setTitle("Test Topic");

        when(valueOps.get(key)).thenReturn(null);
        when(topicService.SelectById(id)).thenReturn(expectedTopic);
        doNothing().when(valueOps).set(eq(key), anyString(), eq(3600L));

        ResponseEntity<Topic> response = topicController.getOneTopic(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTopic, response.getBody());
        verify(valueOps, times(1)).set(eq(key), anyString(), eq(3600L));
    }

    @Test
    public void testGetOneTopic_CacheHit() {
        Integer id = 1;
        String key = "topicId:" + id;
        Topic expectedTopic = new Topic();
        expectedTopic.setId(id);
        expectedTopic.setTitle("Test Topic");
        String json = JSON.toJSONString(expectedTopic);

        when(valueOps.get(key)).thenReturn(json);

        ResponseEntity<Topic> response = topicController.getOneTopic(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTopic, response.getBody());
    }

    @Test
    public void testInsert() {
        Topic topic = new Topic();
        RetType expectedRetType = new RetType();
        expectedRetType.setOk(true);
        expectedRetType.setMsg("Inserted successfully");

        when(topicService.insertTopic(topic)).thenReturn(expectedRetType);

        RetType actualRetType = topicController.insert(topic);

        assertEquals(expectedRetType, actualRetType);
    }

    @Test
    public void testSearch() {
        String keyword = "test";
        int pageIndex = 1;
        int pageSize = 10;
        PageInfo<Topic> expectedTopic = new PageInfo<>();

        when(topicService.search(keyword, pageSize, pageIndex)).thenReturn(expectedTopic);

        ResponseEntity<PageInfo<Topic>> response = topicController.search(keyword, pageIndex, pageSize);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedTopic, response.getBody());
    }

    @Test
    public void testFollow() {
        int userId = 1;
        int topicId = 1;
        RetType expectedRetType = new RetType();
        expectedRetType.setOk(true);
        expectedRetType.setMsg("Followed successfully");

        when(topicService.setFollow(topicId, userId)).thenReturn(expectedRetType);

        RetType actualRetType = topicController.follow(userId, topicId);

        assertEquals(expectedRetType, actualRetType);
    }

    @Test
    public void testUnfollow() {
        int userId = 1;
        int topicId = 1;
        boolean expectedResult = true;

        when(topicService.findFollow(topicId, userId)).thenReturn(expectedResult);

        boolean actualResult = topicController.unfollow(userId, topicId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testDelete() {
        int id = 1;
        RetType expectedRetType = new RetType();
        expectedRetType.setOk(true);
        expectedRetType.setMsg("Deleted successfully");

        when(topicService.deleteTopic(id)).thenReturn(expectedRetType);

        RetType actualRetType = topicController.delete(id);

        assertEquals(expectedRetType, actualRetType);
        verify(stringRedisTemplate, times(1)).delete("topicId:" + id);
    }

    @Test
    public void testHotTopic_CacheMiss() {
        String key = "hotTopic";
        List<Topic> expectedTopics = List.of(new Topic(), new Topic());

        when(valueOps.get(key)).thenReturn(null);
        when(topicService.hotTopic()).thenReturn(expectedTopics);
        doNothing().when(valueOps).set(eq(key), anyString(), eq(3600L));

        List<Topic> actualTopics = topicController.hotTopic();

        assertEquals(expectedTopics, actualTopics);
        verify(valueOps, times(1)).set(eq(key), anyString(), eq(3600L));
    }

    @Test
    public void testHotTopic_CacheHit() {
        String key = "hotTopic";
        List<Topic> expectedTopics = List.of(new Topic(), new Topic());
        String json = JSON.toJSONString(expectedTopics);

        when(valueOps.get(key)).thenReturn(json);

        List<Topic> actualTopics = topicController.hotTopic();

        assertEquals(expectedTopics, actualTopics);
    }

    @Test
    public void testGetTopic_Exception() {
        Integer id = 1;
        int pageIndex = 1;
        int pageSize = 10;
        String type = "type";
        String key = "id" + id + "pageIndex" + pageIndex + "pageSize" + pageSize + "type" + type;

        when(valueOps.get(key)).thenThrow(new RuntimeException());

        ResponseEntity<PageInfo<Topic>> response = topicController.getTopic(id, pageIndex, pageSize, type);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testGetOneTopic_Exception() {
        Integer id = 1;
        String key = "topicId:" + id;

        when(valueOps.get(key)).thenThrow(new RuntimeException());

        ResponseEntity<Topic> response = topicController.getOneTopic(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testHotTopic_Exception() {
        String key = "hotTopic";

        when(valueOps.get(key)).thenThrow(new RuntimeException());

        List<Topic> actualTopics = topicController.hotTopic();

        assertEquals(null, actualTopics);
    }
}
