package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.*;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.jiaoji.pojo.Topic;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Integer addRemark(Remark data) {
        if (!remarkMapper.selectByUser(data.getUserId(), data.getObjectId()).isEmpty()) return -1;
        RetType ret = new RetType();
        remarkMapper.insert(data);
        Objects object = objectsMapper.selectOneById(data.getObjectId());
        Topic topic = objectsMapper.selectTopicById(object.getTopicId());
        topicMapper.updateRemarkNum(topic.getRemarkNum() + 1, topic.getId());
        ret.setMsg("上传成功");
        ret.setOk(true);
        ret.setData(null);
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
        return ret;
    }

    @Override
    public RetType deleteRemark(Integer id) {
        RetType ret = new RetType();
        remarkMapper.delete(id);
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
        List<Integer> scoreList = new ArrayList<>();
        for (int i = 1; i <= 5; ++i)
            scoreList.add(remarkMapper.selectByObjectIdAndScore(objectId, 2 * i).size());
        return scoreList;
    }

    @Override
    public RetType deleteUserObj(Integer objectId, Integer uid) {
        RetType ret=new RetType();
        remarkMapper.deleteUserObj(uid, objectId);
        if (remarkMapper.selectByUser(uid, objectId).isEmpty()) {
            ret.setOk(true);
            ret.setData(null);
            ret.setMsg("删除成功");
        }
        else {
            ret.setOk(false);
            ret.setData(null);
            ret.setMsg("删除失败");
        }
        return ret;
    }
}