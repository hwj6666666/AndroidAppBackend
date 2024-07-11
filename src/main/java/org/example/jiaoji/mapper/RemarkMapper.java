package org.example.jiaoji.mapper;

import org.apache.ibatis.annotations.*;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RemarkLike;

import java.util.List;

@Mapper
public interface RemarkMapper {
    @Select("select * from remarks where object_id = #{objectId}")
    public List<Remark> selectByObject(Integer objectId);

    @Select("select * from remarks where object_id = #{objectId} Order By publish_time DESC")
    public List<Remark> selectByObjectIdOrderByTime(Integer objectId);

    @Select("select * from remarks where object_id = #{objectId} Order By `like` DESC")
    public List<Remark> selectByObjectIdOrderByLike(Integer objectId);

    @Select("select * from remarks where id = #{id}")
    public List<Remark> selectById(Integer id);

    @Select("select * from remarks where object_id=#{objectId} and score=#{score}")
    public List<Remark> selectByObjectIdAndScore(Integer objectId, Integer score);

    @Select("select * from remarks where user_id = #{uid} and object_id = #{objectId}")
    public List<Remark> selectByUser(Integer uid, Integer objectId);

    @Insert("insert into remarks(user_id,object_id,content,`like`,score,publish_time) values (#{userId},#{objectId},#{content},#{like},#{score},#{publishTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insert(Remark remark);

    @Update("update remarks set `like` = #{change} + `like` where id=#{id}")
    public void update(Integer id, Integer change);

    @Delete("delete from remarks where id=#{id}")
    public void delete(Integer id);

    @Delete("delete from remarks where user_id = #{uid} and object_id = #{objectId}")
    public void deleteUserObj(Integer uid, Integer objectId);

    @Select("select * from rmk_likes where uid = #{uid} and rmk_id = #{remarkId}")
    public List<RemarkLike> getLikeByUid(Integer uid, Integer remarkId);

    @Update("update rmk_likes set `liked` = 1 - `liked` where uid = #{uid} and rmk_id = #{remarkId}")
    public void updateLikeByUid(Integer uid, Integer remarkId);

    @Insert("insert into rmk_likes(uid,rmk_id) values (#{uid},#{remarkId})")
    public void insertLikes(Integer uid, Integer remarkId);
}
