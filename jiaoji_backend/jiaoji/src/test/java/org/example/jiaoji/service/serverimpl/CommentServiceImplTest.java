package org.example.jiaoji.service.serverimpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.jiaoji.mapper.CommentMapper;
import org.example.jiaoji.mapper.ObjectMapper;
import org.example.jiaoji.mapper.RemarkMapper;
import org.example.jiaoji.mapper.TopicMapper;
import org.example.jiaoji.pojo.Comment;
import org.example.jiaoji.pojo.Objects;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private RemarkMapper remarkMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private TopicMapper topicMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addComment() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setRemarkId(2);

        Remark remark = new Remark();
        remark.setId(2);
        remark.setObjectId(3);

        Objects object = new Objects();
        object.setId(3);
        object.setTopicId(4);

        Topic topic = new Topic();
        topic.setId(4);
        topic.setRemarkNum(5);

        doNothing().when(commentMapper).insert(any(Comment.class));
        when(remarkMapper.selectById(anyInt())).thenReturn(List.of(remark));
        when(objectMapper.selectById(anyInt())).thenReturn(List.of(object));
        when(topicMapper.selectById(anyInt())).thenReturn(topic);
        when(topicMapper.updateRemarkNum(anyInt(), anyInt())).thenReturn(1);

        Integer result = commentService.addComment(comment);

        assertEquals(1, result);
        verify(commentMapper, times(1)).insert(comment);
        verify(remarkMapper, times(1)).selectById(anyInt());
        verify(objectMapper, times(1)).selectById(anyInt());
        verify(topicMapper, times(1)).selectById(anyInt());
        verify(topicMapper, times(1)).updateRemarkNum(anyInt(), anyInt());
    }

    @Test
    void SelectByRemark() {
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId(1);
        comment1.setRemarkId(2);
        comments.add(comment1);

        when(commentMapper.selectByRemark(anyInt())).thenReturn(comments);

        PageInfo<Comment> result = commentService.SelectByRemark(2, 10, 1);

        assertEquals(1, result.getList().size());
        assertEquals(comment1, result.getList().get(0));
        verify(commentMapper, times(1)).selectByRemark(anyInt());
    }

    @Test
    void deleteById() {
        when(commentMapper.selectById(anyInt())).thenReturn(new ArrayList<>());

        RetType result = commentService.deleteById(1);

        assertEquals(true, result.isOk());
        assertEquals("删除成功", result.getMsg());
        verify(commentMapper, times(1)).deleteById(anyInt());
        verify(commentMapper, times(1)).selectById(anyInt());

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment());

        when(commentMapper.selectById(anyInt())).thenReturn(comments);

        result = commentService.deleteById(1);

        assertEquals(false, result.isOk());
        assertEquals("删除失败", result.getMsg());
        verify(commentMapper, times(2)).deleteById(anyInt());
        verify(commentMapper, times(2)).selectById(anyInt());
    }
}
