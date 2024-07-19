package org.example.jiaoji.service.serverimpl;

import java.io.IOException;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.pojo.top3Object;
import org.example.jiaoji.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ObjectServiceImpl implements ObjectService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private RemarkMapper remarkMapper;
    @Autowired
    private RestHighLevelClient client;

    @Transactional
    public Integer InsertObject(Objects data) {
        RetType ret = new RetType();

        Integer id = objectMapper.selectIdByTitle(data.getTitle(), data.getTopicId());
        if (id != null) {
            ret.setMsg("该对象已存在");
            ret.setOk(false);
            ret.setData(null);
            return -1;
        }
        Objects object = new Objects();
        object.setTitle(data.getTitle());
        object.setDescription(data.getDescription());
        object.setUserId(data.getUserId());
        object.setPicture(data.getPicture());
        object.setAveScore(0);
        object.setRemarkNum(0);
        object.setHottestRemark("");
        object.setRemarkId(0);

        object.setTopicId(data.getTopicId());
        Topic topic = objectMapper.selectTopicById(object.getTopicId());
        topicMapper.updateObjectNum(topic.getObjectNum() + 1, topic.getId());
        objectMapper.insert(object);
        remarkMapper.insertScore(object.getId());
        IndexRequest request = new IndexRequest("object").id(object.getId().toString());

        // 2.准备Json文档
        request.source(JSON.toJSONString(object), XContentType.JSON);
        // 3.发送请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ret.setMsg("上传成功");

        ret.setOk(true);
        ret.setData(null);
        return object.getId();
    }

    public PageInfo<Objects> SelectAllInTopic(Integer id, Integer pageSize, Integer pageIndex) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Objects> objects = objectMapper.selectAllInTopic(id);
        PageInfo<Objects> pageInfo = new PageInfo<>(objects);
        return pageInfo;
    }

    public List<Objects> SelectById(Integer id) {
        return objectMapper.selectById(id);
    }

    public Topic SelectTopicById(Integer id) {
        return objectMapper.selectTopicById(id);
    }

    public double getAveScore(Integer id) {
        List<Remark> remarks = objectMapper.selectAllRemarks(id);
        if (remarks == null || remarks.isEmpty()) {
            return 0;
        }
        Integer length = remarks.size();
        double scores = 0;
        for (Remark remark : remarks) {
            scores += remark.getScore();
        }
        return scores / length;
    }

    public Remark getHottestRemark(Integer id) {
        List<Remark> remarks = objectMapper.selectAllRemarks(id);
        Integer likes = 0;
        Remark remark1 = null;
        for (Remark remark : remarks) {
            if (remark.getLike() > likes) {
                likes = remark.getLike();
                remark1 = remark;
            }
        }
        return remark1;
    }

    public List<Objects> search(String keyword) {
        keyword = "%" + keyword + "%";
        return objectMapper.search(keyword);
//        List<Objects> objects = new ArrayList<>();
//        SearchRequest request = new SearchRequest("object");
//        // 组织DSL参数
//        request.source()
//                .query(QueryBuilders.matchQuery(keyword, "title"));
//        request.source().from(0).size(100);
//        SearchResponse response = null;
//        try {
//            response = client.search(request, RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        SearchHits searchHits = response.getHits();
//        SearchHit[] hits = searchHits.getHits();
//        for (SearchHit hit : hits) {
//            String json = hit.getSourceAsString();
//            Objects object = JSON.parseObject(json, Objects.class);
//            System.out.println(object);
//            objects.add(object);
//        }
//        return objects;
    }

    public List<top3Object> SelectTop3(Integer topicId) {
        return objectMapper.selectTop3(topicId);
    }

    public RetType deleteObject(Integer objectId) {
        RetType ret = new RetType();
        objectMapper.delete(objectId);

        if (objectMapper.selectOneById(objectId) == null) {
            ret.setMsg("删除成功");
            ret.setOk(true);
            ret.setData(null);
        } else {
            ret.setMsg("删除失败");
            ret.setOk(false);
            ret.setData(null);
        }
        return ret;
    }

    public void updateAveScore(Integer id, Integer score) {
        Objects object = objectMapper.selectOneById(id);
        Integer remarkNum = object.getRemarkNum();
        double newScore = (object.getAveScore() * remarkNum + score) / (remarkNum + 1);
        objectMapper.updateAveScore(newScore, remarkNum + 1, id);
    }

    public void decAveScore(Integer id, Integer score) {
        Objects object = objectMapper.selectOneById(id);
        Integer remarkNum = object.getRemarkNum();
        double newScore = 0;
        if (remarkNum > 1) newScore = (object.getAveScore() * remarkNum - score) / (remarkNum - 1);
        objectMapper.updateAveScore(newScore, remarkNum - 1, id);
    }

    public void updateHotComment(Integer id, Integer remark_id, Integer change) {
        Objects object = objectMapper.selectOneById(id);
        Integer old_remark_id = object.getRemarkId();
        if (old_remark_id.equals(0)) {
            List<Remark> remarks = remarkMapper.selectById(remark_id);
            if (remarks == null || remarks.isEmpty()) {
                return;
            }
            Remark remark = remarks.getFirst();
            objectMapper.updateHotComment(id, remark.getContent(), remark_id);
            return;
        }
        if (old_remark_id.equals(remark_id)) {
            if (change < 0) {
                Remark remark = getHottestRemark(id);
                if (remark != null) objectMapper.updateHotComment(object.getId(), remark.getContent(), remark.getId());
                else objectMapper.updateHotComment(id, "", 0);
            }
        } else {
            List<Remark> old_remarks = remarkMapper.selectById(old_remark_id);
            List<Remark> remarks = remarkMapper.selectById(remark_id);
            if (remarks == null || remarks.isEmpty()) {
                return;
            }
            Remark remark = remarks.getFirst();
            if (old_remarks == null || old_remarks.isEmpty()) {
                objectMapper.updateHotComment(id, remark.getContent(), remark_id);
                return;
            }
            Remark old_remark = old_remarks.getFirst();
            if (old_remark.getLike() < remark.getLike()) {
                objectMapper.updateHotComment(id, remark.getContent(), remark_id);
            }
        }
    }
}
