package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.*;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.service.ObjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemarkServiceImplTest {

    @Mock
    private RemarkMapper remarkMapper;

    @Mock
    private ObjectMapper objectsMapper;

    @Mock
    private TopicMapper topicMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private RemarkServiceImpl remarkService;

    private Remark remark;
    private Objects object;
    private Topic topic;
    private RetType retType;
    private User user;
    private RemarkScore remarkScore;

    @BeforeEach
    void setUp() {
        remark = new Remark();
        remark.setId(1);
        remark.setUserId(1);
        remark.setObjectId(1);
        remark.setScore(4);

        object = new Objects();
        object.setId(1);
        object.setTopicId(1);

        topic = new Topic();
        topic.setId(1);
        topic.setRemarkNum(10);

        retType = new RetType();

        user = new User();
        user.setId(1);

        remarkScore = new RemarkScore();
        remarkScore.setScore2(1);
        remarkScore.setScore4(1);
        remarkScore.setScore6(1);
        remarkScore.setScore8(1);
        remarkScore.setScore10(1);
    }

    @Test
    void testAddRemark() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        when(objectsMapper.selectOneById(anyInt())).thenReturn(object);
        when(objectsMapper.selectTopicById(anyInt())).thenReturn(topic);
        when(topicMapper.updateRemarkNum(anyInt(), anyInt())).thenReturn(1);
        doNothing().when(objectService).updateAveScore(anyInt(), anyInt());
        doNothing().when(remarkMapper).updateScore(anyString(), anyInt());

        Integer result = remarkService.addRemark(remark);
        assertEquals(remark.getId(), result);

        verify(remarkMapper, times(1)).insert(any(Remark.class));
        verify(topicMapper, times(1)).updateRemarkNum(anyInt(), anyInt());
        verify(objectService, times(1)).updateAveScore(anyInt(), anyInt());
        verify(remarkMapper, times(1)).updateScore(anyString(), anyInt());
    }

    @Test
    void testSelectByObject() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectByObjectIdOrderByLike(anyInt())).thenReturn(remarks);
        PageInfo<Remark> pageInfo = remarkService.SelectByObject(1, 1, 10, true);

        assertNotNull(pageInfo);
        assertEquals(remarks, pageInfo.getList());

        verify(remarkMapper, times(1)).selectByObjectIdOrderByLike(anyInt());
    }

    @Test
    void testSelectById() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectById(anyInt())).thenReturn(remarks);

        List<Remark> result = remarkService.SelectById(1);

        assertEquals(remarks, result);

        verify(remarkMapper, times(1)).selectById(anyInt());
    }

    @Test
    void testChangeLike() {
        when(remarkMapper.selectById(anyInt())).thenReturn(List.of(remark));
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(remarkMapper).update(anyInt(), anyInt());
        doNothing().when(remarkMapper).insertLikes(anyInt(), anyInt());
        doNothing().when(objectService).updateHotComment(anyInt(), anyInt(), anyInt());

        RetType result = remarkService.changeLike(1, 1, 1);

        assertTrue(result.isOk());
        assertEquals("点赞成功", result.getMsg());

        verify(remarkMapper, times(1)).update(anyInt(), anyInt());
        verify(remarkMapper, times(1)).insertLikes(anyInt(), anyInt());
        verify(objectService, times(1)).updateHotComment(anyInt(), anyInt(), anyInt());
    }

    @Test
    void testDeleteRemark() {
        when(remarkMapper.SelectOneById(anyInt())).thenReturn(remark);
        doNothing().when(objectService).decAveScore(anyInt(), anyInt());
        doNothing().when(remarkMapper).delete(anyInt());
        when(remarkMapper.selectById(anyInt())).thenReturn(new ArrayList<>());

        RetType result = remarkService.deleteRemark(1);

        assertTrue(result.isOk());
        assertEquals("删除成功", result.getMsg());

        verify(objectService, times(1)).decAveScore(anyInt(), anyInt());
        verify(remarkMapper, times(1)).delete(anyInt());
    }

    @Test
    void testGetAllUser() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userMapper.selectAll()).thenReturn(users);

        List<User> result = remarkService.getAllUser();

        assertEquals(users, result);

        verify(userMapper, times(1)).selectAll();
    }

    @Test
    void testIsLike() {
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        Boolean result = remarkService.isLike(1, 1);

        assertFalse(result);

        verify(remarkMapper, times(1)).getLikeByUid(anyInt(), anyInt());
    }

    @Test
    void testIsRemarked() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        Boolean result = remarkService.isRemarked(1, 1);

        assertFalse(result);

        verify(remarkMapper, times(1)).selectByUser(anyInt(), anyInt());
    }

    @Test
    void testGetScore() {
        when(remarkMapper.selectScore(anyInt())).thenReturn(remarkScore);

        List<Integer> result = remarkService.getScore(1);

        assertEquals(5, result.size());
        assertTrue(result.contains(1));

        verify(remarkMapper, times(1)).selectScore(anyInt());
    }

    @Test
    void testDeleteUserObj() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(remarks);
