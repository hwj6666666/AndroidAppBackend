package org.example.jiaoji.service;

import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;

import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TopicService {

    public RetType insertTopic(Topic data);

    public PageInfo<Topic> SelectAll(Integer pageSize, Integer pageIndex, String type);

    public Topic SelectById(Integer Id);

    public PageInfo<Topic> SelectByClassId(Integer id, Integer pageSize, Integer pageIndex, String type);

    public PageInfo<Topic> search(String keyword, Integer pageSize, Integer pageIndex);

    public RetType setFollow(Integer topicId, Integer userId);

    public Boolean findFollow(Integer topicId, Integer userId);

    public RetType deleteTopic(Integer topicId);

    public List<Topic> hotTopic();

    public void addViews(Integer topicId);
}