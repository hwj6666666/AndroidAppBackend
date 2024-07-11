package org.example.jiaoji.service.serverimpl;

import java.util.List;

import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TopicMapper topicMapper;

    @Transactional
    public Integer InsertObject(Objects data) {
        RetType ret = new RetType();

        Integer id = objectMapper.selectIdByTitle(data.getTitle(), data.getTopicId());
        if (id != null) {
            ret.setMsg("该对象已存在");
            ret.setOk(false);
            ret.setData(null);
            return -1;
        }
        Objects object = new Objects();
        object.setTitle(data.getTitle());
        object.setId(data.getId());
        object.setDescription(data.getDescription());
        object.setUserId(data.getUserId());
        object.setPicture(data.getPicture());

        object.setTopicId(data.getTopicId());
        Topic topic = objectMapper.selectTopicById(object.getTopicId());
        topicMapper.updateObjectNum(topic.getObjectNum() + 1, topic.getId());
        objectMapper.insert(object);
        ret.setMsg("上传成功");

        ret.setOk(true);
        ret.setData(null);
        return object.getId();
    }

    public List<Objects> SelectAllInTopic(Integer id) {
        List<Objects> objects = objectMapper.selectAllInTopic(id);
        return objects;
    }

    public List<Objects> SelectById(Integer id) {
        return objectMapper.selectById(id);
    }

    public Topic SelectTopicById(Integer id) {
        return objectMapper.selectTopicById(id);
    }

    public double getAveScore(Integer id) {
        List<Remark> remarks = objectMapper.selectAllRemarks(id);
        if (remarks == null || remarks.isEmpty()) {
            return 0;
        }
        Integer length = remarks.size();
        double scores = 0;
        for (Remark remark : remarks) {
            scores += remark.getScore();
        }
        return scores / length;
    }

    public String getHottestRemark(Integer id) {
        List<Remark> remarks = objectMapper.selectAllRemarks(id);
        Integer likes = 0;
        String hottestRemark = "";
        for (Remark remark : remarks) {
            if (remark.getLike() > likes) {
                likes = remark.getLike();
                hottestRemark = remark.getContent();
            }
        }
        return hottestRemark;
    }

    public List<Objects> search(String keyword) {
        keyword = "%" + keyword + "%";
        return objectMapper.search(keyword);
    }

    public List<top3Object> SelectTop3(Integer topicId) {
        return objectMapper.selectTop3(topicId);
    }

    public RetType deleteObject(Integer objectId) {
        RetType ret = new RetType();
        objectMapper.delete(objectId);

        if (objectMapper.selectOneById(objectId) == null) {
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

}
