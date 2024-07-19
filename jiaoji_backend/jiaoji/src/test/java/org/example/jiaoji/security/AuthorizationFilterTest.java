package org.example.jiaoji.security;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jiaoji.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationFilterTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthorizationFilter authorizationFilter;

    private static final byte[] SECRET_KEY = "MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8);
    private static final byte[] WRONG_KEY = "MyConstant.JWT_WRONG_KEY".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    void setUp() throws ServletException {
        authorizationFilter.init(new MockFilterConfig());
    }

    @Test
    void testExcludedPath() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/login");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testMissingToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("请先登录获取token", response.getErrorMessage());
    }

    @Test
    void testInvalidTokenPrefix() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "invalid_token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Not Jiao Ji token!", response.getErrorMessage());
    }

    @Test
    void testExpiredToken() throws ServletException, IOException {
        String token = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() - 1000 * 60 * 10)). // 设置为已过期的时间
                        setKey(SECRET_KEY).sign();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "woshinengdie:_" + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Token过期，请重新登录", response.getErrorMessage());
    }




    @Test
    void testInvalidIP() throws ServletException, IOException {
        String token = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "192.168.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(SECRET_KEY).sign();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "woshinengdie:_" + token);
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("ip check failed!", response.getErrorMessage());
    }

    @Test
    void testInvalidEmail() throws ServletException, IOException {
        String token = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(SECRET_KEY).sign();

        when(userMapper.selectIdByEmail("test@example.com")).thenReturn(null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "woshinengdie:_" + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("email check failed!", response.getErrorMessage());
    }

    @Test
    void testValidToken() throws ServletException, IOException {
        String token = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(SECRET_KEY).sign();

        when(userMapper.selectIdByEmail("test@example.com")).thenReturn(1);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "woshinengdie:_" + token);
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testUnauthorizedAccess() throws ServletException, IOException {
        String token = JWT.create().
                setPayload("email", "test@example.com").
                setPayload("ip", "127.0.0.1").
                setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10)).
                setKey(WRONG_KEY).sign();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/resource");
        request.addHeader("Authorization", "woshinengdie:_" + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        authorizationFilter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("token验证失败", response.getErrorMessage());
    }


    @Test
    void testFilterDestroy() {
        authorizationFilter.destroy();
        // Verify any behavior if needed, for now just call the method to ensure it runs without exception
    }
}
