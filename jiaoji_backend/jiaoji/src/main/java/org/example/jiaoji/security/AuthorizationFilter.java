 package org.example.jiaoji.security;

 import cn.hutool.core.exceptions.ValidateException;
 import cn.hutool.jwt.JWT;
 import cn.hutool.jwt.JWTUtil;

 import cn.hutool.jwt.JWTValidator;
 import jakarta.servlet.Filter;
 import jakarta.servlet.FilterChain;
 import jakarta.servlet.FilterConfig;
 import jakarta.servlet.ServletException;
 import jakarta.servlet.ServletRequest;
 import jakarta.servlet.ServletResponse;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 import org.example.jiaoji.mapper.UserMapper;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.data.redis.core.StringRedisTemplate;
 import org.springframework.stereotype.Component;

 import java.io.IOException;
 import java.nio.charset.StandardCharsets;
 import java.util.HashSet;
 import java.util.Set;

 @Component
 public class AuthorizationFilter implements Filter {
     private static final byte[] SECRET_KEY = "MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8);
     private static final Set<String> EXCLUDED_PATHS = new HashSet<>();

     @Autowired
     UserMapper userMapper;
     @Autowired
     private StringRedisTemplate stringRedisTemplate;

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
         EXCLUDED_PATHS.add("/refresh/token");
         EXCLUDED_PATHS.add("/actuator/health");
         System.out.println("filter initialized");
     }

     @Override
     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
         HttpServletRequest request = (HttpServletRequest) servletRequest;
         HttpServletResponse response = (HttpServletResponse) servletResponse;

         String requestURI = request.getRequestURI();

         System.out.println("Request URI: " + requestURI);
 //        System.out.println("Is excluded: " + isExcluded(requestURI));

         if (isExcluded(requestURI)) {
             filterChain.doFilter(servletRequest, servletResponse);
         } else {
             String token;
             if ((token = request.getHeader("Authorization")) == null) {
                 System.out.println("No token request!");
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "请先登录获取token");
                 return;
             }
             if(stringRedisTemplate.opsForValue().get(token.replaceFirst("woshinengdie:_", "")) != null){
                 filterChain.doFilter(servletRequest, servletResponse);
             }else
             {
                 if (!token.startsWith("woshinengdie:_")) {
                 System.out.println("Not Jiao Ji token!");
                 response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not Jiao Ji token!");
                 return;
                 }

                 token = token.replaceFirst("woshinengdie:_", "");

                 if (JWTUtil.verify(token, SECRET_KEY)) {
                     JWTValidator validator = JWTValidator.of(JWTUtil.parseToken(token));
                     try {
                         validator.validateDate();
                     } catch (ValidateException e) {
                         System.out.println("Token should be updated!");
                         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token过期，请重新登录");
                         return;
                     }

                     JWT parsedToken = JWTUtil.parseToken(token);

                     if (!parsedToken.getPayloads().getStr("ip").equals(request.getRemoteAddr())) {
                         System.out.println("ip check failed!");
                         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ip check failed!");
                         return;
                     }

                     if (!parsedToken.getPayloads().getStr("tokenType").equals("access")) {
                         System.out.println("tokenType check failed!");
                         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "tokenType check failed!");
                         return;
                     }

                     if (userMapper.selectIdByEmail(parsedToken.getPayloads().getStr("email")) == null) {
                         System.out.println("email check failed!");
                         response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "email check failed!");
                         return;
                     }

                     System.out.println("Token verified successfully");
                     stringRedisTemplate.opsForValue().set(token, "1", 600, java.util.concurrent.TimeUnit.SECONDS);
                     filterChain.doFilter(servletRequest, servletResponse);
                 } else {
                     System.out.println("Unauthorized access");
                     response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token验证失败");
                     return;
                 }
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
