package org.example.jiaoji.controller;

import java.util.List;

import com.github.pagehelper.PageInfo;

import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.RemarkService;
import org.example.jiaoji.utils.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

@RestController
@CrossOrigin
public class RemarkController {
    @Autowired
    private RemarkService remarkService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private KafkaProducerService kfkproducer;

    //takes 10s
    @GetMapping("/remarks/{objectId}")
    @ResponseBody
    public ResponseEntity<PageInfo<Remark>> getRemark(@PathVariable("objectId") Integer id, @RequestParam Integer pageIndex,
                                                      @RequestParam Integer pageSize, @RequestParam Boolean orderByHot) {
        return ResponseEntity.ok(remarkService.SelectByObject(id, pageIndex, pageSize, orderByHot));
    }
    //takes 7s
    @GetMapping("/remarks/isRemark/{objectId}/{uid}")
    public ResponseEntity<Boolean> isRemark(@PathVariable("objectId") Integer objectId, @PathVariable("uid") Integer uid) {
        return ResponseEntity.ok(remarkService.isRemarked(objectId, uid));
    }

    @GetMapping("/remarks/score/{objectId}")
    public ResponseEntity<List<Integer>> getScore(@PathVariable("objectId") Integer objectId) {
        kfkproducer.syncGetObjScore("object_getScore", objectId);

        String key="ObjScore:"+objectId;
        if (stringRedisTemplate.opsForValue().get(key) == null) {
            List<Integer> scores = remarkService.getScore(objectId);
            String json = JSON.toJSONString(scores);
            stringRedisTemplate.opsForValue().set(key, json, 3600, java.util.concurrent.TimeUnit.SECONDS);
            return ResponseEntity.ok(scores);
        } else {
            String json = stringRedisTemplate.opsForValue().get(key);
            List<Integer> scores = JSON.parseObject(json, new TypeReference<List<Integer>>() {});
            return ResponseEntity.ok(scores);
        }

    }

    @GetMapping("/remarks/changeLike/{id}/{change}/{uid}")
    public RetType changeLike(@PathVariable("id") Integer id, @PathVariable("change") Integer change,
            @PathVariable("uid") Integer uid) {
        return remarkService.changeLike(id, change, uid);
    }

    @GetMapping("/remarks/delete/{id}")
    public RetType delete(@PathVariable("id") Integer id) {
        return remarkService.deleteRemark(id);
    }

    @GetMapping("/remarks/deleteUserObj/{objectId}/{uid}")
    public RetType deleteUserObj(@PathVariable("objectId") Integer objectId,@PathVariable("uid") Integer uid) {
        return remarkService.deleteUserObj(objectId, uid);
    }

    //takes 5s
    @GetMapping("/getAllUser")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(remarkService.getAllUser());
    }

    @PostMapping("/remarks")
    public Integer insert(@RequestBody Remark remark) {
        Integer num = remarkService.addRemark(remark);
        stringRedisTemplate.delete("ObjScore:"+remark.getObjectId());
        return num;
    }

    @GetMapping("/remarks/getLike/{id}/{uid}")
    public ResponseEntity<Boolean> getLike(@PathVariable("id") Integer id, @PathVariable("uid") Integer uid) {
        return ResponseEntity.ok(remarkService.isLike(id, uid));
    }
}
