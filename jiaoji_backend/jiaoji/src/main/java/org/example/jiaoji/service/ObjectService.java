package org.example.jiaoji.service;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.*;

import java.util.List;

public interface ObjectService {

    public Integer InsertObject(Objects data);

    public PageInfo<Objects> SelectAllInTopic(Integer id, Integer pageSize, Integer pageIndex);

    public  List<Objects> SelectById(Integer id);

    public Topic SelectTopicById(Integer id);

    public double getAveScore(Integer id);

    public List<Objects> search(String keyword);

    public Remark getHottestRemark(Integer id);

    public List<top3Object> SelectTop3(Integer topicId);

    public RetType deleteObject(Integer objectId);

    public void updateAveScore(Integer id, Integer score);

    public void decAveScore(Integer id, Integer score);

    public void updateHotComment(Integer id, Integer likes, Integer change);
}
