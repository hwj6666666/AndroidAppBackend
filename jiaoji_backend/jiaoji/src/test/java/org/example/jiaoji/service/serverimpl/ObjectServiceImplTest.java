package org.example.jiaoji.service.serverimpl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObjectServiceImplTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TopicMapper topicMapper;

    @Mock
    private RemarkMapper remarkMapper;

    @Mock
    private RestHighLevelClient client;

    @InjectMocks
    private ObjectServiceImpl objectService;

    private Objects data;

    @BeforeEach
    void setUp() {
        data = new Objects();
        data.setTitle("Test Title");
        data.setId(1);
        data.setDescription("This is a test description.");
        data.setUserId(1);
        data.setPicture("picture_path");
        data.setTopicId(1);
        data.setRemarkNum(100);
    }

    @Test
    void testInsertObject_ObjectAlreadyExists() {
        when(objectMapper.selectIdByTitle(anyString(), anyInt())).thenReturn(1);

        Integer result = objectService.InsertObject(data);

        assertEquals(-1, result);
        verify(objectMapper, times(1)).selectIdByTitle(anyString(), anyInt());
        verify(objectMapper, never()).insert(any(Objects.class));
    }

    @Test
    void testInsertObject_Success() throws IOException {
        when(objectMapper.selectIdByTitle(anyString(), anyInt())).thenReturn(null);
        Topic topic = new Topic();
        topic.setId(1);
        topic.setObjectNum(0);
        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);
        when(topicMapper.updateObjectNum(anyInt(), anyInt())).thenReturn(1);
        doAnswer(invocation -> {
            Objects object = invocation.getArgument(0);
            object.setId(1); // Simulate auto-increment ID generation
            return null;
        }).when(objectMapper).insert(any(Objects.class));
//        doNothing().when(client).index(any(IndexRequest.class), any(RequestOptions.class));

        Integer result = objectService.InsertObject(data);

        assertNotEquals(-1, result);
        verify(objectMapper, times(1)).selectIdByTitle(anyString(), anyInt());
        verify(objectMapper, times(1)).insert(any(Objects.class));
