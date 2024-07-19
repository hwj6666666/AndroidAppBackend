package org.example.jiaoji.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.ClassService;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

@RestController
@CrossOrigin
public class ClassController {
    @Autowired
    private ClassService classService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/class")
    public List<Class> getMethodName() {
       String key = "class";
         if (stringRedisTemplate.opsForValue().get(key) == null) {
              List<Class> classes = classService.selectAll();
              String json = JSON.toJSONString(classes);
              stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
              return classes;
         } else {
              String json = stringRedisTemplate.opsForValue().get(key);
              List<Class> classes = JSON.parseObject(json, new TypeReference<List<Class>>() {});
              return classes;
         }
    }

    @PostMapping("/class/addFavor")
    public RetType getMethodName2(@RequestParam Integer classId, @RequestParam Integer userId) {
        return classService.addFavor(classId, userId);
    }

    @PostMapping("/class/deleteFavor")
    public RetType getMethodName3(@RequestParam Integer classId, @RequestParam Integer userId) {
        return classService.deleteFavor(classId, userId);
    }

    @GetMapping("/class/favor")
    public List<Integer> getMethodName4(@RequestParam Integer userId) {
        return classService.selectFavor(userId);
    }

}
