package org.example.jiaoji.service.serverimpl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RestHighLevelClient client;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RetType retType;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
        user.setState(0);

        retType = new RetType();
    }

    @Test
    void testSelectAll() {
        when(userMapper.selectAll()).thenReturn(Arrays.asList(user));
        List<User> users = userService.SelectAll();
        assertEquals(1, users.size());
        verify(userMapper, times(1)).selectAll();
    }

    @Test
    void testSelectByUserId() {
        when(userMapper.selectByUserId(1)).thenReturn(user);
        User result = userService.SelectByUserId(1);
        assertEquals(user, result);
        verify(userMapper, times(1)).selectByUserId(1);
    }

    @Test
    void testSelectTopicsById() {
        List<Topic> topics = Arrays.asList(new Topic());
        when(userMapper.selectTopicsByUserId(1)).thenReturn(topics);
        List<Topic> result = userService.SelectTopicsById(1);
        assertEquals(topics, result);
        verify(userMapper, times(1)).selectTopicsByUserId(1);
    }

    @Test
    void testSelectObjectsById() {
        List<Objects> objects = Arrays.asList(new Objects());
        when(userMapper.selectObjectsByUserId(1)).thenReturn(objects);
        List<Objects> result = userService.SelectObjectsById(1);
        assertEquals(objects, result);
        verify(userMapper, times(1)).selectObjectsByUserId(1);
    }

    @Test
    void testSelectRemarksById() {
        List<Remark> remarks = Arrays.asList(new Remark());
        when(userMapper.selectRemarksByUserId(1)).thenReturn(remarks);
        List<Remark> result = userService.SelectRemarksById(1);
        assertEquals(remarks, result);
        verify(userMapper, times(1)).selectRemarksByUserId(1);
    }

    @Test
    void testSelectFollows() {
        List<Topic> follows = Arrays.asList(new Topic());
        when(userMapper.selectFollows(1)).thenReturn(follows);
        List<Topic> result = userService.SelectFollows(1);
        assertEquals(follows, result);
        verify(userMapper, times(1)).selectFollows(1);
    }

    @Test
    void testUpdateUser() throws IOException {
        User updatedUser = new User();
        updatedUser.setId(1);
        when(userMapper.selectByUserId(1)).thenReturn(updatedUser);
        IndexResponse response = mock(IndexResponse.class);
        when(client.index(any(), eq(RequestOptions.DEFAULT))).thenReturn(response);

        User result = userService.updateUser(1, user);
        assertEquals(updatedUser, result);
        verify(userMapper, times(1)).update(user);
        verify(client, times(1)).index(any(), eq(RequestOptions.DEFAULT));
    }

    @Test
    void testUpdatePsd() {
        when(userMapper.selectByUserId(1)).thenReturn(user);
        when(userMapper.selectSaltByUid(anyInt())).thenReturn("salt");
        User result = userService.updatePsd(1, user);
        assertEquals(user, result);
        verify(userMapper, times(2)).selectByUserId(1);
        verify(userMapper, times(1)).resetPassword(anyInt(), anyString());
    }

    @Test
    void testGetObjectNameAndTopicNameById() {
        Map<String, String> map = new HashMap<>();
        when(userMapper.selectObjectNameAndTopicNameById(1)).thenReturn(map);
        Map<String, String> result = userService.getObjectNameAndTopicNameById(1);
        assertEquals(map, result);
        verify(userMapper, times(1)).selectObjectNameAndTopicNameById(1);
    }

    @Test
    void testRegister() throws IOException {
        when(userMapper.selectIdByEmail(anyString())).thenReturn(null).thenReturn(1);
        IndexResponse response = mock(IndexResponse.class);
        when(client.index(any(), eq(RequestOptions.DEFAULT))).thenReturn(response);

        RetType result = userService.Register("test@example.com", "password", "avatar", "username");
        assertTrue(result.isOk());
        assertEquals("注册成功", result.getMsg());
        when(userMapper.selectIdByEmail(anyString())).thenReturn(1);
        RetType result1 = userService.Register("test@example.com", "password", "avatar", "username");
        assertFalse(result1.isOk());

        verify(userMapper, times(3)).selectIdByEmail(anyString());
        verify(userMapper, times(1)).insert(anyString(), anyString(), anyString());
        verify(userMapper, times(1)).insertPassword(anyString(), anyInt(), anyString(), anyString());
        verify(client, times(1)).index(any(), eq(RequestOptions.DEFAULT));
    }

    @Test
    void testLogin() {
        when(userMapper.selectSaltByUid(anyInt())).thenReturn("salt");
        when(userMapper.selectIdByEmail(anyString())).thenReturn(null);
        RetType result = userService.Login("test@example.com", "password");
        assertFalse(result.isOk());

        when(userMapper.selectIdByEmail(anyString())).thenReturn(1);
        when(userMapper.selectIdByEmailAndPassword(anyString(), anyString())).thenReturn(null);
        RetType result1 = userService.Login("test@example.com", "password");
        assertFalse(result1.isOk());

        when(userMapper.selectIdByEmailAndPassword(anyString(), anyString())).thenReturn(1);
        User user = new User();
        user.setState(2);
        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
        RetType result2 = userService.Login("test@example.com", "password");
        assertFalse(result2.isOk());

        User user1 = new User();
        user1.setState(1);
        when(userMapper.selectByUserId(anyInt())).thenReturn(user1);
        RetType result3 = userService.Login("test@example.com", "password");
        assertTrue(result3.isOk());

        verify(userMapper, times(4)).selectIdByEmail(anyString());
        verify(userMapper, times(3)).selectIdByEmailAndPassword(anyString(), anyString());
        verify(userMapper, times(2)).selectByUserId(anyInt());
    }

    @Test
    void testSearch() throws IOException {
        SearchResponse response = mock(SearchResponse.class);
        SearchHits searchHits = mock(SearchHits.class);
        SearchHit searchHit = mock(SearchHit.class);

        when(client.search(any(), eq(RequestOptions.DEFAULT))).thenReturn(response);
        when(response.getHits()).thenReturn(searchHits);
        when(searchHits.getHits()).thenReturn(new SearchHit[]{searchHit});
        when(searchHit.getSourceAsString()).thenReturn("{\"id\":1,\"email\":\"test@example.com\",\"username\":\"testuser\"}");

        List<User> users = userService.search("test");
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
        verify(client, times(1)).search(any(), eq(RequestOptions.DEFAULT));
    }

    @Test
    void testBanUser() {
        User user = new User();
        user.setId(1);
        user.setState(0);
        when(userMapper.selectByUserId(anyInt())).thenReturn(user);

        RetType result = userService.banUser(1);
        assertNotNull(result);
        assertFalse(result.isOk());
        assertEquals("超级管理员不可封禁", result.getMsg());

        User user1 = new User();
        user1.setId(2);
        user1.setState(2);
        when(userMapper.selectByUserId(anyInt())).thenReturn(user1);
        RetType result1 = userService.banUser(2);
        assertNotNull(result1);
        assertTrue(result1.isOk());
        assertEquals("解封成功", result1.getMsg());

        User user2 = new User();
        user2.setId(3);
        user2.setState(0);
        when(userMapper.selectByUserId(anyInt())).thenReturn(user2);
        RetType result2 = userService.banUser(3);
        assertNotNull(result2);
        assertTrue(result2.isOk());
        assertEquals("封禁成功", result2.getMsg());

        verify(userMapper, times(3)).selectByUserId(anyInt());
        verify(userMapper, times(1)).updateSuper(anyInt(), eq(2));
    }

    @Test
    void testGetAvater() {
        when(userMapper.getAvater(1)).thenReturn("avatar");
        String result = userService.getAvater(1);
        assertEquals("avatar", result);
        verify(userMapper, times(1)).getAvater(1);
    }

    @Test
    void testGetUserName() {
        when(userMapper.selectByUserId(1)).thenReturn(user);
        String result = userService.getUserName(1);
        assertEquals("testuser", result);
        verify(userMapper, times(1)).selectByUserId(1);
    }

    @Test
    void testCheckNameExist() {
        when(userMapper.checkNameExist("username")).thenReturn(1);
        Boolean result = userService.checkNameExist("username");
        assertTrue(result);
        verify(userMapper, times(1)).checkNameExist("username");
    }

    @Test
    void testReset() {
        when(userMapper.selectIdByEmail("test@example.com")).thenReturn(1);
        when(userMapper.selectSaltByUid(anyInt())).thenReturn("salt");
        RetType result = userService.reset("test@example.com", "newpassword");
        assertTrue(result.isOk());
        assertEquals("重置密码成功", result.getMsg());
        when(userMapper.selectIdByEmail("test@example.com")).thenReturn(null);
        RetType result1 = userService.reset("test@example.com", "newpassword");
        assertFalse(result1.isOk());
        verify(userMapper, times(2)).selectIdByEmail("test@example.com");
        verify(userMapper, times(1)).resetPassword(anyInt(), anyString());
    }
}