//        doNothing().when(objectService).decAveScore(anyInt(), anyInt());
        doNothing().when(remarkMapper).deleteUserObj(anyInt(), anyInt());
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        RetType result = remarkService.deleteUserObj(1, 1);

        assertTrue(result.isOk());
        assertEquals("删除成功", result.getMsg());

//        verify(objectService, times(1)).decAveScore(anyInt(), anyInt());
        verify(remarkMapper, times(1)).deleteUserObj(anyInt(), anyInt());
    }

    @Test
    void testAddRemark_UserAlreadyRemarked() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(List.of(remark));

        Integer result = remarkService.addRemark(remark);

        assertEquals(-1, result);

        verify(remarkMapper, times(1)).selectByUser(anyInt(), anyInt());
    }

    @Test
    void testSelectByObject_OrderByTime() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectByObjectIdOrderByTime(anyInt())).thenReturn(remarks);

        PageInfo<Remark> pageInfo = remarkService.SelectByObject(1, 1, 10, false);

        assertNotNull(pageInfo);
        assertEquals(remarks, pageInfo.getList());

        verify(remarkMapper, times(1)).selectByObjectIdOrderByTime(anyInt());
    }

    @Test
    void testChangeLike_UpdateLikeByUid() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectById(anyInt())).thenReturn(remarks);
        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(List.of(new RemarkLike()));

        doNothing().when(remarkMapper).update(anyInt(), anyInt());
        doNothing().when(remarkMapper).updateLikeByUid(anyInt(), anyInt());
        doNothing().when(objectService).updateHotComment(anyInt(), anyInt(), anyInt());

        RetType result = remarkService.changeLike(1, 1, 1);

        assertTrue(result.isOk());
        assertEquals("修改点赞状态成功", result.getMsg());

        verify(remarkMapper, times(1)).update(anyInt(), anyInt());
        verify(remarkMapper, times(1)).updateLikeByUid(anyInt(), anyInt());
        verify(objectService, times(1)).updateHotComment(anyInt(), anyInt(), anyInt());
    }

    @Test
    void testDeleteRemark_Failure() {
        when(remarkMapper.SelectOneById(anyInt())).thenReturn(remark);
        doNothing().when(objectService).decAveScore(anyInt(), anyInt());
        doNothing().when(remarkMapper).delete(anyInt());
        when(remarkMapper.selectById(anyInt())).thenReturn(List.of(remark));

        RetType result = remarkService.deleteRemark(1);

        assertFalse(result.isOk());
        assertEquals("删除失败", result.getMsg());

        verify(objectService, times(1)).decAveScore(anyInt(), anyInt());
        verify(remarkMapper, times(1)).delete(anyInt());
    }

    @Test
    void testIsLike_FoundLike() {
        List<RemarkLike> likes = new ArrayList<>();
        RemarkLike like = new RemarkLike();
        like.setLiked(true);
        likes.add(like);

        when(remarkMapper.getLikeByUid(anyInt(), anyInt())).thenReturn(likes);

        Boolean result = remarkService.isLike(1, 1);

        assertTrue(result);

        verify(remarkMapper, times(2)).getLikeByUid(anyInt(), anyInt());
    }

    @Test
    void testDeleteUserObj_NoRemarks() {
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(remarkMapper).deleteUserObj(anyInt(), anyInt());

        RetType result = remarkService.deleteUserObj(1, 1);

        assertTrue(result.isOk());
        assertEquals("删除成功", result.getMsg());

        verify(remarkMapper, times(1)).deleteUserObj(anyInt(), anyInt());
    }

    @Test
    void testDeleteUserObj_Failure() {
        List<Remark> remarks = new ArrayList<>();
        remarks.add(remark);

        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(remarks);
        doNothing().when(objectService).decAveScore(anyInt(), anyInt());
        doNothing().when(remarkMapper).deleteUserObj(anyInt(), anyInt());
        when(remarkMapper.selectByUser(anyInt(), anyInt())).thenReturn(List.of(remark));

        RetType result = remarkService.deleteUserObj(1, 1);

        assertFalse(result.isOk());
        assertEquals("删除失败", result.getMsg());

        verify(objectService, times(1)).decAveScore(anyInt(), anyInt());
        verify(remarkMapper, times(1)).deleteUserObj(anyInt(), anyInt());
    }
}