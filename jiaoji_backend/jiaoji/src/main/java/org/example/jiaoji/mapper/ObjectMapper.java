package org.example.jiaoji.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;

@Mapper
public interface ObjectMapper {
    @Select("select * from object") // 由于这里是用的注解，所以不需要写xml文件，但需要将原来的xml文件删除，不然会报错
    public List<Objects> selectAll();

    @Select("select * from object where topic_id = #{id}")
    public List<Objects> selectAllInTopic(Integer id);

    @Select("select * from object where id = #{id}")
    public List<Objects> selectById(Integer id);
    @Select("select * from object where id = #{id}")
    public Objects selectOneById(Integer Id);
    @Select("select id from object where title = #{title} and topic_id = #{topic_id}")
    public Integer selectIdByTitle(String title, Integer topic_id);
    @Select("select * from object where title=#{title}")
    public Objects selectByTitle(String title);

    @Select("select * from topic where id = #{id}")
    public Topic selectTopicById(Integer id);

    @Insert("insert into object(id,title,topic_id,user_id,picture,description) values(#{id},#{title},#{topicId},#{userId},#{picture},#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void insert(Objects object);

    @Select("select * from remarks where object_id = #{object_id}")
    public List<Remark> selectAllRemarks(Integer object_id);

    @Delete("delete from object where id = #{id}")
    public void delete(Integer id);

    @Select("select * from object where title like #{keyword} or description like #{keyword}")
    public List<Objects> search(String keyword);

    @Select("select id,title from object where topic_id = #{topicId} order by id desc limit 3")
    public List<top3Object> selectTop3(Integer topicId);

    @Update("update object set remark_num = #{new_remark_num}, aveScore = #{score} where id = #{id}")
    public void updateAveScore(double score, Integer new_remark_num, Integer id);

    @Update("update object set hottestRemark = #{hot_comment}, remark_id = #{remark_id} where id = #{id}")
    public void updateHotComment(Integer id, String hot_comment, Integer remark_id);
}
