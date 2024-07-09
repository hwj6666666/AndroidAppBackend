//package org.example.jiaoji.service.serverimpl;
//
//import org.example.jiaoji.mapper.UserMapper;
//import org.example.jiaoji.pojo.*;
//import org.example.jiaoji.pojo.Objects;
//import org.example.jiaoji.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class UserServiceImplTest {
//
//    @Mock
//    private UserMapper userMapper;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void selectAll() {
//        List<User> users = new ArrayList<>();
//        when(userMapper.selectAll()).thenReturn(users);
//
//        List<User> result = userService.SelectAll();
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectAll();
//    }
//
//    @Test
//    void selectByUserId() {
//        User user = new User();
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//
//        User result = userService.SelectByUserId(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectByUserId(anyInt());
//    }
//
//    @Test
//    void selectTopicsById() {
//        List<Topic> topics = new ArrayList<>();
//        when(userMapper.selectTopicsByUserId(anyInt())).thenReturn(topics);
//
//        List<Topic> result = userService.SelectTopicsById(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectTopicsByUserId(anyInt());
//    }
//
//    @Test
//    void selectObjectsById() {
//        List<Objects> objects = new ArrayList<>();
//        when(userMapper.selectObjectsByUserId(anyInt())).thenReturn(objects);
//
//        List<Objects> result = userService.SelectObjectsById(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectObjectsByUserId(anyInt());
//    }
//
//    @Test
//    void selectRemarksById() {
//        List<Remark> remarks = new ArrayList<>();
//        when(userMapper.selectRemarksByUserId(anyInt())).thenReturn(remarks);
//
//        List<Remark> result = userService.SelectRemarksById(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectRemarksByUserId(anyInt());
//    }
//
//    @Test
//    void selectFollows() {
//        List<Topic> topics = new ArrayList<>();
//        when(userMapper.selectFollows(anyInt())).thenReturn(topics);
//
//        List<Topic> result = userService.SelectFollows(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectFollows(anyInt());
//    }
//
//    @Test
//    void updateUser() {
//        User user = new User();
//        user.setId(1);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//
//        User result = userService.updateUser(1, user);
//        assertNotNull(result);
//        verify(userMapper, times(1)).update(user);
//        verify(userMapper, times(1)).selectByUserId(anyInt());
//    }
//
//    @Test
//    void updatePsd() {
//        User user = new User();
//        user.setId(1);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//
//        User result = userService.updatePsd(1, user);
//        assertNotNull(result);
//        verify(userMapper, times(1)).updateUserPsd(user);
//        verify(userMapper, times(1)).selectByUserId(anyInt());
//    }
//
//    @Test
//    void getObjectNameAndTopicNameById() {
//        Map<String, String> map = new HashMap<>();
//        when(userMapper.selectObjectNameAndTopicNameById(anyInt())).thenReturn(map);
//
//        Map<String, String> result = userService.getObjectNameAndTopicNameById(1);
//        assertNotNull(result);
//        verify(userMapper, times(1)).selectObjectNameAndTopicNameById(anyInt());
//    }
//
//    @Test
//    void register() {
//        when(userMapper.selectIdByEmail(anyString())).thenReturn(null);
//        doNothing().when(userMapper).insert(anyString(), anyString(), anyString());
//
//        RetType result = userService.Register("test@example.com", "password", "avatar");
//        assertNotNull(result);
//        assertTrue(result.isOk());
//        verify(userMapper, times(2)).selectIdByEmail(anyString());
//        verify(userMapper, times(1)).insert(anyString(), anyString(), anyString());
//
//        when(userMapper.selectIdByEmail(anyString())).thenReturn(1);
//        doNothing().when(userMapper).resetPassword(anyInt(), anyString());
//
//        RetType result1 = userService.Register("test@example.com", "password", "avatar");
//        assertFalse(result1.isOk());
//        verify(userMapper, times(3)).selectIdByEmail(anyString());
//        verify(userMapper, times(1)).resetPassword(anyInt(), anyString());
//    }
//
//    @Test
//    void login() {
//        when(userMapper.selectIdByEmail(anyString())).thenReturn(null);
//        RetType result = userService.Login("test@example.com", "password");
//        assertFalse(result.isOk());
//
//        when(userMapper.selectIdByEmail(anyString())).thenReturn(1);
//        when(userMapper.selectIdByEmailAndPassword(anyString(), anyString())).thenReturn(null);
//        RetType result1 = userService.Login("test@example.com", "password");
//        assertFalse(result1.isOk());
//
//        when(userMapper.selectIdByEmailAndPassword(anyString(), anyString())).thenReturn(1);
//        User user = new User();
//        user.setState(2);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//        RetType result2 = userService.Login("test@example.com", "password");
//        assertFalse(result2.isOk());
//
//        User user1 = new User();
//        user1.setState(1);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user1);
//        RetType result3 = userService.Login("test@example.com", "password");
//        assertTrue(result3.isOk());
//
//        verify(userMapper, times(4)).selectIdByEmail(anyString());
//        verify(userMapper, times(3)).selectIdByEmailAndPassword(anyString(), anyString());
//        verify(userMapper, times(2)).selectByUserId(anyInt());
//    }
//
//    @Test
//    void search() {
//        List<User> users = new ArrayList<>();
//        when(userMapper.search(anyString())).thenReturn(users);
//
//        List<User> result = userService.search("keyword");
//        assertNotNull(result);
//        verify(userMapper, times(1)).search(anyString());
//    }
//
//    @Test
//    void banUser() {
//        User user = new User();
//        user.setId(1);
//        user.setState(0);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//
//        RetType result = userService.banUser(1);
//        assertNotNull(result);
//        assertFalse(result.isOk());
//        assertEquals("超级管理员不可封禁", result.getMsg());
//
//        User user1 = new User();
//        user1.setId(2);
//        user1.setState(2);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user1);
//        RetType result1 = userService.banUser(2);
//        assertNotNull(result1);
//        assertTrue(result1.isOk());
//        assertEquals("解封成功", result1.getMsg());
//
//        User user2 = new User();
//        user2.setId(3);
//        user2.setState(0);
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user2);
//        RetType result2 = userService.banUser(3);
//        assertNotNull(result2);
//        assertTrue(result2.isOk());
//        assertEquals("封禁成功", result2.getMsg());
//
//        verify(userMapper, times(3)).selectByUserId(anyInt());
//        verify(userMapper, times(1)).updateSuper(anyInt(), eq(2));
//    }
//
//    @Test
//    void getAvater() {
//        when(userMapper.getAvater(anyInt())).thenReturn("avatar");
//
//        String result = userService.getAvater(1);
//        assertEquals("avatar", result);
//        verify(userMapper, times(1)).getAvater(anyInt());
//    }
//
//    @Test
//    void getUserName() {
//        User user = new User();
//        user.setUsername("username");
//        when(userMapper.selectByUserId(anyInt())).thenReturn(user);
//
//        String result = userService.getUserName(1);
//        assertEquals("username", result);
//        verify(userMapper, times(1)).selectByUserId(anyInt());
//    }
//}
