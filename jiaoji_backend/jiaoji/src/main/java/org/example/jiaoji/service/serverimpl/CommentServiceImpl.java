package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.CommentMapper;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Comment;
import org.example.jiaoji.service.CommentService;
import org.example.jiaoji.utils.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private KafkaProducerService kfkproducer;

    @Transactional
    @Override
    public Integer addComment(Comment data) {
        RetType ret = new RetType();
        commentMapper.insert(data);

        kfkproducer.sendMessage("addRemarkNum", data.getRemarkId());
        ret.setMsg("上传成功");
        ret.setOk(true);
        ret.setData(null);
        return data.getId();
    }

    @Override
    public PageInfo<Comment> SelectByRemark(Integer remarkId, Integer pageSize, Integer pageIndex) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Comment> res= commentMapper.selectByRemark(remarkId);
        return new PageInfo<>(res);
    }

    @Override
    public RetType deleteById(Integer id) {
        RetType ret = new RetType();
        commentMapper.deleteById(id);

        if (commentMapper.selectById(id).isEmpty()) {
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
