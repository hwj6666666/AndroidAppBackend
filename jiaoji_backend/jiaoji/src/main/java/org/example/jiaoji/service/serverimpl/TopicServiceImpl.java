package org.example.jiaoji.service.serverimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.TopicService;
import org.example.jiaoji.utils.LockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.concurrent.locks.Lock;
import java.time.temporal.ChronoUnit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    public static final int viewsRate = 1;
    public static final int remarkRate = 8;
    public static final int favorRate = 15;
    public static final int objectRate = 5;

     private final LockManager lockMap = new LockManager(); 

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private RestHighLevelClient client;

    public RetType insertTopic(Topic data) {
        RetType ret = new RetType();

        Integer id = topicMapper.selectIdByTitle(data.getTitle());
        if (id != null) {
            ret.setMsg("该话题已存在");
            ret.setOk(false);
            ret.setData(null);
            return ret;
        }
        Topic topic = new Topic();
        topic.setTitle(data.getTitle());
        topic.setClassId(data.getClassId());
        topic.setIntroduction(data.getIntroduction());
        topic.setUserId(data.getUserId());
        topic.setHot(0);
        topic.setPicture("this is topic picture");
        topic.setPublicTime(java.time.LocalDateTime.now());
        topic.setBase64(data.getBase64());
        topicMapper.insert(topic);
        IndexRequest request = new IndexRequest("topic").id(topic.getId().toString());
        // 2.准备Json文档
        request.source(JSON.toJSONString(topic), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ret.setMsg("上传成功");
        ret.setOk(true);
        ret.setData(topicMapper.selectByTitle(topic.getTitle()));
        return ret;
    }

    public PageInfo<Topic> SelectAll(Integer pageSize, Integer pageIndex, String type) {
        
        if(type.equals("hot")){
            PageHelper.startPage(pageIndex, pageSize);
            List<Topic> topics = topicMapper.selectAllOrderByHot();
            PageInfo<Topic> pageInfo = new PageInfo<>(topics);
            return pageInfo;
        }else{
            PageHelper.startPage(pageIndex, pageSize);
            List<Topic> topics = topicMapper.selectAllOrderByTime();
            PageInfo<Topic> pageInfo = new PageInfo<>(topics);
            return pageInfo;
        }
    }

    public PageInfo<Topic> SelectByClassId(Integer id, Integer pageSize, Integer pageIndex, String type) {
       if(type.equals("hot")){
            PageHelper.startPage(pageIndex, pageSize);
            List<Topic> topics = topicMapper.selectByClassIdOrderByHot(id);
            PageInfo<Topic> pageInfo = new PageInfo<>(topics);
            return pageInfo;
        }else{
            PageHelper.startPage(pageIndex, pageSize);
            List<Topic> topics = topicMapper.selectByClassIdOrderByTime(id);
            PageInfo<Topic> pageInfo = new PageInfo<>(topics);
            return pageInfo;
        }
       
        // for (Topic topic : topics) {
        // int remarkNum = topic.getRemarkNum() * remarkRate;
        // int favor = topic.getFavor() * favorRate;
        // int views = topic.getViews() * viewsRate;
        // int objectNum = topic.getObjectNum() * objectRate;
        // LocalDateTime publicTime = topic.getPublicTime();
        // LocalDateTime now = LocalDateTime.now();
        // double hours = ChronoUnit.HOURS.between(publicTime, now) / 24;
        // double hot = (remarkNum + favor + views + objectNum) / (Math.pow(hours + 2,
        // 1.2));
        // topic.setHot((int) hot);
        // }
    }

    @Override
    public Topic SelectById(Integer Id) {
        return topicMapper.selectById(Id);
    }

    @Override
    public PageInfo<Topic> search(String keyword, Integer pageSize, Integer pageIndex) {
//        keyword = "%" + keyword + "%";
//        PageHelper.startPage(pageIndex, pageSize);
//        List<Topic> topics = topicMapper.search(keyword);
//        PageInfo<Topic> pageInfo = new PageInfo<>(topics);
//        return pageInfo;
        List<Topic> topics = new ArrayList<>();
        SearchRequest request = new SearchRequest("topic");
        // 组织DSL参数
        request.source()
                .query(QueryBuilders.multiMatchQuery(keyword, "title", "introduction"));
        request.source().from((pageIndex - 1) * pageSize).size(pageSize);
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        for(SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            Topic topic = JSON.parseObject(json, Topic.class);
            topics.add(topic);
        }
        PageInfo<Topic> pageInfo = new PageInfo<>(topics);
        return pageInfo;
    }

    @Override
    @Transactional
    public RetType setFollow(Integer topicId, Integer userId) {
        RetType ret = new RetType();
        Boolean follow = topicMapper.findFollow(topicId, userId);
        if (follow) {
            topicMapper.deleteFollow(topicId, userId);
            topicMapper.subFavor(topicId);
            ret.setMsg("取消关注成功");
            ret.setOk(true);
            ret.setData(null);
        } else {
            topicMapper.insertFollow(topicId, userId);
            topicMapper.addFavor(topicId);
            ret.setMsg("关注成功");
            ret.setOk(true);
            ret.setData(null);
        }
        return ret;
    }

    @Override
    public Boolean findFollow(Integer topicId, Integer userId) {
        return topicMapper.findFollow(topicId, userId);
    }

    @Override
    public RetType deleteTopic(Integer topicId) {
        RetType ret = new RetType();
        List<Objects> toDelete = objectMapper.selectAllInTopic(topicId);
        for (Objects objects : toDelete) {
            objectService.deleteObject(objects.getId());
        }

        topicMapper.deleteTopic(topicId);

        if (topicMapper.selectById(topicId) == null) {
            ret.setMsg("删除成功");
            ret.setOk(true);
            ret.setData(null);
        } else {
            ret.setMsg("删除失败");
            ret.setOk(false);
            ret.setData(null);
        }
        return ret;
    }

    public List<Topic> hotTopic() {
        List<Topic> topics = topicMapper.selectAll();
        for (Topic topic : topics) {
            int remarkNum = topic.getRemarkNum() * remarkRate;
            int favor = topic.getFavor() * favorRate;
            int views = topic.getViews() * viewsRate;
            int objectNum = topic.getObjectNum() * objectRate;
            LocalDateTime publicTime = topic.getPublicTime();
            LocalDateTime now = LocalDateTime.now();
            double hours = ChronoUnit.HOURS.between(publicTime, now) / 24;
            double hot = (remarkNum + favor + views + objectNum) / (Math.pow(hours + 2, 1.2));
            topic.setHot((int) hot);
        }

        List<Topic> topThreeHotTopics = topics.stream()
                .sorted((t1, t2) -> Integer.compare(t2.getHot(), t1.getHot())) // 按 hot 值降序排序
                .limit(3) // 取前 3 个元素
                .collect(Collectors.toList());

        return topThreeHotTopics;
    }

    @Async
    public void addViews(Integer topicId) {
       Lock lock = lockMap.getLock(topicId);
        lock.lock();
        try {
            topicMapper.addViews(topicId);
        } finally {
            lock.unlock();
        }
    }
}
