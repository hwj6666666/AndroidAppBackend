package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopicControllerTest {

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getTopic() {
        PageInfo<Topic> mockPageInfo = new PageInfo<>(new ArrayList<>());
        when(topicService.SelectAll(anyInt(), anyInt(), anyString())).thenReturn(mockPageInfo);
        when(topicService.SelectByClassId(anyInt(), anyInt(), anyInt(), anyString())).thenReturn(mockPageInfo);

        ResponseEntity<PageInfo<Topic>> response = topicController.getTopic(0, 1, 10, "type");
        assertEquals(mockPageInfo, response.getBody());

        response = topicController.getTopic(1, 1, 10, "type");
        assertEquals(mockPageInfo, response.getBody());

        verify(topicService, times(1)).SelectAll(anyInt(), anyInt(), anyString());
        verify(topicService, times(1)).SelectByClassId(anyInt(), anyInt(), anyInt(), anyString());
    }

    @Test
    void getOneTopic() {
        Topic mockTopic = new Topic();
        when(topicService.SelectById(anyInt())).thenReturn(mockTopic);

        ResponseEntity<Topic> response = topicController.getOneTopic(1);
        assertEquals(mockTopic, response.getBody());

        verify(topicService, times(1)).SelectById(anyInt());
    }

    @Test
    void insert() {
        RetType mockRetType = new RetType();
        when(topicService.insertTopic(any(Topic.class))).thenReturn(mockRetType);

        Topic testTopic = new Topic();
        RetType response = topicController.insert(testTopic);
        assertEquals(mockRetType, response);

        verify(topicService, times(1)).insertTopic(any(Topic.class));
    }

    @Test
    void search() {
        PageInfo<Topic> mockPageInfo = new PageInfo<>(new ArrayList<>());
        when(topicService.search(anyString(), anyInt(), anyInt())).thenReturn(mockPageInfo);

        ResponseEntity<PageInfo<Topic>> response = topicController.search("keyword", 1, 10);
        assertEquals(mockPageInfo, response.getBody());

        verify(topicService, times(1)).search(anyString(), anyInt(), anyInt());
    }

    @Test
    void follow() {
        RetType mockRetType = new RetType();
        when(topicService.setFollow(anyInt(), anyInt())).thenReturn(mockRetType);

        RetType response = topicController.follow(1, 1);
        assertEquals(mockRetType, response);

        verify(topicService, times(1)).setFollow(anyInt(), anyInt());
    }

    @Test
    void unfollow() {
        when(topicService.findFollow(anyInt(), anyInt())).thenReturn(true);

        boolean response = topicController.unfollow(1, 1);
        assertTrue(response);

        verify(topicService, times(1)).findFollow(anyInt(), anyInt());
    }

    @Test
    void delete() {
        RetType mockRetType = new RetType();
        when(topicService.deleteTopic(anyInt())).thenReturn(mockRetType);

        RetType response = topicController.delete(1);
        assertEquals(mockRetType, response);

        verify(topicService, times(1)).deleteTopic(anyInt());
    }

    @Test
    void hotTopic() {
        List<Topic> mockTopicList = new ArrayList<>();
        when(topicService.hotTopic()).thenReturn(mockTopicList);

        List<Topic> response = topicController.hotTopic();
        assertEquals(mockTopicList, response);

        verify(topicService, times(1)).hotTopic();
    }
}
