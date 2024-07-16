package org.example.jiaoji.service.serverimpl;

import java.util.List;

import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private RemarkMapper remarkMapper;

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
        object.setAveScore(0);
        object.setRemarkNum(0);
        object.setHottestRemark("");
        object.setRemarkId(0);

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

    public Remark getHottestRemark(Integer id) {
        List<Remark> remarks = objectMapper.selectAllRemarks(id);
        Integer likes = 0;
        Remark remark1 = null;
        for (Remark remark : remarks) {
            if (remark.getLike() > likes) {
                likes = remark.getLike();
                remark1 = remark;
            }
        }
        return remark1;
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

    public void updateAveScore(Integer id, Integer score) {
        Objects object = objectMapper.selectOneById(id);
        Integer remarkNum = object.getRemarkNum();
        double newScore = (object.getAveScore() * remarkNum + score)/(remarkNum + 1);
        objectMapper.updateAveScore(newScore, remarkNum + 1, id);
    }

    public void decAveScore(Integer id, Integer score) {
        Objects object = objectMapper.selectOneById(id);
        Integer remarkNum = object.getRemarkNum();
        double newScore = (object.getAveScore() * remarkNum - score)/(remarkNum - 1);
        objectMapper.updateAveScore(newScore, remarkNum - 1, id);
    }

    public void updateHotComment(Integer id, Integer remark_id, Integer change) {
        Objects object = objectMapper.selectOneById(id);
        Integer old_remark_id = object.getRemarkId();
        if(old_remark_id.equals(0)){
            Remark remark =remarkMapper.selectById(remark_id).getFirst();
            objectMapper.updateHotComment(id, remark.getContent(), remark_id);
            return;
        }
        if(old_remark_id.equals(remark_id)) {
            if(change < 0){
                Remark remark = getHottestRemark(id);
                if(remark != null) objectMapper.updateHotComment(object.getId(), remark.getContent(), remark.getId());
                else objectMapper.updateHotComment(id, "", 0);
            }
        }else{
            Remark old_remark = remarkMapper.selectById(old_remark_id).getFirst();
            Remark remark = remarkMapper.selectById(remark_id).getFirst();
            if(old_remark.getLike() < remark.getLike()){
                objectMapper.updateHotComment(id, remark.getContent(), remark_id);
            }
        }
    }
}
