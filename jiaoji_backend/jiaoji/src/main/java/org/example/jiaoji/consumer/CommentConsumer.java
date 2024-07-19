package org.example.jiaoji.consumer;

import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.Topic;
import org.example.jiaoji.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service  
public class CommentConsumer {  
    @Autowired
    private CommentService commentService;
    @Autowired
    private RemarkMapper remarkMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TopicMapper topicMapper;

    @KafkaListener(topics = "deleteComment", groupId = "deleteComment-group")  
    public void deleteComment(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        commentService.deleteById(id);
    } 



    @KafkaListener(topics = "addComment", groupId = "addComment-group")  
    public void addComment(String message) { 
        // 解析JSON字符串 
        Integer id=Integer.parseInt(message);
        Remark remark = remarkMapper.selectById(id).get(0);
        Objects object = objectMapper.selectById(remark.getObjectId()).get(0);
        Topic topic = topicMapper.selectById(object.getTopicId());
        topicMapper.updateRemarkNum(topic.getRemarkNum() + 1, topic.getId());
    }
}
