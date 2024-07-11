package org.example.jiaoji.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.User;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> selectAll();

    @Select("select * from user where id = #{id}")
    public User selectByUserId(Integer id);

    @Select("select * from topic where topic.user_id = #{id}")
    public List<Topic> selectTopicsByUserId(Integer id);

    @Select("select * from object where object.user_id = #{id}")
    public List<Objects> selectObjectsByUserId(Integer id);

    @Select("select * from remarks where remarks.user_id = #{id}")
    public List<Remark> selectRemarksByUserId(Integer id);

    @Select("select * from topic\n" +
            "where topic.id in (select topic_id from follow where follow.user_id = #{id})")
    public List<Topic> selectFollows(Integer id);

    @Update("update user set username=#{username}, note=#{note}, avatar=#{avatar} where id=#{id}")
    void update(User user);

    @Update("update user set password=#{password} where id=#{id}")
    void updateUserPsd(User user);

    @Select("SELECT o.title as objectName, t.title as topicName FROM object o JOIN topic t ON o.topic_id = t.id WHERE o.id = #{objectId}")
    Map<String, String> selectObjectNameAndTopicNameById(Integer objectId);

    @Select("select id from user where email = #{email}")
    public Integer selectIdByEmail(String email);

    @Select("select uid from password where email = #{email} and password = #{password}")
    public Integer selectIdByEmailAndPassword(String email, String password);

    @Select("select * from remarks where object_id = #{object_id}")
    public List<Remark> selectAllRemarks(Integer object_id);

    @Update("update user set password = #{password} where id = #{id}")
    public void resetPassword(Integer id, String password);

    @Update("insert into user(email,avatar,username) values(#{email},#{avatar},#{username})")
    public void insert(String email, String avatar,String username);

    @Update("insert into password(email, uid, salt, password) values(#{email},#{uid},#{salt},#{password})")
    public void insertPassword(String email, Integer uid, String salt, String password);

    @Select("select * from user where username like #{keyword} or email like #{keyword}")
    public List<User> search(String keyword);

    @Update("update user set state = #{sup} where id = #{id}")
    public void updateSuper(Integer id, Integer sup);

    @Select("select avatar from user where id = #{id}")
    public String getAvater(Integer id);


    @Select("select count(*) from user where username = #{username}")
    public Integer checkNameExist(String username);

    @Select("select salt from password where uid = #{uid}")
    public String selectSaltByUid(Integer uid);
}
