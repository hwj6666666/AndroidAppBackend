package org.example.jiaoji.controller;

import com.github.pagehelper.PageInfo;
import org.example.jiaoji.pojo.Remark;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.pojo.User;
import org.example.jiaoji.service.RemarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemarkControllerTest {

    @Mock
    private RemarkService remarkService;

    @InjectMocks
    private RemarkController remarkController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRemark() {
        PageInfo<Remark> mockPageInfo = new PageInfo<>(Arrays.asList(new Remark(), new Remark()));
        when(remarkService.SelectByObject(anyInt(), anyInt(), anyInt(), anyBoolean())).thenReturn(mockPageInfo);

        ResponseEntity<PageInfo<Remark>> response = remarkController.getRemark(1, 1, 10, true);
        assertEquals(mockPageInfo, response.getBody());

        verify(remarkService, times(1)).SelectByObject(anyInt(), anyInt(), anyInt(), anyBoolean());
    }

    @Test
    void isRemark() {
        when(remarkService.isRemarked(anyInt(), anyInt())).thenReturn(true);

        ResponseEntity<Boolean> response = remarkController.isRemark(1, 1);
        assertEquals(true, response.getBody());

        verify(remarkService, times(1)).isRemarked(anyInt(), anyInt());
    }

    @Test
    void getScore() {
        List<Integer> mockScores = Arrays.asList(1, 2, 3);
        when(remarkService.getScore(anyInt())).thenReturn(mockScores);

        ResponseEntity<List<Integer>> response = remarkController.getScore(1);
        assertEquals(mockScores, response.getBody());

        verify(remarkService, times(1)).getScore(anyInt());
    }

    @Test
    void changeLike() {
        RetType mockRetType = new RetType();
        when(remarkService.changeLike(anyInt(), anyInt(), anyInt())).thenReturn(mockRetType);

        RetType response = remarkController.changeLike(1, 1, 1);
        assertEquals(mockRetType, response);

        verify(remarkService, times(1)).changeLike(anyInt(), anyInt(), anyInt());
    }

    @Test
    void delete() {
        RetType mockRetType = new RetType();
        when(remarkService.deleteRemark(anyInt())).thenReturn(mockRetType);

        RetType response = remarkController.delete(1);
        assertEquals(mockRetType, response);

        verify(remarkService, times(1)).deleteRemark(anyInt());
    }

    @Test
    void deleteUserObj() {
        RetType mockRetType = new RetType();
        when(remarkService.deleteUserObj(anyInt(), anyInt())).thenReturn(mockRetType);

        RetType response = remarkController.deleteUserObj(1, 1);
        assertEquals(mockRetType, response);

        verify(remarkService, times(1)).deleteUserObj(anyInt(), anyInt());
    }

    @Test
    void getAllUsers() {
        List<User> mockUsers = Arrays.asList(new User(), new User());
        when(remarkService.getAllUser()).thenReturn(mockUsers);

        ResponseEntity<List<User>> response = remarkController.getAllUsers();
        assertEquals(mockUsers, response.getBody());

        verify(remarkService, times(1)).getAllUser();
    }

    @Test
    void insert() {
        when(remarkService.addRemark(any(Remark.class))).thenReturn(1);

        Integer response = remarkController.insert(new Remark());
        assertEquals(1, response);

        verify(remarkService, times(1)).addRemark(any(Remark.class));
    }

    @Test
    void getLike() {
        when(remarkService.isLike(anyInt(), anyInt())).thenReturn(true);

        ResponseEntity<Boolean> response = remarkController.getLike(1, 1);
        assertEquals(true, response.getBody());

        verify(remarkService, times(1)).isLike(anyInt(), anyInt());
    }
}
