package org.example.jiaoji.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.github.pagehelper.PageInfo;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.TopicService;
import org.example.jiaoji.utils.KafkaProducerService;

@RestController
@CrossOrigin
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private TopicService topicService;
    @Autowired
    private KafkaProducerService kfkproducer;

    //takes 3s
    @GetMapping("/{id}")
    public ResponseEntity<PageInfo<Topic>> getTopic(@PathVariable("id") Integer id,
                                                    @RequestParam Integer pageIndex,
                                                    @RequestParam Integer pageSize,
                                     @RequestParam String type) {
        try {
            String key = "id" + id + "pageIndex" + pageIndex + "pageSize" + pageSize + "type" + type;
            if (stringRedisTemplate.opsForValue().get(key) == null) {
                PageInfo<Topic> topic;
                if (id == 0) {
                    topic = topicService.SelectAll(pageSize, pageIndex, type);
                } else {
                    topic = topicService.SelectByClassId(id, pageSize, pageIndex, type);
                }
                String json = JSON.toJSONString(topic);
                stringRedisTemplate.opsForValue().set(key, json, 8, java.util.concurrent.TimeUnit.SECONDS);
                return ResponseEntity.ok(topic);
            } else {
                String json = stringRedisTemplate.opsForValue().get(key);
                PageInfo<Topic> topic = JSON.parseObject(json, new TypeReference<PageInfo<Topic>>() {});
                return ResponseEntity.ok(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //takes 3s
    @GetMapping("/object/{id}")
    public ResponseEntity<Topic> getOneTopic(@PathVariable("id") Integer id) {
        try {
            String key = "topicId:" + id;
            if (stringRedisTemplate.opsForValue().get(key) == null) {
                Topic topic = topicService.SelectById(id);
                String json = JSON.toJSONString(topic);
                stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
                return ResponseEntity.ok(topic);
            } else {
                kfkproducer.sendMessage("topic_views",id.toString());
                String json = stringRedisTemplate.opsForValue().get(key);
                Topic topic = JSON.parseObject(json, new TypeReference<Topic>() {});
                return ResponseEntity.ok(topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public RetType insert(@RequestBody Topic test) {
        return topicService.insertTopic(test);
    }

    //takes 5s
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageInfo<Topic>> search(@PathVariable("keyword") String keyword,
                                                  @RequestParam Integer pageIndex,
                                                  @RequestParam Integer pageSize) {
        PageInfo<Topic> topic = topicService.search(keyword, pageSize, pageIndex);
        return ResponseEntity.ok(topic);
    }

    
    // @PostMapping("/follow")
    // public RetType follow(@RequestParam Integer userId, @RequestParam Integer topicId) {
    //     return topicService.setFollow(topicId, userId);
    // }

    @PostMapping("/follow")
    public RetType follow(@RequestParam Integer userId, @RequestParam Integer topicId) {
   
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("userId", userId);
        map.put("topicId", topicId);
    kfkproducer.sendMessage("topic_follow",map);
    return new RetType(true, "修改成功", null);
    //return topicService.setFollow(topicId, userId);
    }

    @GetMapping("/follow")
    public boolean unfollow(@RequestParam Integer userId, @RequestParam Integer topicId) {
        return topicService.findFollow(topicId, userId);
    }

    @DeleteMapping("/{id}")
    public RetType delete(@PathVariable("id") Integer id) {
        RetType ret = topicService.deleteTopic(id);
        stringRedisTemplate.delete("topicId:" + id);
        return ret;
    }

    @GetMapping("/hot")
    public List<Topic> hotTopic() {
        try {
            String key = "hotTopic";
            if (stringRedisTemplate.opsForValue().get(key) == null) {
                List<Topic> topic = topicService.hotTopic();
                String json = JSON.toJSONString(topic);
                stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
                return topic;
            } else {
                String json = stringRedisTemplate.opsForValue().get(key);
                List<Topic> topic = JSON.parseObject(json, new TypeReference<List<Topic>>() {});
                return topic;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
