package org.example.jiaoji.service;

import java.util.List;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Remark;

import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;

public interface RemarkService {

    public Integer addRemark(Remark data);

    public PageInfo<Remark> SelectByObject(Integer objectId, Integer pageIndex, Integer pageSize, Boolean orderByHot);

    public Boolean isRemarked(Integer objectId,Integer uid);

    public List<Remark> SelectById(Integer id);

    public RetType changeLike(Integer id, Integer change, Integer uid);

    public RetType deleteRemark(Integer id);

    public RetType deleteUserObj(Integer objectId, Integer uid);

    public List<User> getAllUser();

    public Boolean isLike(Integer remarkId, Integer uid);

    public List<Integer> getScore(Integer objectId);
}
