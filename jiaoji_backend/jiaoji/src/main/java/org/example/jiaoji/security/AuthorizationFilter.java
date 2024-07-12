package org.example.jiaoji.security;

import cn.hutool.jwt.JWTUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        // Add paths to be excluded from filtering
        EXCLUDED_PATHS.add("login");
        EXCLUDED_PATHS.add("register");
        // Add more paths as needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        // Debug: Log the request URI and check if it is excluded
        System.out.println("Request URI: " + requestURI);
        System.out.println("Is excluded: " + isExcluded(requestURI));

        if (isExcluded(requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String token = request.getHeader("Authorization");
            // Debug: Log the token presence and verification result
            System.out.println("Token: " + token);
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
        // Check for exact match or if the path starts with any of the excluded paths
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public void destroy() {
        // Filter destruction if needed
    }
}
