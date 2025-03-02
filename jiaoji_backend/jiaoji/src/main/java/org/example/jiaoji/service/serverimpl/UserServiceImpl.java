package org.example.jiaoji.service.serverimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.security.PasswordEncoder;
import org.example.jiaoji.service.UserService;
import org.example.jiaoji.utils.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired 
  private UserMapper userMapper;
  @Autowired
  private RestHighLevelClient client;
  @Autowired
  private KafkaProducerService kafkaProducer;

  public List<User> SelectAll() {
    return userMapper.selectAll();
  }

  public User SelectByUserId(Integer id) {
    return userMapper.selectByUserId(id);
  }

  public List<Topic> SelectTopicsById(Integer id) {
    return userMapper.selectTopicsByUserId(id);
  }

  public List<Objects> SelectObjectsById(Integer id) {
    return userMapper.selectObjectsByUserId(id);
  }

  public List<Remark> SelectRemarksById(Integer id) {
    return userMapper.selectRemarksByUserId(id);
  }

  public List<Topic> SelectFollows(Integer id) {
    return userMapper.selectFollows(id);
  }


  public User updateUser(Integer id, User user) {
    user.setId(id);
    userMapper.update(user);
    User newuser = userMapper.selectByUserId(id);
    kafkaProducer.sendMessage("ES_update_user", id);
    IndexRequest request = new IndexRequest("user").id(newuser.getId().toString());

    // 2.准备Json文档
    request.source(JSON.toJSONString(user), XContentType.JSON);
    // 3.发送请求
      try {
          client.index(request, RequestOptions.DEFAULT);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
      return newuser;
  }


  public User updatePsd(Integer id, User user) {
    User this_user=userMapper.selectByUserId(id);
    reset(this_user.getEmail(), user.getPassword());
    return userMapper.selectByUserId(id);
  }


  public Map<String, String> getObjectNameAndTopicNameById(Integer objectId) {
    return userMapper.selectObjectNameAndTopicNameById(objectId);
  }


  public RetType Register(String email, String password, String avatar,String  username){

    Integer id = userMapper.selectIdByEmail(email);
    RetType retType = new RetType();
    if (id != null) {
      retType.setMsg("邮箱已被占用");
      retType.setOk(false);
      return retType;
    }

    userMapper.insert(email, avatar, username);
    id = userMapper.selectIdByEmail(email);
    String salt=PasswordEncoder.generateRandomSalt();
    String encodedPassword=PasswordEncoder.encode(password,salt);
    userMapper.insertPassword(email,id,salt,encodedPassword);
    kafkaProducer.sendMessage("ES_store_user", id);
    
      retType.setData(id);
    retType.setMsg("注册成功");
    retType.setOk(true);
    return retType;
  }

  public RetType Login(String email, String password) {
    Integer id = userMapper.selectIdByEmail(email);
    RetType retType = new RetType();
    if (id == null) {
      retType.setData(null);
      retType.setMsg("邮箱未注册");
      retType.setOk(false);
      return retType;
    }
    password= PasswordEncoder.encode(password,userMapper.selectSaltByUid(id));
    id = userMapper.selectIdByEmailAndPassword(email, password);
    if (id == null) {
      retType.setData(null);
      retType.setMsg("密码错误");
      retType.setOk(false);
      return retType;
    }
    User user = userMapper.selectByUserId(id);
    if (user.getState() == 2) {
      retType.setData(null);
      retType.setMsg("用户已被封禁");
      retType.setOk(false);
      return retType;
    }
    retType.setData(id);
    retType.setMsg("登录成功");
    retType.setOk(true);
    return retType;
  }

  public List<User> search(String keyword) {
//    System.out.println(keyword);
//    keyword = "%" + keyword + "%";
//    return userMapper.search(keyword);
    List<User> users = new ArrayList<>();
    SearchRequest request = new SearchRequest("user");

    request.source()
            .query(QueryBuilders.boolQuery()
                    .should(QueryBuilders.termQuery("email", keyword)) // 精确匹配 email
                    .should(QueryBuilders.matchQuery("username", keyword)) // 全文搜索 username
            );
    request.source().from(0).size(100);
    SearchResponse response = null;
    try {
      response = client.search(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    SearchHits searchHits = response.getHits();
    SearchHit[] hits = searchHits.getHits();
    for (SearchHit hit : hits) {
      String json = hit.getSourceAsString();
      User user = JSON.parseObject(json, User.class);
      users.add(user);
    }
    return users;
  }

  public RetType banUser(Integer id) {
    RetType retType = new RetType();
    User user = userMapper.selectByUserId(id);
    if (user.getId() == 1) {
      retType.setOk(false);
      retType.setMsg("超级管理员不可封禁");
      return retType;
    } else if (user.getState() == 2) {
      userMapper.updateSuper(id, 0);
      retType.setOk(true);
      retType.setMsg("解封成功");
      retType.setData(0);
      return retType;
    } else {
      userMapper.updateSuper(id, 2);
      retType.setOk(true);
      retType.setMsg("封禁成功");
      retType.setData(2);
      return retType;
    }
  }

  @Override
  public String getAvater(Integer id) {
    return userMapper.getAvater(id);
  }

  @Override
  public String getUserName(Integer id) {
    return userMapper.selectByUserId(id).getUsername();
  }


  @Override
  public Boolean checkNameExist(String username) {
    return userMapper.selectIfNameExist(username) !=0;
  }

  @Override
  public RetType reset(String email, String password) {
    Integer id = userMapper.selectIdByEmail(email);
    RetType retType = new RetType();
    if (id == null) {
      retType.setOk(false);
      retType.setMsg("邮箱未注册");
      return retType;
    }
    String salt=userMapper.selectSaltByUid(id);
    String encodedPassword=PasswordEncoder.encode(password,salt);
    userMapper.updatePassword(id, encodedPassword);
    retType.setOk(true);
    retType.setMsg("重置密码成功");
    return retType;
  }
}
