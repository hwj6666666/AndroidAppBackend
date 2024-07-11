package org.example.jiaoji.Topic;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.serverimpl.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TopicServiceTest {

    @Mock
    private TopicMapper topicMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private TopicServiceImpl topicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInsertTopic_TopicExists() {
        Topic topic = new Topic();
        topic.setTitle("Existing Topic");

        when(topicMapper.selectIdByTitle(topic.getTitle())).thenReturn(1);

        RetType result = topicService.insertTopic(topic);

        assertEquals("该话题已存在", result.getMsg());
        assertEquals(false, result.isOk());
    }

    @Test
    public void testInsertTopic_TopicDoesNotExist() {
        Topic topic = new Topic();
        topic.setTitle("New Topic");

        when(topicMapper.selectIdByTitle(topic.getTitle())).thenReturn(null);
        when(topicMapper.insert(any(Topic.class))).thenReturn(1);
        when(topicMapper.selectByTitle(topic.getTitle())).thenReturn(topic);

        RetType result = topicService.insertTopic(topic);

        assertEquals("上传成功", result.getMsg());
        assertEquals(true, result.isOk());
        assertEquals(topic, result.getData());
    }

    @Test
    public void testSelectAll_Hot() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());

        when(topicMapper.selectAllOrderByHot()).thenReturn(topics);

        PageInfo<Topic> result = topicService.SelectAll(10, 1, "hot");

        assertEquals(topics, result.getList());
    }

    @Test
    public void testSelectAll_Time() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());

        when(topicMapper.selectAllOrderByTime()).thenReturn(topics);

        PageInfo<Topic> result = topicService.SelectAll(10, 1, "time");

        assertEquals(topics, result.getList());
    }

    @Test
    public void testSelectByClassId_Hot() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());

        when(topicMapper.selectByClassIdOrderByHot(1)).thenReturn(topics);

        PageInfo<Topic> result = topicService.SelectByClassId(1, 10, 1, "hot");

        assertEquals(topics, result.getList());
    }

    @Test
    public void testSelectByClassId_Time() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());

        when(topicMapper.selectByClassIdOrderByTime(1)).thenReturn(topics);

        PageInfo<Topic> result = topicService.SelectByClassId(1, 10, 1, "time");

        assertEquals(topics, result.getList());
    }

    @Test
    public void testSelectById() {
        Topic topic = new Topic();
        topic.setId(1);

        when(topicMapper.selectById(1)).thenReturn(topic);

        Topic result = topicService.SelectById(1);

        assertEquals(topic, result);
    }

    @Test
    public void testSearch() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic());

        when(topicMapper.search("%keyword%")).thenReturn(topics);

        PageInfo<Topic> result = topicService.search("keyword", 10, 1);

        assertEquals(topics, result.getList());
    }

    @Test
    public void testSetFollow_FollowExists() {
        when(topicMapper.findFollow(1, 1)).thenReturn(true);
       when(topicMapper.deleteFollow(1, 1)).thenReturn(1);

        RetType result = topicService.setFollow(1, 1);

        assertEquals("取消关注成功", result.getMsg());
        assertEquals(true, result.isOk());
    }

    @Test
    public void testSetFollow_FollowDoesNotExist() {
        when(topicMapper.findFollow(1, 1)).thenReturn(false);
        when(topicMapper.insertFollow(1, 1)).thenReturn(1);

        RetType result = topicService.setFollow(1, 1);

        assertEquals("关注成功", result.getMsg());
        assertEquals(true, result.isOk());
    }

    @Test
    public void testFindFollow() {
        when(topicMapper.findFollow(1, 1)).thenReturn(true);

        boolean result = topicService.findFollow(1, 1);

        assertEquals(true, result);
    }

    @Test
    public void testDeleteTopic() {
        List<Objects> objects = new ArrayList<>();
        objects.add(new Objects());

        when(objectMapper.selectAllInTopic(1)).thenReturn(objects);
        doNothing().when(topicMapper).deleteTopic(1);
        when(topicMapper.selectById(1)).thenReturn(null);

        RetType result = topicService.deleteTopic(1);

        assertEquals("删除成功", result.getMsg());
        assertEquals(true, result.isOk());
    }

    @Test
    public void testHotTopic() {
        List<Topic> topics = new ArrayList<>();
        Topic topic1 = new Topic();
        topic1.setRemarkNum(1);
        topic1.setFavor(1);
        topic1.setViews(1);
        topic1.setObjectNum(1);
        topic1.setPublicTime(LocalDateTime.now().minusHours(1));
        topics.add(topic1);

        when(topicMapper.selectAll()).thenReturn(topics);

        List<Topic> result = topicService.hotTopic();

        assertEquals(1, result.size());
        assertEquals(topic1, result.get(0));
    }
}

