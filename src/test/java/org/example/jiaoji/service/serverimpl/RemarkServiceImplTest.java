package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.*;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.service.RemarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RemarkServiceImplTest {

    @Mock
    private RemarkMapper remarkMapper;
    @Mock
    private ObjectMapper objectsMapper;
    @Mock
    private TopicMapper topicMapper;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RemarkServiceImpl remarkService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addRemark() {
        List<Remark> remarks = new ArrayList<>();
        Remark remark = new Remark();
        remark.setUserId(1);
        remark.setObjectId(2);
        Objects object = new Objects();
        object.setTopicId(3);
        Topic topic = new Topic();
        topic.setId(3);
        topic.setRemarkNum(0);
        remarks.add(remark);

        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(objectsMapper.selectOneById(anyInt())).thenReturn(object);
        when(objectsMapper.selectTopicById(anyInt())).thenReturn(topic);
        doNothing().when(remarkMapper).insert(any(Remark.class));
        when(topicMapper.updateRemarkNum(anyInt(), anyInt())).thenReturn(1);

        Integer result = remarkService.addRemark(remark);

        assertEquals(remark.getId(), result);
        verify(remarkMapper, times(1)).selectByUser(anyInt(), anyInt());
        verify(objectsMapper, times(1)).selectOneById(anyInt());
        verify(objectsMapper, times(1)).selectTopicById(anyInt());
        verify(topicMapper, times(1)).updateRemarkNum(anyInt(), anyInt());

        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(remarks);

        Integer result2 = remarkService.addRemark(remark);
        assertEquals(-1, result2);
    }

    @Test
    void selectByObject() {
        List<Remark> remarks = new ArrayList<>();
        when(remarkMapper.selectByObjectIdOrderByLike(anyInt())).thenReturn(remarks);
        when(remarkMapper.selectByObjectIdOrderByTime(anyInt())).thenReturn(remarks);

        PageInfo<Remark> pageInfo = remarkService.SelectByObject(1, 1, 1, true);
        assertNotNull(pageInfo);

        pageInfo = remarkService.SelectByObject(1, 1, 1, false);
        assertNotNull(pageInfo);

        verify(remarkMapper, times(1)).selectByObjectIdOrderByLike(anyInt());
        verify(remarkMapper, times(1)).selectByObjectIdOrderByTime(anyInt());
    }

    @Test
    void selectById() {
        List<Remark> remarks = new ArrayList<>();
        when(remarkMapper.selectById(anyInt())).thenReturn(remarks);

        List<Remark> result = remarkService.SelectById(1);
        assertNotNull(result);
        verify(remarkMapper, times(1)).selectById(anyInt());
    }

    @Test
    void changeLike() {
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        RetType ret = remarkService.changeLike(1, 1, 1);
        assertTrue(ret.isOk());
        assertEquals("点赞成功", ret.getMsg());

        List<RemarkLike> likes = new ArrayList<>();
        likes.add(new RemarkLike());
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(likes);

        ret = remarkService.changeLike(1, 1, 1);
        assertTrue(ret.isOk());
        assertEquals("修改点赞状态成功", ret.getMsg());

        verify(remarkMapper, times(2)).update(anyInt(), anyInt());
        verify(remarkMapper, times(1)).insertLikes(anyInt(), anyInt());
        verify(remarkMapper, times(1)).updateLikeByUid(anyInt(), anyInt());
    }

    @Test
    void deleteRemark() {
        when(remarkMapper.selectById(anyInt())).thenReturn(new ArrayList<>());

        RetType ret = remarkService.deleteRemark(1);
        assertTrue(ret.isOk());
        assertEquals("删除成功", ret.getMsg());

        List<Remark> remarks = new ArrayList<>();
        remarks.add(new Remark());
        when(remarkMapper.selectById(anyInt())).thenReturn(remarks);

        ret = remarkService.deleteRemark(1);
        assertFalse(ret.isOk());
        assertEquals("删除失败", ret.getMsg());

        verify(remarkMapper, times(2)).delete(anyInt());
        verify(remarkMapper, times(2)).selectById(anyInt());
    }

    @Test
    void getAllUser() {
        List<User> users = new ArrayList<>();
        when(userMapper.selectAll()).thenReturn(users);

        List<User> result = remarkService.getAllUser();
        assertNotNull(result);
        verify(userMapper, times(1)).selectAll();
    }

    @Test
    void isLike() {
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        Boolean result = remarkService.isLike(1, 1);
        assertFalse(result);

        List<RemarkLike> likes = new ArrayList<>();
        RemarkLike like = new RemarkLike();
        like.setLiked(true);
        likes.add(like);
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(likes);

        result = remarkService.isLike(1, 1);
        assertTrue(result);
        verify(remarkMapper, times(3)).getLikeByUid(anyInt(), anyInt());
    }

    @Test
    void isRemarked() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        Boolean result = remarkService.isRemarked(1, 1);
        assertFalse(result);

        List<Remark> remarks = new ArrayList<>();
        remarks.add(new Remark());
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(remarks);

        result = remarkService.isRemarked(1, 1);
        assertTrue(result);
        verify(remarkMapper, times(2)).selectByUser(anyInt(), anyInt());
    }

    @Test
    void getScore() {
        List<Remark> remarks = new ArrayList<>();
        when(remarkMapper.selectByObjectIdAndScore(anyInt(), anyInt())).thenReturn(remarks);

        List<Integer> result = remarkService.getScore(1);
        assertNotNull(result);
        assertEquals(5, result.size());

        verify(remarkMapper, times(5)).selectByObjectIdAndScore(anyInt(), anyInt());
    }

    @Test
    void deleteUserObj() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        RetType ret = remarkService.deleteUserObj(1, 1);
        assertTrue(ret.isOk());
        assertEquals("删除成功", ret.getMsg());

        List<Remark> remarks = new ArrayList<>();
        remarks.add(new Remark());
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(remarks);

        ret = remarkService.deleteUserObj(1, 1);
        assertFalse(ret.isOk());
        assertEquals("删除失败", ret.getMsg());

        verify(remarkMapper, times(2)).deleteUserObj(anyInt(), anyInt());
        verify(remarkMapper, times(2)).selectByUser(anyInt(), anyInt());
    }
}
