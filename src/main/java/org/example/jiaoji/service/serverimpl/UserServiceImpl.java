package org.example.jiaoji.service.serverimpl;

import java.util.List;
import java.util.Map;
import org.example.jiaoji.mapper.UserMapper;
import org.example.jiaoji.pojo.*;
import org.example.jiaoji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserMapper userMapper;

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
    return userMapper.selectByUserId(id);
  }


  public User updatePsd(Integer id, User user) {
    user.setId(id);
    userMapper.updateUserPsd(user);
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

    userMapper.insert(email, password, avatar,username);
    id = userMapper.selectIdByEmail(email);
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
    keyword = "%" + keyword + "%";
    return userMapper.search(keyword);
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
    return userMapper.checkNameExist(username) !=0;
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
    userMapper.resetPassword(id, password);
    retType.setOk(true);
    retType.setMsg("重置密码成功");
    return retType;
  }
}