//        verify(client, times(1)).index(any(IndexRequest.class), any(RequestOptions.class));
    }

    @Test
    void testInsertObject_IOException() throws IOException {
        when(objectMapper.selectIdByTitle(anyString(), anyInt())).thenReturn(null);
        Topic topic = new Topic();
        topic.setId(1);
        topic.setObjectNum(0);
        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);
        when(topicMapper.updateObjectNum(anyInt(),anyInt())).thenReturn(1);
        doNothing().when(objectMapper).insert(any(Objects.class));
        doThrow(IOException.class).when(client).index(any(IndexRequest.class), any(RequestOptions.class));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            objectService.InsertObject(data);
        });

        assertEquals(IOException.class, exception.getCause().getClass());
        verify(objectMapper, times(1)).selectIdByTitle(anyString(), anyInt());
        verify(objectMapper, times(1)).insert(any(Objects.class));
        verify(client, times(1)).index(any(IndexRequest.class), any(RequestOptions.class));
    }

    @Test
    void testSelectAllInTopic() {
        List<Objects> objects = new ArrayList<>();
        objects.add(data);
        when(objectMapper.selectAllInTopic(anyInt())).thenReturn(objects);

        PageInfo<Objects> result = objectService.SelectAllInTopic(1, 10, 1);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        verify(objectMapper, times(1)).selectAllInTopic(anyInt());
    }

    @Test
    void testSelectById() {
        List<Objects> objects = new ArrayList<>();
        objects.add(data);
        when(objectMapper.selectById(anyInt())).thenReturn(objects);

        List<Objects> result = objectService.SelectById(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objectMapper, times(1)).selectById(anyInt());
    }

    @Test
    void testSelectTopicById() {
        Topic topic = new Topic();
        topic.setId(1);
        topic.setTitle("Test Topic");
        when(objectMapper.selectTopicById(anyInt())).thenReturn(topic);

        Topic result = objectService.SelectTopicById(1);

        assertNotNull(result);
        assertEquals("Test Topic", result.getTitle());
        verify(objectMapper, times(1)).selectTopicById(anyInt());
    }

    @Test
    void testGetAveScore_NoRemarks() {
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(null);

        double result = objectService.getAveScore(1);

        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(new ArrayList<>());

        double result1 = objectService.getAveScore(1);

        assertEquals(0, result);
        verify(objectMapper, times(2)).selectAllRemarks(anyInt());
    }

    @Test
    void testGetAveScore_WithRemarks() {
        List<Remark> remarks = new ArrayList<>();
        Remark remark = new Remark();
        remark.setScore(5);
        remarks.add(remark);
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(remarks);

        double result = objectService.getAveScore(1);

        assertEquals(5.0, result);
        verify(objectMapper, times(1)).selectAllRemarks(anyInt());
    }

    @Test
    void testGetHottestRemark_NoRemarks() {
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(Collections.emptyList());

        Remark result = objectService.getHottestRemark(1);

        assertNull(result);
        verify(objectMapper, times(1)).selectAllRemarks(anyInt());
    }

    @Test
    void testGetHottestRemark_WithRemarks() {
        List<Remark> remarks = new ArrayList<>();
        Remark remark = new Remark();
        remark.setLike(10);
        Remark remark1 = new Remark();
        remark1.setLike(0);
        remarks.add(remark);
        remarks.add(remark1);
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(remarks);

        Remark result = objectService.getHottestRemark(1);

        assertNotNull(result);
        assertEquals(10, result.getLike());
        verify(objectMapper, times(1)).selectAllRemarks(anyInt());
    }

    @Test
    void testSearch() throws IOException {
        List<Objects> objects = new ArrayList<>();
        objects.add(data);
        SearchRequest searchRequest = new SearchRequest("object");
        searchRequest.source().query(QueryBuilders.matchQuery("title", "keyword"));
        searchRequest.source().from(0).size(100);
        SearchResponse searchResponse = mock(SearchResponse.class);
        SearchHits searchHits = mock(SearchHits.class);
        SearchHit searchHit = mock(SearchHit.class);
        when(searchResponse.getHits()).thenReturn(searchHits);
        when(searchHits.getHits()).thenReturn(new SearchHit[]{searchHit});
        when(searchHit.getSourceAsString()).thenReturn(JSON.toJSONString(data));
        when(client.search(any(SearchRequest.class), any(RequestOptions.class))).thenReturn(searchResponse);

        List<Objects> result = objectService.search("keyword");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(client, times(1)).search(any(SearchRequest.class), any(RequestOptions.class));
    }

    @Test
    void testSelectTop3() {
        List<top3Object> top3Objects = new ArrayList<>();
        top3Object top3Object = new top3Object();
        top3Objects.add(top3Object);
        when(objectMapper.selectTop3(anyInt())).thenReturn(top3Objects);

        List<top3Object> result = objectService.SelectTop3(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(objectMapper, times(1)).selectTop3(anyInt());
    }

    @Test
    void testDeleteObject_Success() {
        when(objectMapper.selectOneById(anyInt())).thenReturn(null);

        RetType result = objectService.deleteObject(1);

        assertTrue(result.isOk());
        assertEquals("删除成功", result.getMsg());
        verify(objectMapper, times(1)).delete(anyInt());
        verify(objectMapper, times(1)).selectOneById(anyInt());
    }

    @Test
    void testDeleteObject_Failure() {
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);

        RetType result = objectService.deleteObject(1);

        assertFalse(result.isOk());
        assertEquals("删除失败", result.getMsg());
        verify(objectMapper, times(1)).delete(anyInt());
        verify(objectMapper, times(1)).selectOneById(anyInt());
    }

    @Test
    void testUpdateAveScore() {
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);

        objectService.updateAveScore(1, 5);

        verify(objectMapper, times(1)).updateAveScore(anyDouble(), anyInt(), anyInt());
    }

    @Test
    void testDecAveScore() {
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);

        objectService.decAveScore(1, 5);

        verify(objectMapper, times(1)).updateAveScore(anyDouble(), anyInt(), anyInt());
    }

    @Test
    void testUpdateHotComment_NoPreviousRemark() {
        data.setRemarkId(0);
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);
        Remark remark = new Remark();
        remark.setContent("Hot Comment");
        remark.setLike(10);
        when(remarkMapper.selectById(anyInt())).thenReturn(Collections.singletonList(remark));

        objectService.updateHotComment(1, 1, 1);

        verify(objectMapper, times(1)).updateHotComment(anyInt(), anyString(), anyInt());
    }

    @Test
    void testUpdateHotComment_SameRemark_DecreaseLikes() {
        data.setRemarkId(1);
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);
        List<Remark> remarks = new ArrayList<>();
        Remark remark = new Remark();
        remark.setLike(10);
        remarks.add(remark);
        lenient().when(remarkMapper.selectById(anyInt())).thenReturn(remarks);
        List<Remark> n = new ArrayList<>();
        n.add(remark);
        objectService.updateHotComment(1, 1, 1);
        objectService.updateHotComment(1, 1, -1);
        when(objectMapper.selectAllRemarks(anyInt())).thenReturn(n);
//        when(objectService.getHottestRemark(anyInt())).thenReturn(remark);

        objectService.updateHotComment(1, 1, -1);

        verify(objectMapper, times(1)).updateHotComment(anyInt(), anyString(), anyInt());
    }

    @Test
    void testUpdateHotComment_DifferentRemark() {
        data.setRemarkId(1);
        when(objectMapper.selectOneById(anyInt())).thenReturn(data);
        Remark oldRemark = new Remark();
        oldRemark.setLike(5);
        oldRemark.setContent("test");
        Remark newRemark = new Remark();
        newRemark.setLike(10);
        newRemark.setContent("test");
        when(remarkMapper.selectById(1)).thenReturn(Collections.singletonList(oldRemark));
        when(remarkMapper.selectById(2)).thenReturn(Collections.singletonList(newRemark));

        objectService.updateHotComment(1, 2, 1);

        when(remarkMapper.selectById(1)).thenReturn(Collections.singletonList(newRemark));
        when(remarkMapper.selectById(2)).thenReturn(Collections.singletonList(oldRemark));

        objectService.updateHotComment(1, 2, 1);

        verify(objectMapper, times(1)).updateHotComment(anyInt(), anyString(), anyInt());
    }
}