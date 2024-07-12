package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

  @Autowired private UserService userService;

  @PostMapping("/user/login")
  public RetType postMethodName(@RequestBody User user) {
    System.out.println("login");
    System.out.println(user);
    System.out.println(userService.Login(user.getEmail(), user.getPassword()));
    return userService.Login(user.getEmail(), user.getPassword());
  }

  @PostMapping("/user/register")
  public RetType register(@RequestBody User user) {

    System.out.println("register");
    System.out.println(user);
    return userService.Register(user.getEmail(), user.getPassword(), user.getAvatar(),user.getUsername());
  }
}
