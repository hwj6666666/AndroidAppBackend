package org.example.jiaoji.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin
public class UserController {
  @Autowired private UserService userService;

  @Autowired private ObjectService objectService;

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

    //takes 5s
    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userService.SelectAll();
        return ResponseEntity.ok(users);
    }

  @GetMapping("/user/{id}")
  @ResponseBody
  public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
      String key="userInfo"+id;
    if (stringRedisTemplate.opsForValue().get(key) == null) {
      User user = userService.SelectByUserId(id);
      String json = JSON.toJSONString(user);
      stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
      return ResponseEntity.ok(user);
    }
    else {
      String json = stringRedisTemplate.opsForValue().get(key);
      User user = JSON.parseObject(json, new TypeReference<User>() {});
      return ResponseEntity.ok(user);
    }
  }

  @GetMapping("/user/{id}/topics")
  @ResponseBody
  public ResponseEntity<List<Topic>> getTopicsById(@PathVariable("id") Integer id) {
    List<Topic> topics = userService.SelectTopicsById(id);
    return ResponseEntity.ok(topics);
  }

    //takes 7s
    @GetMapping("/user/{id}/objects")
    @ResponseBody
    public ResponseEntity<List<Objects>> getObjectsById(@PathVariable("id") Integer id) {
        List<Objects> objects = userService.SelectObjectsById(id);
        for (Objects object : objects) {
            object.setAveScore(objectService.getAveScore(object.getId()));
            object.setHottestRemark(objectService.getHottestRemark(object.getId()).getContent());
        }
        return ResponseEntity.ok(objects);
    }

  @GetMapping("/user/{id}/remarks")
  @ResponseBody
  public ResponseEntity<List<Remark>> getRemarksById(@PathVariable("id") Integer id) {
    List<Remark> remark = userService.SelectRemarksById(id);
    return ResponseEntity.ok(remark);
  }

  @GetMapping("/user/{id}/follows")
  @ResponseBody
  public ResponseEntity<List<Topic>> getFollows(@PathVariable("id") Integer id) {
    List<Topic> follow = userService.SelectFollows(id);
    return ResponseEntity.ok(follow);
  }

  @PutMapping("/user/{id}")
  public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
    User updatedUser = userService.updateUser(id, user);
    //        updatedUser.setPassword("");
    if (updatedUser == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedUser);
  }

  @PutMapping("/psd/{id}")
  public ResponseEntity<User> updatePsd(@PathVariable("id") Integer id, @RequestBody User user) {
    User updatedPsdUser = userService.updatePsd(id, user);
    //        暂时直接返回
    //        updatedPsdUser.setPassword("");
    return ResponseEntity.ok(updatedPsdUser);
  }

  @GetMapping("/user/object/{id}/nameAndTopic")
  @ResponseBody
  public ResponseEntity<Map<String, String>> getObjectNameAndTopicNameById(
      @PathVariable("id") Integer id) {
    Map<String, String> result = userService.getObjectNameAndTopicNameById(id);
    if (result == null || result.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping("user/search/{keyword}")
  public List<User> getMethodName(@PathVariable("keyword") String keyword) {
    return userService.search(keyword);
  }

  @GetMapping("user/ban/{id}")
  public RetType ban(@PathVariable("id") Integer id) {
    return userService.banUser(id);
  }

  @GetMapping("user/name/{name}")
  public Boolean checkNameExist(@PathVariable String name) {
    System.out.println("checkNameExist" + userService.checkNameExist(name));
    return userService.checkNameExist(name);
  }

  @PostMapping("user/reset")
  public RetType reset(@RequestBody User user) {
    return userService.reset(user.getEmail(), user.getPassword());
  }
  // test for cicd 4
}
