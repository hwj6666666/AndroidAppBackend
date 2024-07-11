package org.example.jiaoji.service.serverimpl;

import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.service.ObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private ObjectServiceImpl objectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void InsertObject() {
        Objects data = new Objects();
        data.setId(1);
        data.setTitle("Title");
        data.setTopicId(2);

        Topic topic = new Topic();
        topic.setId(2);
        topic.setObjectNum(5);

        when(objectMapper.selectIdByTitle(anyString(), anyInt())).thenReturn(null);
        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);
        when(topicMapper.updateObjectNum(anyInt(), anyInt())).thenReturn(1);
        doNothing().when(objectMapper).insert(any(Objects.class));

        Integer result = objectService.InsertObject(data);

        assertEquals(1, result);
        verify(objectMapper, times(1)).selectIdByTitle(anyString(), anyInt());
        verify(objectMapper, times(1)).selectTopicById(anyInt());
        verify(topicMapper, times(1)).updateObjectNum(anyInt(), anyInt());
        verify(objectMapper, times(1)).insert(any(Objects.class));

        when(objectMapper.selectIdByTitle(anyString(), anyInt())).thenReturn(1);
        Integer result2 = objectService.InsertObject(data);
        assertEquals(-1, result2);
        verify(objectMapper, times(2)).selectIdByTitle(anyString(), anyInt());
        verify(objectMapper, times(1)).selectTopicById(anyInt());
        verify(topicMapper, times(1)).updateObjectNum(anyInt(), anyInt());
        verify(objectMapper, times(1)).insert(any(Objects.class));
    }

    @Test
    void SelectAllInTopic() {
        List<Objects> objects = new ArrayList<>();
        Objects object = new Objects();
        object.setId(1);
        object.setTopicId(2);
        objects.add(object);

        Topic topic = new Topic();
        topic.setId(2);
        topic.setViews(10);

        when(objectMapper.selectAllInTopic(anyInt())).thenReturn(objects);
        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);
        when(topicMapper.updateViews(anyInt(),anyInt())).thenReturn(1);

        List<Objects> result = objectService.SelectAllInTopic(2);

        assertEquals(1, result.size());
        verify(objectMapper, times(1)).selectAllInTopic(anyInt());
        verify(objectMapper, times(1)).selectTopicById(anyInt());
        verify(topicMapper, times(1)).updateViews(anyInt(), anyInt());
    }

    @Test
    void SelectById() {
        List<Objects> objects = new ArrayList<>();
        Objects object = new Objects();
        object.setId(1);
        objects.add(object);

        when(objectMapper.selectById(anyInt())).thenReturn(objects);

        List<Objects> result = objectService.SelectById(1);

        assertEquals(1, result.size());
        verify(objectMapper, times(1)).selectById(anyInt());
    }

    @Test
    void SelectTopicById() {
        Topic topic = new Topic();
        topic.setId(1);

        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);

        Topic result = objectService.SelectTopicById(1);

        assertEquals(1, result.getId());
        verify(objectMapper, times(1)).selectTopicById(anyInt());
    }

    @Test
    void getAveScore() {
        List<Remark> remarks = new ArrayList<>();
        Remark remark1 = new Remark();
        remark1.setScore(4);
        remarks.add(remark1);
        Remark remark2 = new Remark();
        remark2.setScore(6);
        remarks.add(remark2);

        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(remarks);

        double result = objectService.getAveScore(1);

        assertEquals(5.0, result);
        verify(objectMapper, times(1)).selectAllRemarks(anyInt());

        List<Remark> remarks1 = new ArrayList<>();
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(remarks1);
        double result1 = objectService.getAveScore(1);
        assertEquals(0.0, result1);
        verify(objectMapper, times(2)).selectAllRemarks(anyInt());

        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(null);
        double result2 = objectService.getAveScore(1);
        assertEquals(0.0, result2);
    }

    @Test
    void getHottestRemark() {
        List<Remark> remarks = new ArrayList<>();
        Remark remark1 = new Remark();
        remark1.setLike(10);
        remark1.setContent("Remark 1");
        remarks.add(remark1);
        Remark remark2 = new Remark();
        remark2.setLike(20);
        remark2.setContent("Remark 2");
        remarks.add(remark2);
        Remark remark3 = new Remark();
        remark3.setLike(15);
        remark3.setContent("Remark 3");
        remarks.add(remark3);

        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(remarks);

        String result = objectService.getHottestRemark(1);

        assertEquals("Remark 2", result);
        verify(objectMapper, times(1)).selectAllRemarks(anyInt());
    }

    @Test
    void search() {
        List<Objects> objects = new ArrayList<>();
        Objects object = new Objects();
        object.setId(1);
        objects.add(object);

        when(objectMapper.search(anyString())).thenReturn(objects);

        List<Objects> result = objectService.search("keyword");

        assertEquals(1, result.size());
        verify(objectMapper, times(1)).search(anyString());
    }

    @Test
    void SelectTop3() {
        List<top3Object> top3Objects = new ArrayList<>();
        top3Object topObject = new top3Object();
        topObject.setId(1);
        top3Objects.add(topObject);

        when(objectMapper.selectTop3(anyInt())).thenReturn(top3Objects);

        List<top3Object> result = objectService.SelectTop3(1);

        assertEquals(1, result.size());
        verify(objectMapper, times(1)).selectTop3(anyInt());
    }

    @Test
    void deleteObject() {
        doNothing().when(objectMapper).delete(anyInt());
        when(objectMapper.selectOneById(anyInt())).thenReturn(null);

        RetType result = objectService.deleteObject(1);

        assertEquals(true, result.isOk());
        assertEquals("删除成功", result.getMsg());
        verify(objectMapper, times(1)).delete(anyInt());
        verify(objectMapper, times(1)).selectOneById(anyInt());

        // Test failure case
        Objects object = new Objects();
        object.setId(1);
        when(objectMapper.selectOneById(anyInt())).thenReturn(object);

        result = objectService.deleteObject(1);

        assertEquals(false, result.isOk());
        assertEquals("删除失败", result.getMsg());
        verify(objectMapper, times(2)).delete(anyInt());
        verify(objectMapper, times(2)).selectOneById(anyInt());
    }
}
