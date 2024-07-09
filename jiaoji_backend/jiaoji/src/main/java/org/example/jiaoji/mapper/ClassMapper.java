package org.example.jiaoji.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.jiaoji.pojo.Class;

@Mapper
public interface ClassMapper {
     
    @Select("select * from class")
    public List<Class> selectAll();

    @Insert("insert into user_favor_class(class_id,user_id) values(#{classId},#{userId})")
    public int addFavor(Integer classId, Integer userId);

    @Delete("delete from user_favor_class where class_id = #{classId} and user_id = #{userId}")
    public int deleteFavor(Integer classId, Integer userId);

    @Select("select class_id from user_favor_class where user_id = #{userId}")
    public List<Integer> selectFavor(Integer userId);
}
