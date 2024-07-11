package org.example.jiaoji.service;

import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;

import java.util.List;

public interface ObjectService {

    public Integer InsertObject(Objects data);

    public List<Objects> SelectAllInTopic(Integer id);

    public  List<Objects> SelectById(Integer id);

    public Topic SelectTopicById(Integer id);

    public double getAveScore(Integer id);

    public List<Objects> search(String keyword);

    public String getHottestRemark(Integer id);

    public List<top3Object> SelectTop3(Integer topicId);

    public RetType deleteObject(Integer objectId);
}
