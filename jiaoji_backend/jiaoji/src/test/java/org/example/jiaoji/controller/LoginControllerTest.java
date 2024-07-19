package org.example.jiaoji.controller;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Subject subject;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private LoginController loginController;

    private static final byte[] SECRET_KEY = "MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8);
    private static final byte[] WRONG_KEY = "MyConstant.JWT_WRONG_KEY".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

        // Initialize Shiro SecurityManager
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        SecurityUtils.setSecurityManager(securityManager);

        // Bind the subject to the current thread
        ThreadContext.bind(subject);
    }

    @Test
    public void testPostMethodName() throws Exception {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            User user = new User();
            user.setEmail("test@example.com");
            user.setPassword("password");

            securityUtilsMockedStatic.when(SecurityUtils::getSubject).thenReturn(subject);
            lenient().when(request.getRemoteAddr()).thenReturn("127.0.0.1");
            doNothing().when(subject).login(any());

            when(userMapper.selectIdByEmail(anyString())).thenReturn(1);

            mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(true))
                    .andExpect(jsonPath("$.msg").value("登录成功！"))
                    .andExpect(jsonPath("$.data.uid").value(1))
                    .andExpect(jsonPath("$.data.accessToken").exists())
                    .andExpect(jsonPath("$.data.refreshToken").exists());

            verify(subject, times(1)).login(any());
            verify(userMapper, times(1)).selectIdByEmail(anyString());
        }
    }

    @Test
    public void testPostMethodNameAuthenticationException() throws Exception {
        try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class)) {
            User user = new User();
            user.setEmail("test@example.com");
            user.setPassword("password");

            securityUtilsMockedStatic.when(SecurityUtils::getSubject).thenReturn(subject);
            doThrow(new AuthenticationException("Authentication failed")).when(subject).login(any());

            mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"email\":\"test@example.com\",\"password\":\"password\"}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(false))
                    .andExpect(jsonPath("$.msg").value("Authentication failed"));

            verify(subject, times(1)).login(any());
        }
    }

    @Test
    public void testRefreshToken() throws Exception {
        String refreshToken = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(SECRET_KEY).sign();

        mockMvc.perform(MockMvcRequestBuilders.post("/refresh/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.msg").value("Token refreshed successfully"))
                .andExpect(jsonPath("$.data").exists());

        verify(userMapper, times(0)).selectIdByEmail(anyString()); // No DB interaction for refreshing token
    }

    @Test
    public void testRefreshTokenInvalid() throws Exception {
        String invalidRefreshToken = JWT.create().
                setPayload("email", "test@example").
                setPayload("ip", "127").
                setExpiresAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)).
                setKey(WRONG_KEY).sign();

        mockMvc.perform(MockMvcRequestBuilders.post("/refresh/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRefreshToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.msg").value("refresh token无效，请重新登录"));

        verify(userMapper, times(0)).selectIdByEmail(anyString());
    }

    @Test
    public void testRefreshTokenException() throws Exception {
        String refreshToken = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(SECRET_KEY).sign();

        try (MockedStatic<JWTUtil> jwtUtilMockedStatic = mockStatic(JWTUtil.class)) {
            jwtUtilMockedStatic.when(() -> JWTUtil.parseToken(refreshToken)).thenThrow(new RuntimeException("Parsing error"));

            mockMvc.perform(MockMvcRequestBuilders.post("/refresh/token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(refreshToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ok").value(false))
                    .andExpect(jsonPath("$.msg").value("Failed to refresh token: Parsing error"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testRegister() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setAvatar("avatar");
        user.setUsername("username");

        RetType retType = new RetType();
        retType.setOk(true);
        retType.setMsg("注册成功");

        when(userService.Register(anyString(), anyString(), anyString(), anyString())).thenReturn(retType);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"password\",\"avatar\":\"avatar\",\"username\":\"username\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.msg").value("注册成功"));

        verify(userService, times(1)).Register(anyString(), anyString(), anyString(), anyString());
    }
}
