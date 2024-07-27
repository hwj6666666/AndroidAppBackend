package org.example.jiaoji.controller;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.top3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.util.List;

import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.TopicService;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
public class ObjectController {
    @Autowired
    private ObjectService objectService;
    @Autowired
    private TopicService topicService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/object/{id}")
    @ResponseBody
    public ResponseEntity<PageInfo<Objects>> getObject(@PathVariable("id") Integer id,
                                                       @RequestParam Integer pageIndex,
                                                       @RequestParam Integer pageSize) {
        System.out.println(pageSize);
        PageInfo<Objects> objects;
        topicService.addViews(id);
        String key = "getObjectbyTopicId:" + id + "pageIndex:" + pageIndex + "pageSize:" + pageSize;
        if (stringRedisTemplate.opsForValue().get(key) == null) {
            objects = objectService.SelectAllInTopic(id, pageSize, pageIndex);
            System.out.println(objects);
            String json = JSON.toJSONString(objects);
            stringRedisTemplate.opsForValue().set(key, json, 10, java.util.concurrent.TimeUnit.SECONDS);
            return ResponseEntity.ok(objects);
        } else {
            String json = stringRedisTemplate.opsForValue().get(key);
            objects = JSON.parseObject(json, new TypeReference<PageInfo<Objects>>() {
            });
            System.out.println(objects);
            return ResponseEntity.ok(objects);
        }
    }

    @PostMapping("/object")
    public Integer insert(@RequestBody Objects object) {

        return objectService.InsertObject(object);
    }

    @GetMapping("/object/remark/{id}")
    @ResponseBody
    public ResponseEntity<List<Objects>> getObjectById(@PathVariable("id") Integer id) {
        String key = "object:" + id;
        if (stringRedisTemplate.opsForValue().get(key) == null) {
            List<Objects> objects;
            objects = objectService.SelectById(id);
            String json = JSON.toJSONString(objects);
            stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
            return ResponseEntity.ok(objects);
        } else {
            String json = stringRedisTemplate.opsForValue().get(key);
            List<Objects> objects = JSON.parseObject(json, new TypeReference<List<Objects>>() {
            });
            return ResponseEntity.ok(objects);
        }
    }

    //takes 3s
    @GetMapping("/object/search/{keyword}")
    @ResponseBody
    public List<Objects> getObjectsByTopicId(@PathVariable("keyword") String keyword) {
        List<Objects> objects;
        objects = objectService.search(keyword);
        return objects;
    }

    @GetMapping("/object/top3/{topicId}")
    @ResponseBody
    public List<top3Object> getObjectsByTopicId(@PathVariable("topicId") Integer topicId) {
        if (stringRedisTemplate.opsForValue().get("top3Object:" + topicId) == null) {
            List<top3Object> objects = objectService.SelectTop3(topicId);
            String json = JSON.toJSONString(objects);
            stringRedisTemplate.opsForValue().set("top3Object:" + topicId, json, 600, java.util.concurrent.TimeUnit.SECONDS);
            return objects;
        } else {
            String json = stringRedisTemplate.opsForValue().get("top3Object:" + topicId);
            List<top3Object> objects = JSON.parseObject(json, new TypeReference<List<top3Object>>() {
            });
            return objects;
        }
    }

    @DeleteMapping("/object/{id}")
    @ResponseBody
    public RetType delete(@PathVariable("id") Integer id) {
        RetType ret = objectService.deleteObject(id);
        stringRedisTemplate.delete("object:" + id);
        return ret;
    }

}