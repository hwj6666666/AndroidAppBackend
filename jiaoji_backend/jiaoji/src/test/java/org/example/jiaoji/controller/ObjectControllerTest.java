package org.example.jiaoji.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ObjectControllerTest {

    @InjectMocks
    private ObjectController objectController;

    @Mock
    private ObjectService objectService;

    @Mock
    private TopicService topicService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetObject() {
        Integer topicId = 1;
        Integer pageIndex = 1;
        Integer pageSize = 10;

        PageInfo<Objects> expectedObjectsPage = new PageInfo<>(new ArrayList<>());
        String jsonObject = JSON.toJSONString(expectedObjectsPage);

        doNothing().when(topicService).addViews(topicId);
        when(valueOperations.get("getObjectbyTopicId:" + topicId)).thenReturn(null);
        when(objectService.SelectAllInTopic(eq(topicId), eq(pageSize), eq(pageIndex))).thenReturn(expectedObjectsPage);

        ResponseEntity<PageInfo<Objects>> responseEntity = objectController.getObject(topicId, pageIndex, pageSize);

        assertEquals(expectedObjectsPage, responseEntity.getBody());

        when(valueOperations.get("getObjectbyTopicId:" + topicId)).thenReturn(jsonObject);
        ResponseEntity<PageInfo<Objects>> responseEntity1 = objectController.getObject(topicId, pageIndex, pageSize);

        assertEquals(expectedObjectsPage, responseEntity.getBody());

        verify(topicService, times(2)).addViews(topicId);
        verify(objectService, times(1)).SelectAllInTopic(topicId, pageSize, pageIndex);
        verify(valueOperations, times(3)).get("getObjectbyTopicId:" + topicId);
        verify(valueOperations, times(1)).set("getObjectbyTopicId:" + topicId, jsonObject, 10, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Test
    void testInsert() {
        Objects object = new Objects();
        when(objectService.InsertObject(any(Objects.class))).thenReturn(1);

        Integer response = objectController.insert(object);

        assertEquals(1, response);
        verify(objectService, times(1)).InsertObject(object);
    }

    @Test
    void testGetObjectById() {
        Integer id = 1;
        List<Objects> expectedObjectsList = new ArrayList<>();
        String jsonObject = JSON.toJSONString(expectedObjectsList);

        when(valueOperations.get("object:" + id)).thenReturn(null);
        when(objectService.SelectById(eq(id))).thenReturn(expectedObjectsList);

        ResponseEntity<List<Objects>> responseEntity = objectController.getObjectById(id);

        assertEquals(expectedObjectsList, responseEntity.getBody());

        when(valueOperations.get("object:" + id)).thenReturn(jsonObject);
        ResponseEntity<List<Objects>> responseEntity1 = objectController.getObjectById(id);

        assertEquals(expectedObjectsList, responseEntity1.getBody());

        verify(objectService, times(1)).SelectById(id);
        verify(valueOperations, times(3)).get("object:" + id);
        verify(valueOperations, times(1)).set("object:" + id, jsonObject, 3600, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Test
    void testGetObjectsByTopicId() {
        String keyword = "top3Object:";
        List<Objects> expectedObjectsList = new ArrayList<>();
        when(objectService.search(eq(keyword))).thenReturn(expectedObjectsList);

        List<Objects> response = objectController.getObjectsByTopicId(keyword);

        assertEquals(expectedObjectsList, response);
        verify(objectService, times(1)).search(keyword);
    }

    @Test
    void testGetTop3ObjectsByTopicId() {
        Integer topicId = 1;
        List<top3Object> expectedTop3ObjectsList = new ArrayList<>();
        String jsonObject = JSON.toJSONString(expectedTop3ObjectsList);

        when(valueOperations.get("top3Object:" + topicId)).thenReturn(null);
        when(objectService.SelectTop3(eq(topicId))).thenReturn(expectedTop3ObjectsList);

        List<top3Object> response = objectController.getObjectsByTopicId(topicId);

        assertEquals(expectedTop3ObjectsList, response);

        when(valueOperations.get("top3Object:" + topicId)).thenReturn(jsonObject);
        List<top3Object> responseEntity1 = objectController.getObjectsByTopicId(topicId);

        assertEquals(expectedTop3ObjectsList, responseEntity1);

        verify(objectService, times(1)).SelectTop3(topicId);
        verify(valueOperations, times(3)).get("top3Object:" + topicId);
        verify(valueOperations, times(1)).set("top3Object:" + topicId, jsonObject, 3600, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Test
    void testDelete() {
        Integer id = 1;
        RetType expectedRetType = new RetType();
        expectedRetType.setOk(true);
        expectedRetType.setMsg("Deleted successfully");
        when(objectService.deleteObject(eq(id))).thenReturn(expectedRetType);

        RetType response = objectController.delete(id);

        assertEquals(expectedRetType, response);
        verify(objectService, times(1)).deleteObject(id);
        verify(stringRedisTemplate, times(1)).delete("object:" + id);
    }
}
