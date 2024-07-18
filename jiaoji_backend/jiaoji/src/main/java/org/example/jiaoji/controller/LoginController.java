package org.example.jiaoji.controller;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.JWTValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private static final byte[] SECRET_KEY = "MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8);

    @PostMapping("/user/login")
    public RetType postMethodName(@RequestBody User user, HttpServletRequest request) {
        System.out.println("login");
        System.out.println(user);
        RetType res = new RetType();
        res.setData(null);

        Subject subject = SecurityUtils.getSubject();
        AuthenticationToken shiroToken = new UsernamePasswordToken(user.getEmail(), user.getPassword());
        try {
            subject.login(shiroToken);

            String clientIp = request.getRemoteAddr();
            String accessToken = JWT.create().
                    setPayload("email", user.getEmail()).
                    setPayload("ip", clientIp).
                    setExpiresAt(new Date(System.currentTimeMillis() + 1 * 60 * 10)).
                    setKey(SECRET_KEY).sign();

            String refreshToken = JWT.create().
                    setPayload("email", user.getEmail()).
                    setPayload("ip", clientIp).
                    setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)).
                    setKey(SECRET_KEY).sign();

            res.setMsg("登录成功！");
            res.setOk(true);
            res.setData(Map.of("uid", userMapper.selectIdByEmail(user.getEmail()), "refreshToken", refreshToken,
                    "accessToken", accessToken));
        } catch (AuthenticationException e) {
            res.setOk(false);
            res.setMsg(e.getMessage());
        }
        System.out.println(res.getData());
        return res;
    }

    @PostMapping("/refresh/token")
    public RetType refreshToken(@RequestBody String refreshToken) {
        RetType res = new RetType();
        System.out.println("Refreshing token!?!");
        System.out.println(refreshToken);

        try {
            JWTValidator validator = JWTValidator.of(JWTUtil.parseToken(refreshToken));
            validator.validateDate();
        } catch (ValidateException e) {
            res.setOk(false);
            res.setMsg("refresh token无效，请重新登录");
            return res;
        }

        try {
            JWT refreshTokenJwt = JWTUtil.parseToken(refreshToken);
            System.out.println("OHHH: " + refreshTokenJwt.verify());
            String email = JWTUtil.parseToken(refreshToken).getPayloads().getStr("email");
            String clientIp = JWTUtil.parseToken(refreshToken).getPayloads().getStr("ip");

            String newAccessToken = JWT.create()
                    .setPayload("email", email)
                    .setPayload("ip", clientIp)
                    .setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                    .setKey(SECRET_KEY).sign();

            res.setOk(true);
            res.setMsg("Token refreshed successfully");
            res.setData(newAccessToken);
        } catch (Exception e) {
            res.setOk(false);
            res.setMsg("Failed to refresh token: " + e.getMessage());
        }
        System.out.println(res.getMsg());
        return res;
    }

    @PostMapping("/user/register")
    public RetType register(@RequestBody User user) {

        System.out.println("register");
        System.out.println(user);
        return userService.Register(user.getEmail(), user.getPassword(), user.getAvatar(), user.getUsername());
    }
}
