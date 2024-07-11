package org.example.jiaoji.controller;

import org.example.jiaoji.pojo.Class;
import org.example.jiaoji.pojo.RetType;
import org.example.jiaoji.service.ClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClassControllerTest {

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassController classController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getMethodName() {
        List<Class> mockClasses = new ArrayList<>();
        when(classService.selectAll()).thenReturn(mockClasses);

        List<Class> response = classController.getMethodName();
        assertEquals(mockClasses, response);

        verify(classService, times(1)).selectAll();
    }

    @Test
    void getMethodName2() {
        RetType mockRetType = new RetType();
        when(classService.addFavor(anyInt(), anyInt())).thenReturn(mockRetType);

        RetType response = classController.getMethodName2(1, 1);
        assertEquals(mockRetType, response);

        verify(classService, times(1)).addFavor(anyInt(), anyInt());
    }

    @Test
    void getMethodName3() {
        RetType mockRetType = new RetType();
        when(classService.deleteFavor(anyInt(), anyInt())).thenReturn(mockRetType);

        RetType response = classController.getMethodName3(1, 1);
        assertEquals(mockRetType, response);

        verify(classService, times(1)).deleteFavor(anyInt(), anyInt());
    }

    @Test
    void getMethodName4() {
        List<Integer> mockFavorList = new ArrayList<>();
        when(classService.selectFavor(anyInt())).thenReturn(mockFavorList);

        List<Integer> response = classController.getMethodName4(1);
        assertEquals(mockFavorList, response);

        verify(classService, times(1)).selectFavor(anyInt());
    }
}
