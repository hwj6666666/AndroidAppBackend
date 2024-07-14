package org.example.jiaoji.security;

import cn.hutool.jwt.JWTUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@WebFilter(urlPatterns = "/*")
public class AuthorizationFilter implements Filter {
    private static final byte[] SECRET_KEY = "MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8);
    private static final Set<String> EXCLUDED_PATHS = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        EXCLUDED_PATHS.add("/user/login");
        EXCLUDED_PATHS.add("/user/name");
        EXCLUDED_PATHS.add("/user/register");
        EXCLUDED_PATHS.add("/user/reset");
        EXCLUDED_PATHS.add("/mail");
        EXCLUDED_PATHS.add("/send");
        EXCLUDED_PATHS.add("/class");
        EXCLUDED_PATHS.add("/topic");
        EXCLUDED_PATHS.add("/object");
        System.out.println("filter initialized");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        System.out.println("Request URI: " + requestURI);
        System.out.println("Is excluded: " + isExcluded(requestURI));

        if (isExcluded(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader("Authorization");

            System.out.println("Token: " + token);
            if (!token.startsWith("woshinengdie:_")) {
                System.out.println("Not Jiao Ji token!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Not Jiao Ji token!");
                return;
            }
            token=token.replaceFirst("woshinengdie:_", "");

            System.out.println("Jiao Ji token: " + token);
            if (token != null && JWTUtil.verify(token, SECRET_KEY)) {
                System.out.println("Token verified successfully");
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                System.out.println("Unauthorized access");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
            }
        }
    }

    private boolean isExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public void destroy() {
        System.out.println("Filter destroyed");
    }
}
