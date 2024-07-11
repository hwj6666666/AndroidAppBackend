package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.ObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    private TopicMapper topicMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private TopicServiceImpl topicService;

    private Topic topic;
    private Objects object;

    @BeforeEach
    void setUp() {
        topic = new Topic();
        topic.setId(1);
        topic.setTitle("Test Topic");
        topic.setClassId(1);
        topic.setIntroduction("Test Introduction");
        topic.setUserId(1);
        topic.setHot(0);
        topic.setPicture("Test Picture");
        topic.setPublicTime(LocalDateTime.now());
        topic.setBase64("Test Base64");

        object = new Objects();
        object.setId(1);
        object.setTitle("Test Object");
        object.setDescription("Test Description");
        object.setUserId(1);
        object.setPicture("Test Picture");
        object.setTopicId(1);
    }

    @Test
    void insertTopic() {
        when(topicMapper.selectIdByTitle(anyString())).thenReturn(null);
        when(topicMapper.selectByTitle(anyString())).thenReturn(topic);

        RetType result = topicService.insertTopic(topic);

        assertTrue(result.isOk());
        assertEquals("上传成功", result.getMsg());
        assertEquals(topic, result.getData());
        verify(topicMapper, times(1)).insert(any(Topic.class));

        when(topicMapper.selectIdByTitle(anyString())).thenReturn(1);

        RetType resultExisting = topicService.insertTopic(topic);

        assertFalse(resultExisting.isOk());
        assertEquals("该话题已存在", resultExisting.getMsg());
        assertNull(resultExisting.getData());
    }

    @Test
    void SelectAll() {
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);

        when(topicMapper.selectAllOrderByHot()).thenReturn(topics);
        when(topicMapper.selectAllOrderByTime()).thenReturn(topics);

        PageInfo<Topic> resultHot = topicService.SelectAll(10, 1, "hot");
        assertEquals(1, resultHot.getList().size());
        verify(topicMapper, times(1)).selectAllOrderByHot();

        PageInfo<Topic> resultTime = topicService.SelectAll(10, 1, "time");
        assertEquals(1, resultTime.getList().size());
        verify(topicMapper, times(1)).selectAllOrderByTime();
    }

    @Test
    void SelectByClassId() {
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);

        when(topicMapper.selectByClassIdOrderByHot(anyInt())).thenReturn(topics);
        when(topicMapper.selectByClassIdOrderByTime(anyInt())).thenReturn(topics);

        PageInfo<Topic> resultHot = topicService.SelectByClassId(1, 10, 1, "hot");
        assertEquals(1, resultHot.getList().size());
        verify(topicMapper, times(1)).selectByClassIdOrderByHot(anyInt());

        PageInfo<Topic> resultTime = topicService.SelectByClassId(1, 10, 1, "time");
        assertEquals(1, resultTime.getList().size());
        verify(topicMapper, times(1)).selectByClassIdOrderByTime(anyInt());
    }

    @Test
    void SelectById() {
        when(topicMapper.selectById(anyInt())).thenReturn(topic);

        Topic result = topicService.SelectById(1);

        assertEquals(topic, result);
        verify(topicMapper, times(1)).selectById(anyInt());
    }

    @Test
    void search() {
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);

        when(topicMapper.search(anyString())).thenReturn(topics);

        PageInfo<Topic> result = topicService.search("keyword", 10, 1);

        assertEquals(1, result.getList().size());
        verify(topicMapper, times(1)).search(anyString());
    }

    @Test
    void setFollow() {
        when(topicMapper.findFollow(anyInt(), anyInt())).thenReturn(true);

        RetType resultUnfollow = topicService.setFollow(1, 1);

        assertTrue(resultUnfollow.isOk());
        assertEquals("取消关注成功", resultUnfollow.getMsg());
        verify(topicMapper, times(1)).deleteFollow(anyInt(), anyInt());

        when(topicMapper.findFollow(anyInt(), anyInt())).thenReturn(false);

        RetType resultFollow = topicService.setFollow(1, 1);

        assertTrue(resultFollow.isOk());
        assertEquals("关注成功", resultFollow.getMsg());
        verify(topicMapper, times(1)).insertFollow(anyInt(), anyInt());
    }

    @Test
    void findFollow() {
        when(topicMapper.findFollow(anyInt(), anyInt())).thenReturn(true);

        Boolean result = topicService.findFollow(1, 1);

        assertTrue(result);
        verify(topicMapper, times(1)).findFollow(anyInt(), anyInt());
    }

    @Test
    void deleteTopic() {
        List<Objects> objects = new ArrayList<>();
        objects.add(object);

        when(objectMapper.selectAllInTopic(anyInt())).thenReturn(objects);
        when(topicMapper.selectById(anyInt())).thenReturn(null);

        RetType result = topicService.deleteTopic(1);

        assertTrue(result.isOk());
        assertEquals("删除成功", result.getMsg());
        when(topicMapper.selectById(anyInt())).thenReturn(topic);
        RetType resultExisting = topicService.deleteTopic(1);
        assertFalse(resultExisting.isOk());
        verify(objectService, times(2)).deleteObject(anyInt());
        verify(topicMapper, times(2)).deleteTopic(anyInt());
    }

    @Test
    void hotTopic() {
        List<Topic> topics = new ArrayList<>();
        topic = new Topic();
        topic.setRemarkNum(10);
        topic.setFavor(5);
        topic.setViews(3);
        topic.setObjectNum(10);
        topic.setPublicTime(LocalDateTime.now());
        Topic topic1 = new Topic();
        topic1.setRemarkNum(9);
        topic1.setFavor(4);
        topic1.setViews(2);
        topic1.setObjectNum(9);
        topic1.setPublicTime(LocalDateTime.now());
        Topic topic2 = new Topic();
        topic2.setRemarkNum(9);
        topic2.setFavor(4);
        topic2.setViews(2);
        topic2.setObjectNum(9);
        topic2.setPublicTime(LocalDateTime.now());
        Topic topic3 = new Topic();
        topic3.setRemarkNum(11);
        topic3.setFavor(6);
        topic3.setViews(4);
        topic3.setObjectNum(11);
        topic3.setPublicTime(LocalDateTime.now());
        topics.add(topic);
        topics.add(topic1);
        topics.add(topic2);
        topics.add(topic3);

        when(topicMapper.selectAll()).thenReturn(topics);

        List<Topic> result = topicService.hotTopic();

        assertEquals(3, result.size());
        verify(topicMapper, times(1)).selectAll();
    }
}
