package org.example.jiaoji.mapper;

import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    private Objects testObject;
    private Topic testTopic;
    private Remark testRemark;
    private List<Objects> objectsList;
    private List<Remark> remarksList;
    private List<top3Object> top3ObjectsList;

    @BeforeEach
    void setUp() {
        testObject = new Objects();
        testObject.setId(1);
        testObject.setTitle("Test Object");
        testObject.setDescription("This is a test object.");
        testObject.setPicture("picture.jpg");
        testObject.setTopicId(1);
        testObject.setUserId(1);
        testTopic = new Topic();
        testTopic.setId(1);
        testTopic.setTitle("Test Topic");
        testRemark = new Remark();
        testRemark.setId(1);
        testRemark.setObjectId(1);
        testRemark.setContent("This is a test remark.");
        testRemark.setScore(8);
        objectsList = new ArrayList<>();
        objectsList.add(testObject);
        remarksList = new ArrayList<>();
        remarksList.add(testRemark);
        top3ObjectsList = new ArrayList<>();
        top3Object o1 = new top3Object();
        o1.setId(1);
        o1.setTitle("Test Object 1");
        top3Object o2 = new top3Object();
        o2.setId(2);
        o2.setTitle("Test Object 2");
        top3Object o3 = new top3Object();
        o3.setId(3);
        o3.setTitle("Test Object 3");
        top3ObjectsList.add(o1);
        top3ObjectsList.add(o2);
        top3ObjectsList.add(o3);
    }

    @Test
    void selectAllInTopic() {
        when(objectMapper.selectAllInTopic(1)).thenReturn(objectsList);
        List<Objects> result = objectMapper.selectAllInTopic(1);
        assertEquals(objectsList, result);
        verify(objectMapper, times(1)).selectAllInTopic(1);
    }

    @Test
    void selectById() {
        when(objectMapper.selectById(1)).thenReturn(objectsList);
        List<Objects> result = objectMapper.selectById(1);
        assertEquals(objectsList, result);
        verify(objectMapper, times(1)).selectById(1);
    }

    @Test
    void selectOneById() {
        when(objectMapper.selectOneById(1)).thenReturn(testObject);
        Objects result = objectMapper.selectOneById(1);
        assertEquals(testObject, result);
        verify(objectMapper, times(1)).selectOneById(1);
    }

    @Test
    void selectIdByTitle() {
        when(objectMapper.selectIdByTitle("Test Object", 1)).thenReturn(1);
        Integer result = objectMapper.selectIdByTitle("Test Object", 1);
        assertEquals(1, result);
        verify(objectMapper, times(1)).selectIdByTitle("Test Object", 1);
    }

    @Test
    void selectTopicById() {
        when(objectMapper.selectTopicById(1)).thenReturn(testTopic);
        Topic result = objectMapper.selectTopicById(1);
        assertEquals(testTopic, result);
        verify(objectMapper, times(1)).selectTopicById(1);
    }

    @Test
    void insert() {
        doNothing().when(objectMapper).insert(testObject);
        objectMapper.insert(testObject);
        verify(objectMapper, times(1)).insert(testObject);
    }

    @Test
    void selectAllRemarks() {
        when(objectMapper.selectAllRemarks(1)).thenReturn(remarksList);
        List<Remark> result = objectMapper.selectAllRemarks(1);
        assertEquals(remarksList, result);
        verify(objectMapper, times(1)).selectAllRemarks(1);
    }

    @Test
    void delete() {
        doNothing().when(objectMapper).delete(1);
        objectMapper.delete(1);
        verify(objectMapper, times(1)).delete(1);
    }

    @Test
    void search() {
        when(objectMapper.search("%Test%")).thenReturn(objectsList);
        List<Objects> result = objectMapper.search("%Test%");
        assertEquals(objectsList, result);
        verify(objectMapper, times(1)).search("%Test%");
    }

    @Test
    void selectTop3() {
        when(objectMapper.selectTop3(1)).thenReturn(top3ObjectsList);
        List<top3Object> result = objectMapper.selectTop3(1);
        assertEquals(top3ObjectsList, result);
        verify(objectMapper, times(1)).selectTop3(1);
    }

    @Test
    void updateAveScore() {
        doNothing().when(objectMapper).updateAveScore(8, 2, 1);
        objectMapper.updateAveScore(8, 2, 1);
        verify(objectMapper, times(1)).updateAveScore(8, 2, 1);
    }

    @Test
    void updateHotComment() {
        doNothing().when(objectMapper).updateHotComment(1, "This is a hot comment", 1);
        objectMapper.updateHotComment(1, "This is a hot comment", 1);
        verify(objectMapper, times(1)).updateHotComment(1, "This is a hot comment", 1);
    }
}