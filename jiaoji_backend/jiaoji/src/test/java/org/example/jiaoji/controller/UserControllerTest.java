package org.example.jiaoji.controller;

import com.alibaba.fastjson2.JSON;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ObjectService objectService;

    @InjectMocks
    private UserController userController;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        lenient().when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void getAllUser() {
        List<User> mockUsers = new ArrayList<>();
        when(userService.SelectAll()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = userController.getAllUser();
        assertEquals(mockUsers, response.getBody());

        verify(userService, times(1)).SelectAll();
    }

    @Test
    void getUserById() {
        User mockUser = new User();
        String jsonUser = JSON.toJSONString(mockUser);
        when(valueOperations.get("userInfo1")).thenReturn(null);
        when(userService.SelectByUserId(anyInt())).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserById(1);
        assertEquals(mockUser, response.getBody());

        when(valueOperations.get("userInfo1")).thenReturn(jsonUser);
        ResponseEntity<User> response1 = userController.getUserById(1);
        assertEquals(mockUser, response1.getBody());


        verify(valueOperations, times(3)).get("userInfo1");
        verify(userService, times(1)).SelectByUserId(anyInt());
    }

    @Test
    void getTopicsById() {
        List<Topic> mockTopics = new ArrayList<>();
        when(userService.SelectTopicsById(anyInt())).thenReturn(mockTopics);

        ResponseEntity<List<Topic>> response = userController.getTopicsById(1);
        assertEquals(mockTopics, response.getBody());

        verify(userService, times(1)).SelectTopicsById(anyInt());
    }

    @Test
    void testGetObjectsById() {
        List<Objects> mockObjects = new ArrayList<>();
        Objects objects = new Objects();
        objects.setId(1);
        mockObjects.add(objects);
        Remark remark = new Remark();
        remark.setContent("test");
        when(userService.SelectObjectsById(anyInt())).thenReturn(mockObjects);
        when(objectService.getAveScore(anyInt())).thenReturn(4.5);
        when(objectService.getHottestRemark(anyInt())).thenReturn(remark);

        ResponseEntity<List<Objects>> response = userController.getObjectsById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).SelectObjectsById(anyInt());
        verify(objectService, times(1)).getAveScore(anyInt());
        verify(objectService, times(1)).getHottestRemark(anyInt());
    }

    @Test
    void getRemarksById() {
        List<Remark> mockRemarks = new ArrayList<>();
        when(userService.SelectRemarksById(anyInt())).thenReturn(mockRemarks);

        ResponseEntity<List<Remark>> response = userController.getRemarksById(1);
        assertEquals(mockRemarks, response.getBody());

        verify(userService, times(1)).SelectRemarksById(anyInt());
    }

    @Test
    void getFollows() {
        List<Topic> mockFollows = new ArrayList<>();
        when(userService.SelectFollows(anyInt())).thenReturn(mockFollows);

        ResponseEntity<List<Topic>> response = userController.getFollows(1);
        assertEquals(mockFollows, response.getBody());

        verify(userService, times(1)).SelectFollows(anyInt());
    }

    @Test
    void updateUser() {
        User mockUser = new User();
        when(userService.updateUser(anyInt(), any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> response = userController.updateUser(1, mockUser);
        assertEquals(mockUser, response.getBody());

        verify(userService, times(1)).updateUser(anyInt(), any(User.class));
    }

    @Test
    void updateUser_userNotFound() {
        when(userService.updateUser(anyInt(), any(User.class))).thenReturn(null);

        ResponseEntity<User> response = userController.updateUser(1, new User());
        assertEquals(ResponseEntity.notFound().build(), response);

        verify(userService, times(1)).updateUser(anyInt(), any(User.class));
    }

    @Test
    void updatePsd() {
        User mockUser = new User();
        when(userService.updatePsd(anyInt(), any(User.class))).thenReturn(mockUser);

        ResponseEntity<User> response = userController.updatePsd(1, mockUser);
        assertEquals(mockUser, response.getBody());

        verify(userService, times(1)).updatePsd(anyInt(), any(User.class));
    }

    @Test
    void getObjectNameAndTopicNameById() {
        Map<String, String> mockResult = new HashMap<>();
        mockResult.put("name", "name");
        when(userService.getObjectNameAndTopicNameById(anyInt())).thenReturn(mockResult);

        ResponseEntity<Map<String, String>> response = userController.getObjectNameAndTopicNameById(1);
        assertEquals(mockResult, response.getBody());

        verify(userService, times(1)).getObjectNameAndTopicNameById(anyInt());
    }

    @Test
    void getObjectNameAndTopicNameById_notFound() {
        when(userService.getObjectNameAndTopicNameById(anyInt())).thenReturn(null);

        ResponseEntity<Map<String, String>> response = userController.getObjectNameAndTopicNameById(1);
        assertEquals(null, response.getBody());

        verify(userService, times(1)).getObjectNameAndTopicNameById(anyInt());
    }

    @Test
    void getObjectNameAndTopicNameById_emptyMap() {
        when(userService.getObjectNameAndTopicNameById(anyInt())).thenReturn(new HashMap<>());

        ResponseEntity<Map<String, String>> response = userController.getObjectNameAndTopicNameById(1);
        assertEquals(ResponseEntity.notFound().build(), response);

        verify(userService, times(1)).getObjectNameAndTopicNameById(anyInt());
    }


    @Test
    void getMethodName() {
        List<User> mockUsers = new ArrayList<>();
        when(userService.search(anyString())).thenReturn(mockUsers);

        List<User> response = userController.getMethodName("keyword");
        assertEquals(mockUsers, response);

        verify(userService, times(1)).search(anyString());
    }

    @Test
    void ban() {
        RetType mockRetType = new RetType();
        when(userService.banUser(anyInt())).thenReturn(mockRetType);

        RetType response = userController.ban(1);
        assertEquals(mockRetType, response);

        verify(userService, times(1)).banUser(anyInt());
    }

    @Test
    void testCheckNameExist() {
        when(userService.checkNameExist(anyString())).thenReturn(true);

        Boolean response = userController.checkNameExist("name");

        assertEquals(true, response);
        verify(userService, times(2)).checkNameExist(anyString());
    }

    @Test
    void testReset() {
        RetType retType = new RetType();
        User user = new User();
        user.setEmail("111");
        user.setPassword("222");
        when(userService.reset(anyString(), anyString())).thenReturn(retType);

        RetType response = userController.reset(user);

        assertEquals(retType, response);
        verify(userService, times(1)).reset(anyString(), anyString());
    }
}
