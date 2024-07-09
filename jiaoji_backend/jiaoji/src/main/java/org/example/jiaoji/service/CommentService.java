package org.example.jiaoji.service;


import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Comment;
import org.example.jiaoji.pojo.RetType;

public interface CommentService {

    public Integer addComment(Comment data);

    public PageInfo<Comment> SelectByRemark(Integer remarkId, Integer pageSize, Integer pageIndex);

    public RetType deleteById(Integer id);
}
