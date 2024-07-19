package org.example.jiaoji.controller;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Comment;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getComment() {
        PageInfo<Comment> mockPageInfo = new PageInfo<>();
        when(commentService.SelectByRemark(anyInt(), anyInt(), anyInt())).thenReturn(mockPageInfo);

        ResponseEntity<PageInfo<Comment>> response = commentController.getComment(1, 1, 10);
        assertEquals(mockPageInfo, response.getBody());

        verify(commentService, times(1)).SelectByRemark(anyInt(), anyInt(), anyInt());
    }

    @Test
    void insert() {
        Comment mockComment = new Comment();
        when(commentService.addComment(any(Comment.class))).thenReturn(1);

        Integer response = commentController.insert(mockComment);
        assertEquals(1, response);

        verify(commentService, times(1)).addComment(any(Comment.class));
    }

    @Test
    void delete() {
        RetType mockRetType = new RetType();
        when(commentService.deleteById(anyInt())).thenReturn(mockRetType);

        RetType response = commentController.delete(1);
        assertEquals(mockRetType, response);

        verify(commentService, times(1)).deleteById(anyInt());
    }
}
