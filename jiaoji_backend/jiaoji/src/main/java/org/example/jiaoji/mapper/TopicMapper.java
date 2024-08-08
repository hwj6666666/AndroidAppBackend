package org.example.jiaoji.mapper;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.example.jiaoji.pojo.Topic;

@Mapper
public interface TopicMapper {
    @Select("select * from topic Order By hot DESC limit 3") // 由于这里是用的注解，所以不需要写xml文件，但需要将原来的xml文件删除，不然会报错
    public List<Topic> selecthot3();
    @Select("select * from topic Order By public_time DESC") // 由于这里是用的注解，所以不需要写xml文件，但需要将原来的xml文件删除，不然会报错
    public List<Topic> selectAllOrderByTime();
    @Select("select * from topic Order By hot DESC") // 由于这里是用的注解，所以不需要写xml文件，但需要将原来的xml文件删除，不然会报错
    public List<Topic> selectAllOrderByHot();

    @Select("select * from topic where id = #{id} ")
    public Topic selectById(Integer Id);

    @Select("select * from topic where class_id = #{id} Order By public_time DESC")
    public List<Topic> selectByClassIdOrderByTime(Integer id);
     @Select("select * from topic where class_id = #{id} Order By hot DESC")
    public List<Topic> selectByClassIdOrderByHot(Integer id);

    @Select("select id from topic where title = #{title}")
    public Integer selectIdByTitleOrderByTime(String title);

    @Select("select id from topic where title = #{title}")
    public Integer selectIdByTitle(String title);

    @Select("select * from topic where title = #{keyword}")
    public Topic selectByTitle(String keyword);

    @Insert("insert into topic(class_id,user_id,title,picture,introduction,hot,public_time,base64) values(#{classId},#{userId},#{title},#{picture},#{introduction},#{hot},#{publicTime},#{base64})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public int insert(Topic topic);

    @Select("select * from topic where title like #{keyword}")
    public List<Topic> search(String keyword);

    @Update("update topic set views = #{views} where id = #{id}")
    public int updateViews(Integer views, Integer id);

    @Update("update topic set remark_num = #{remarkNum} where id = #{id}")
    public int updateRemarkNum(Integer remarkNum, Integer id);

    @Update("update topic set favor = favor+1 where id = #{id}")
    public int addFavor(Integer id);

    @Update("update topic set favor = favor+1 where id = #{id}")
    public int subFavor(Integer id);

    @Update("update topic set object_num = #{objectNum} where id = #{id}")
    public int updateObjectNum(Integer objectNum, Integer id);

    @Insert("insert into follow(topic_id,user_id) values(#{topicId},#{userId})")
    public int insertFollow(Integer topicId, Integer userId);

    @Delete("delete from follow where topic_id = #{topicId} and user_id = #{userId}")
    public int deleteFollow(Integer topicId, Integer userId);

    @Select("select count(*) from follow where topic_id = #{topicId} and user_id = #{userId}")
    public Boolean findFollow(Integer topicId, Integer userId);

    @Delete("delete from topic where id = #{topicId}")
    public void deleteTopic(Integer topicId);

    @Select("SELECT position FROM ( select id, ROW_NUMBER() OVER (ORDER BY id ASC) AS position FROM topic) AS ranked_topics WHERE id = #{id}")
    public Integer getPositionById(Integer id);

    @Select("SELECT class_id FROM topic WHERE id = #{id}")
    public Integer getClassIdByTopicId();

    @Update("update topic set views = views + 1 where id = #{id}")
    public void addViews(Integer id);
}





// CHANGE REPLICATION SOURCE TO SOURCE_HOST='124.70.129.93', SOURCE_USER='master1',SOURCE_PASSWORD=':ex.RSTcgF3M!Ls', SOURCE_LOG_FILE='mysql-bin.000006',SOURCE_LOG_POS=157;
// CHANGE MASTER TO MASTER_HOST='121.36.220.232', MASTER_USER='jiaoji',MASTER_PASSWORD=':ex.RSTcgF3M!Ls', MASTER_LOG_FILE='mysql-bin.000010',MASTER_LOG_POS=1267;