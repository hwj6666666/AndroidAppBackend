//package org.example.jiaoji.controller;
//
//import org.example.jiaoji.pojo.RetType;
//import org.example.jiaoji.pojo.User;
//import org.example.jiaoji.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LoginControllerTest {
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private LoginController loginController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void postMethodName() {
//        User mockUser = new User();
//        mockUser.setEmail("test@example.com");
//        mockUser.setPassword("password");
//
//        RetType mockRetType = new RetType();
//        when(userService.Login(anyString(), anyString())).thenReturn(mockRetType);
//
//        RetType response = loginController.postMethodName(mockUser);
//        assertEquals(mockRetType, response);
//
//        verify(userService, times(1)).Login(anyString(), anyString());
//    }
//
//    @Test
//    void register() {
//        User mockUser = new User();
//        mockUser.setEmail("test@example.com");
//        mockUser.setPassword("password");
//        mockUser.setAvatar("avatar.png");
//
//        RetType mockRetType = new RetType();
//        when(userService.Register(anyString(), anyString(), anyString())).thenReturn(mockRetType);
//
//        RetType response = loginController.register(mockUser);
//        assertEquals(mockRetType, response);
//
//        verify(userService, times(1)).Register(anyString(), anyString(), anyString());
//    }
//}
