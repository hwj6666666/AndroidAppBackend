package org.example.jiaoji.service;

import org.example.jiaoji.pojo.*;
import java.util.List;
import java.util.Map;

public interface UserService {

    public List<User> SelectAll();

    public RetType banUser(Integer id);

    public User SelectByUserId(Integer id);

    public List<Topic> SelectTopicsById(Integer id);

    public List<Objects> SelectObjectsById(Integer id);

    public List<Remark> SelectRemarksById(Integer id);

    public List<Topic> SelectFollows(Integer id);

    public User updateUser(Integer id, User user);

    public User updatePsd(Integer id, User user);

    public Map<String, String> getObjectNameAndTopicNameById(Integer objectId);

    public RetType Register(String email, String password,String avater,String username);

    public RetType Login(String email, String password);

    public List<User> search(String keyword);

    public String getAvater(Integer id);

    public String getUserName(Integer id);

    public Boolean checkNameExist(String username);

    public RetType reset(String email, String password);
}
