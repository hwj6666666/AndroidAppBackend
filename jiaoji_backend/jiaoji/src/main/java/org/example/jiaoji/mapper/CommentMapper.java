package org.example.jiaoji.mapper;

import org.apache.ibatis.annotations.*;
import org.example.jiaoji.pojo.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Select("select * from comments where remark_id = #{remarkId}")
    public List<Comment> selectByRemark(Integer remarkId);

    @Select("select * from comments where id = #{id}")
    public List<Comment> selectById(Integer id);

    @Insert("insert into comments(id,user_id,remark_id,content,publish_time) values (#{id},#{userId},#{remarkId},#{content},#{publishTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insert(Comment comment);

    @Delete("delete from comments where id = #{id}")
    public void deleteById(Integer id);
}
