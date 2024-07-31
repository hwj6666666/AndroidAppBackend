package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.example.jiaoji.mapper.*;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.service.ObjectService;
import org.example.jiaoji.service.RemarkService;
import org.example.jiaoji.utils.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemarkServiceImpl implements RemarkService {
    @Autowired
    private RemarkMapper remarkMapper;
    @Autowired
    private ObjectMapper objectsMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private KafkaProducerService kafkaProducer;

    @Override
    public Integer addRemark(Remark data) {
        if (!remarkMapper.selectByUser(data.getUserId(), data.getObjectId()).isEmpty()) 
            return -1;
        remarkMapper.insert(data);
        remarkMapper.updateScore("score"+data.getScore(), data.getObjectId());
        Objects object = objectsMapper.selectOneById(data.getObjectId());
        Topic topic = objectsMapper.selectTopicById(object.getTopicId());
        topicMapper.updateRemarkNum(topic.getRemarkNum() + 1, topic.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("objectId", data.getObjectId());
        map.put("score", data.getScore());
        kafkaProducer.sendMessage("object_updateAveScore", map);
        //objectService.updateAveScore(data.getObjectId(), data.getScore());

        return data.getId();
    }

    @Override
    public PageInfo<Remark> SelectByObject(Integer objectId, Integer pageIndex, Integer pageSize, Boolean orderByHot) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Remark> rmk = orderByHot ? remarkMapper.selectByObjectIdOrderByLike(objectId) : remarkMapper.selectByObjectIdOrderByTime(objectId);
        return new PageInfo<>(rmk);
    }

    @Override
    public List<Remark> SelectById(Integer id) {
        return remarkMapper.selectById(id);
    }

    @Override
    public RetType changeLike(Integer id, Integer change, Integer uid) {
        RetType ret = new RetType();
        remarkMapper.update(id, change);
        if (remarkMapper.getLikeByUid(uid, id).isEmpty()) {
            remarkMapper.insertLikes(uid, id);
            ret.setMsg("点赞成功");
            ret.setOk(true);
            ret.setData(null);
        } else {
            remarkMapper.updateLikeByUid(uid, id);
            ret.setMsg("修改点赞状态成功");
            ret.setOk(true);
            ret.setData(null);
        }
        Remark remark = remarkMapper.selectById(id).getFirst();
        objectService.updateHotComment(remark.getObjectId(), remark.getId(), change);

        Map<String, Object> map = new HashMap<>();
        map.put("objectId", remark.getObjectId());
        map.put("id", remark.getId());
        map.put("change", change);
        kafkaProducer.sendMessage("object_updateHotComment", map);
        
        return ret;
    }

    @Override
    public RetType deleteRemark(Integer id) {
        RetType ret = new RetType();
        Remark remark = remarkMapper.SelectOneById(id);
        if (remark != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("objectId", remark.getObjectId());
            map.put("score", remark.getScore());
            kafkaProducer.sendMessage("object_decAveScore", map);
        }
        remarkMapper.delete(id);
        remarkMapper.updateScoreSub("score" + remark.getScore(), remark.getObjectId());
        if (remarkMapper.selectById(id).isEmpty()) {
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

    @Override
    public List<User> getAllUser() {
        return userMapper.selectAll();
    }

    @Override
    public Boolean isLike(Integer remarkId, Integer uid) {
        if (remarkMapper.getLikeByUid(uid, remarkId).isEmpty()) {
            return false;
        } else return remarkMapper.getLikeByUid(uid, remarkId).get(0).getLiked();
    }

    @Override
    public Boolean isRemarked(Integer objectId, Integer uid) {
        return !remarkMapper.selectByUser(uid, objectId).isEmpty();
    }

    @Override
    public List<Integer> getScore(Integer objectId) {
        RemarkScore res = remarkMapper.selectScore(objectId);
        List<Integer> score = new ArrayList<>();
        score.add(res.getScore2());
        score.add(res.getScore4());
        score.add(res.getScore6());
        score.add(res.getScore8());
        score.add(res.getScore10());
        return score;
    }

    @Override
    public RetType deleteUserObj(Integer objectId, Integer uid) {
        RetType ret = new RetType();
        List<Remark> remarks = remarkMapper.selectByUser(uid, objectId);
        if(!remarks.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("objectId", remarks.get(0).getObjectId());
            map.put("score", remarks.get(0).getScore());
            kafkaProducer.sendMessage("object_decAveScore", map);
            //objectService.decAveScore(remarks.get(0).getObjectId(), remarks.get(0).getScore());
            }
        remarkMapper.deleteUserObj(uid, objectId);
        if (remarkMapper.selectByUser(uid, objectId).isEmpty()) {
            ret.setOk(true);
            ret.setData(null);
            ret.setMsg("删除成功");
        } else {
            ret.setOk(false);
            ret.setData(null);
            ret.setMsg("删除失败");
        }
        return ret;
    }
}