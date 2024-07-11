package org.example.jiaoji.controller;

import cn.hutool.jwt.JWT;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@CrossOrigin
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/user/login")
    public RetType postMethodName(@RequestBody User user) {
        System.out.println("login");
        System.out.println(user);
        System.out.println(userService.Login(user.getEmail(), user.getPassword()));
        RetType res = userService.Login(user.getEmail(), user.getPassword());
        if (res.isOk()) {
            String token = JWT.create().setPayload("email", user.getEmail()).
                    setExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                    .setKey("MyConstant.JWT_SIGN_KEY".getBytes(StandardCharsets.UTF_8)).sign();
            res.setMsg(token);
        }
        return res;
    }

    @PostMapping("/user/register")
    public RetType register(@RequestBody User user) {

        System.out.println("register");
        System.out.println(user);
        return userService.Register(user.getEmail(), user.getPassword(), user.getAvatar(), user.getUsername());
    }
}
