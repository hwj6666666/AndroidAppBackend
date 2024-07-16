package org.example.jiaoji.service.serverimpl;

import java.util.List;

import org.example.jiaoji.mapper.ClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.ClassService;



@Service
public class ClassServiceImpl implements ClassService{
    @Autowired
    private ClassMapper classMapper;

    public List<Class> selectAll(){
        return classMapper.selectAll();
    }

    public RetType addFavor(Integer classId, Integer userId){
        RetType ret = new RetType();

        int res = classMapper.addFavor(classId, userId);
        if(res == 1){
            ret.setOk(true);
            ret.setMsg("关注成功");
            ret.setData(null);
        }else{
            ret.setOk(false);
            ret.setMsg("未知原因，收藏失败，请联系管理员");
            ret.setData(null);
        }
        return ret;
    }

    public RetType deleteFavor(Integer classId, Integer userId){
        RetType ret = new RetType();

        int res = classMapper.deleteFavor(classId, userId);
        if(res == 1){
            ret.setOk(true);
            ret.setMsg("取关成功");
            ret.setData(null);
        }else{
            ret.setOk(false);
            ret.setMsg("未知原因，取消收藏失败，请联系管理员");
            ret.setData(null);
        }
        return ret;
    }

    public List<Integer> selectFavor(Integer userId){
        return classMapper.selectFavor(userId);
    }

}
